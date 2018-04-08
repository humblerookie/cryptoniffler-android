package com.bariski.cryptoniffler.data.factory

import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import com.bariski.cryptoniffler.data.api.EventsApi
import com.bariski.cryptoniffler.data.api.models.AuthTokenResponse
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.data.utils.getAssetFromDevice
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.domain.util.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import retrofit2.HttpException
import java.util.*
import kotlin.collections.ArrayList

class EventsRepositoryImpl(private val api: EventsApi, private val remoteConfig: FirebaseRemoteConfig, private val keyValue: KeyValueStore, private val moshi: Moshi, private val context: Context, private val scheduler: Schedulers) : EventsRepository {

    private val EVENT_FILTER_TIMESTAMP_COINS = "event_filter_timestamp_coins_v2"
    private val EVENT_FILTER_TIMESTAMP_CATEGORIES = "event_filter_timestamp_categories_v2"
    private val EVENT_FILTER_CATEGORY = "event_filter_category_v2"
    private val EVENT_FILTER_COINS = "event_filter_coins_v2"
    private val FLAG_TUTORIAL_EVENT_FILTER = "flag_tutorial_event_filter"
    private val API_TOKEN = "event_api_token"


    override fun getAndSaveToken(): Single<AuthTokenResponse> {
        val url = Uri.parse(remoteConfig.getString(EVENTS_BASE_URL) + TOKEN_SUFFIX_URL).buildUpon()
                .appendQueryParameter("grant_type", "client_credentials")
                .appendQueryParameter("client_id", remoteConfig.getString(EVENT_CLIENT))
                .appendQueryParameter("client_secret", remoteConfig.getString(EVENT_SECRET)).build().toString()
        return api.getToken(url).map {
            saveToken(it.token)
            it
        }
    }

    override fun isAuthenticated() = getToken() != null


    override fun getCoins(): Single<List<CalendarCoin>> {
        val delta = Calendar.getInstance().timeInMillis - keyValue.getLong(EVENT_FILTER_TIMESTAMP_COINS)
        return if (delta >= AlarmManager.INTERVAL_DAY) {
            val url = getResolvedUrlBuilder(COIN_SUFFIX_URL).build().toString()
            api.getCoins(url)
                    .map {
                        keyValue.storeLong(EVENT_FILTER_TIMESTAMP_COINS, Calendar.getInstance().timeInMillis)
                        saveEventCoins(it)
                        it
                    }

                    .onErrorReturn {
                        if (shouldAuthenticate(it)) {
                            getAndSaveToken().blockingGet()
                            val coins = api.getCoins(getResolvedUrlBuilder(COIN_SUFFIX_URL)
                                    .build().toString()).blockingGet()
                            keyValue.storeLong(EVENT_FILTER_TIMESTAMP_COINS, Calendar.getInstance().timeInMillis)
                            saveEventCoins(coins)
                            coins

                        } else {
                            getCachedCoins()
                        }
                    }
        } else {
            Single.just(getCachedCoins())
        }

    }


    private fun shouldAuthenticate(throwable: Throwable): Boolean {
        if (throwable is HttpException) {
            return throwable.code() == HttpStatus.SC_UNAUTHORIZED || throwable.code() == HttpStatus.SC_GONE
        }
        return false
    }

    override fun getEvents(coins: String?, categories: String?, startDate: String?, endDate: String?, page: Int, max: Int): Single<ArrayList<Event>> {
        val builder = getResolvedUrlBuilder(EVENT_SUFFIX_URL)
        coins?.let {
            if (it.isNotEmpty()) {
                builder.appendQueryParameter("coins", it)
            }
        }
        categories?.let {
            if (it.isNotEmpty()) {
                builder.appendQueryParameter("categories", it)
            }
        }
        startDate?.let { builder.appendQueryParameter("dateRangeStart", it) }
        endDate?.let { builder.appendQueryParameter("dateRangeEnd", it) }
        page?.let { builder.appendQueryParameter("page", it.toString()) }
        max?.let { builder.appendQueryParameter("max", it.toString()) }

        return api.getEvents(builder.build().toString())
                .observeOn(scheduler.io())
                .map { ArrayList(it) }
                .onErrorReturn {
                    if (shouldAuthenticate(it)) {
                        getAndSaveToken().blockingGet()
                        ArrayList(api.getEvents(builder.build().toString()).blockingGet())
                    } else {
                        throw it
                    }
                }

    }

