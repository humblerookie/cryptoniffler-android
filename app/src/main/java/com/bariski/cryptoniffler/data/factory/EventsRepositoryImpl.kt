package com.bariski.cryptoniffler.data.factory

import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import com.bariski.cryptoniffler.data.api.EventsApi
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.data.utils.getAssetFromDevice
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.domain.util.CATEGORIES_SUFFIX_URL
import com.bariski.cryptoniffler.domain.util.COIN_SUFFIX_URL
import com.bariski.cryptoniffler.domain.util.EVENTS_BASE_URL
import com.bariski.cryptoniffler.domain.util.EVENT_SUFFIX_URL
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import java.util.*

class EventsRepositoryImpl(private val api: EventsApi, private val remoteConfig: FirebaseRemoteConfig, private val keyValue: KeyValueStore, private val moshi: Moshi, private val context: Context, private val scheduler: Schedulers) : EventsRepository {

    private val EVENT_FILTER_TIMESTAMP_COINS = "event_filter_timestamp_coins"
    private val EVENT_FILTER_TIMESTAMP_CATEGORIES = "event_filter_timestamp_categories"
    private val EVENT_FILTER_CATEGORY = "event_filter_category"
    private val EVENT_FILTER_COINS = "event_filter_coins"
    private val FLAG_TUTORIAL_EVENT_FILTER = "flag_tutorial_event_filter"

    override fun getCoins(): Single<List<String>> {
        val delta = Calendar.getInstance().timeInMillis - keyValue.getLong(EVENT_FILTER_TIMESTAMP_COINS)
        return if (delta >= AlarmManager.INTERVAL_DAY) {
            api.getCategories(remoteConfig.getString(EVENTS_BASE_URL) + COIN_SUFFIX_URL)
                    .map {
                        keyValue.storeLong(EVENT_FILTER_TIMESTAMP_COINS, Calendar.getInstance().timeInMillis)
                        saveEventCoins(it)
                        it
                    }
                    .onErrorReturn { getCachedCoins() }
        } else {
            Single.just(1).observeOn(scheduler.io()).map { getCachedCoins() }
        }

    }

    override fun getEvents(coins: String?, categories: String?, startDate: String?, endDate: String?, page: Int, max: Int): Single<List<Event>> {
        val builder = Uri.parse(remoteConfig.getString(EVENTS_BASE_URL) + EVENT_SUFFIX_URL).buildUpon()
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
    }

    override fun getCategories(): Single<List<String>> {
        val delta = Calendar.getInstance().timeInMillis - keyValue.getLong(EVENT_FILTER_TIMESTAMP_CATEGORIES)
        return if (delta >= AlarmManager.INTERVAL_DAY) {
            api.getCategories(remoteConfig.getString(EVENTS_BASE_URL) + CATEGORIES_SUFFIX_URL).map {
                keyValue.storeLong(EVENT_FILTER_TIMESTAMP_CATEGORIES, Calendar.getInstance().timeInMillis)
                saveCategories(it)
                it
            }.onErrorReturn { getCachedCategories() }
        } else {
            Single.just(1).map { getCachedCategories() }
        }

    }

    private fun getCachedCategories(): List<String> {
        val adapter = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        var pref: String? = keyValue.getString(EVENT_FILTER_CATEGORY)
        if (pref == null) {
            pref = getAssetFromDevice("filter_categories.json", context)
        }
        return adapter.fromJson(pref)!!
    }

    private fun getCachedCoins(): List<String> {
        val adapter = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        var pref: String? = keyValue.getString(EVENT_FILTER_COINS)
        if (pref == null) {
            pref = getAssetFromDevice("filter_coins.json", context)
        }
        return adapter.fromJson(pref)!!
    }

    private fun saveCategories(data: List<String>) {
        val adapter = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        keyValue.storeString(EVENT_FILTER_CATEGORY, adapter.toJson(data))
    }

    private fun saveEventCoins(data: List<String>) {
        val adapter = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
        keyValue.storeString(EVENT_FILTER_COINS, adapter.toJson(data))
    }

    override fun isFilterTutorialShown() = keyValue.getBoolean(FLAG_TUTORIAL_EVENT_FILTER)

    override fun setFilterTutorialShown(b: Boolean) {
        keyValue.storeBoolean(FLAG_TUTORIAL_EVENT_FILTER, b)
    }

}