    override fun getCategories(): Single<List<CalendarCategory>> {
        val delta = Calendar.getInstance().timeInMillis - keyValue.getLong(EVENT_FILTER_TIMESTAMP_CATEGORIES)
        return if (delta >= AlarmManager.INTERVAL_DAY) {
            api.getCategories(getResolvedUrlBuilder(CATEGORIES_SUFFIX_URL).build().toString())
                    .map {
                        keyValue.storeLong(EVENT_FILTER_TIMESTAMP_CATEGORIES, Calendar.getInstance().timeInMillis)
                        saveCategories(it)
                        it
                    }.onErrorReturn {
                        if (shouldAuthenticate(it)) {
                            getAndSaveToken().blockingGet()
                            val cats = api.getCategories(getResolvedUrlBuilder(CATEGORIES_SUFFIX_URL)
                                    .build().toString()).blockingGet()
                            keyValue.storeLong(EVENT_FILTER_TIMESTAMP_CATEGORIES, Calendar.getInstance().timeInMillis)
                            saveCategories(cats)
                            cats
                        } else {
                            getCachedCategories()
                        }
                    }
        } else {
            Single.just(getCachedCategories())
        }

    }

    private fun getCachedCategories(): List<CalendarCategory> {
        val adapter = moshi.adapter<List<CalendarCategory>>(Types.newParameterizedType(List::class.java, CalendarCategory::class.java))
        var pref: String? = keyValue.getString(EVENT_FILTER_CATEGORY)
        if (pref == null) {
            pref = getAssetFromDevice("filter_categories.json", context)
        }
        return adapter.fromJson(pref)!!
    }

    private fun getCachedCoins(): List<CalendarCoin> {
        val adapter = moshi.adapter<List<CalendarCoin>>(Types.newParameterizedType(List::class.java, CalendarCoin::class.java))
        var pref: String? = keyValue.getString(EVENT_FILTER_COINS)
        if (pref == null) {
            pref = getAssetFromDevice("filter_coins.json", context)
        }
        return adapter.fromJson(pref)!!
    }

    private fun saveCategories(data: List<CalendarCategory>) {
        val adapter = moshi.adapter<List<CalendarCategory>>(Types.newParameterizedType(List::class.java, CalendarCategory::class.java))
        keyValue.storeString(EVENT_FILTER_CATEGORY, adapter.toJson(data))
    }

    private fun saveEventCoins(data: List<CalendarCoin>) {
        val adapter = moshi.adapter<List<CalendarCoin>>(Types.newParameterizedType(List::class.java, CalendarCoin::class.java))
        keyValue.storeString(EVENT_FILTER_COINS, adapter.toJson(data))
    }

    private fun saveToken(data: String) {
        keyValue.storeString(API_TOKEN, data)
    }

    private fun getToken(): String? {
        return keyValue.getString(API_TOKEN)
    }

    private fun getResolvedUrlBuilder(suffix: String): Uri.Builder {
        return Uri.parse(remoteConfig.getString(EVENTS_BASE_URL) + suffix).buildUpon().appendQueryParameter(KEY_TOKEN, getToken())
    }

    override fun isFilterTutorialShown() = keyValue.getBoolean(FLAG_TUTORIAL_EVENT_FILTER)

    override fun setFilterTutorialShown(b: Boolean) {
        keyValue.storeBoolean(FLAG_TUTORIAL_EVENT_FILTER, b)
    }

}