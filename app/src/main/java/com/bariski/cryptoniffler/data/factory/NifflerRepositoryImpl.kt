package com.bariski.cryptoniffler.data.factory

import android.app.AlarmManager
import android.content.Context
import com.bariski.cryptoniffler.data.api.CryptoNifflerApi
import com.bariski.cryptoniffler.data.api.models.BestCoin
import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.data.api.models.CoinsAndExchanges
import com.bariski.cryptoniffler.data.cache.DataCache
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.data.utils.getAssetFromDevice
import com.bariski.cryptoniffler.domain.model.*
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.BTC_INR_API
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class NifflerRepositoryImpl(val context: Context, private val api: CryptoNifflerApi,
                            private val remoteConfig: FirebaseRemoteConfig, private val keyValueStore: KeyValueStore,
                            private val moshi: Moshi, private val cache: DataCache) : NifflerRepository {


    override fun fetchLatestConfig() {
        var cacheExpiration = if (remoteConfig.info.configSettings.isDeveloperModeEnabled) 0 else AlarmManager.INTERVAL_DAY / 1000
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener({
            if (it.isSuccessful) {
                remoteConfig.activateFetched()
            }
        })
    }

    override fun getArbitrage(source: Set<FilterItem>, dest: Set<FilterItem>): Single<Arbitrage> {
        return api.getArbitrage(source.joinToString(",", transform = { it.getIdentifier() }), dest.joinToString(",", transform = { it.getIdentifier() }))
    }

    override fun getCoins(): Single<ArrayList<Coin>> {
        val delta = Calendar.getInstance().timeInMillis - keyValueStore.getLong(KEY_TIMESTAMP_COINSNEXCHANGES)
        if (delta >= CACHE_EXPIRATION) {
            return api.getCoinsAndExchanges().map {
                updateMaps(it)
                keyValueStore.storeLong(KEY_TIMESTAMP_COINSNEXCHANGES, Calendar.getInstance().timeInMillis)
                keyValueStore.storeString(KEY_DATA_COINSNEXCHANGES, moshi.adapter<CoinsAndExchanges>(CoinsAndExchanges::class.java).toJson(it))
                ArrayList(it.coins)
            }.onErrorReturn {
                val cNe = getDiskCachedData()
                updateMaps(cNe)
                ArrayList(cNe.coins)
            }


        } else {
            return Single.just(1).map {
                if (cache.getCoins().size == 0) {
                    updateMaps(getDiskCachedData())
                }
                ArrayList(cache.getCoins().values)
            }.map { it.sortByDescending { it.priority }; it }
        }
    }

    private fun updateMaps(it: CoinsAndExchanges) {
        for (c in it.coins) {
            cache.getCoins()[c.symbol.toUpperCase()] = c
        }
        for (e in it.exchanges) {
            cache.getExchanges()[e.symbol.toUpperCase()] = e
        }
    }

    private fun getDiskCachedData(): CoinsAndExchanges {
        val adapter = moshi.adapter<CoinsAndExchanges>(CoinsAndExchanges::class.java)
        var pref: String? = keyValueStore.getString(KEY_DATA_COINSNEXCHANGES)
        if (pref == null) {
            pref = getAssetFromDevice("exchanges_coins.json", context)
        }
        return adapter.fromJson(pref)!!
    }


    override fun getExchanges(): Single<ArrayList<Exchange>> {

        val delta = Calendar.getInstance().timeInMillis - keyValueStore.getLong(KEY_TIMESTAMP_COINSNEXCHANGES)
        if (delta >= CACHE_EXPIRATION) {
            return api.getCoinsAndExchanges().map {
                updateMaps(it)
                keyValueStore.storeLong(KEY_TIMESTAMP_COINSNEXCHANGES, Calendar.getInstance().timeInMillis)
                keyValueStore.storeString(KEY_DATA_COINSNEXCHANGES, moshi.adapter<CoinsAndExchanges>(CoinsAndExchanges::class.java).toJson(it))
                ArrayList(it.exchanges)
            }.onErrorReturn {
                val cNe = getDiskCachedData()
                updateMaps(cNe)
                ArrayList(cNe.exchanges)
            }
        } else {
            return Single.just(1).map {
                if (cache.getExchanges().size == 0) {
                    updateMaps(getDiskCachedData())
                }
                ArrayList(cache.getExchanges().values)
            }.map { it.sortByDescending { it.priority }; it }
        }
    }

    override fun getBtcInrRates() = api.getBtcInrRate(remoteConfig.getString(BTC_INR_API))

    override fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse> {
        return api.getBestRates(coin, amount, ignoreFees)
                .map { it ->
                    getExchanges().blockingGet()
                    val list = ArrayList<BestCoin>()
                    it.values.mapTo(list) { it.copy(imgUrl = cache.getExchanges()[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(values = list, imgUrl = cache.getCoins()[it.symbol.toUpperCase()]?.imgUrl)
                    response
                }
    }

    override fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse> {
        return api.getBestCoin(exchange, amount, ignoreFees)
                .map { it ->

                    getCoins().blockingGet() //updates Map
                    val list = ArrayList<BestCoin>()
                    it.coins.mapTo(list) { it.copy(imgUrl = cache.getCoins()[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(coins = list, imgUrl = cache.getExchanges()[it.exchangeSymbol.toUpperCase()]?.imgUrl)
                    response
                }
    }

    override fun hasDrawerBeenShown() = keyValueStore.getBoolean(KEY_FLAG_NAV_DRAWER_SHOWN)
    override fun setDrawerShown(b: Boolean) {
        keyValueStore.storeBoolean(KEY_FLAG_NAV_DRAWER_SHOWN, b)
    }

    override fun hasArbDialogBeenShown() = keyValueStore.getBoolean(KEY_ARB_SHOWN)
    override fun setArbDialogBeenShown(b: Boolean) {
        keyValueStore.storeBoolean(KEY_ARB_SHOWN, b)
    }

    override fun getFiltersList(): Single<ArbitrageFilter>? {
        keyValueStore.getString(KEY_DATA_ARB_FILTERS)?.let { str ->
            return Single.just(1).map { moshi.adapter<ArbitrageFilter>(ArbitrageFilter::class.java).fromJson(str)!! }
        }
        return null
    }

    override fun setFiltersList(filter: ArbitrageFilter) {
        keyValueStore.storeString(KEY_DATA_ARB_FILTERS, moshi.adapter<ArbitrageFilter>(ArbitrageFilter::class.java).toJson(filter))
    }

    override fun getSourceList(): Set<ArbitrageExchange> {
        keyValueStore.getString(KEY_DATA_SRC_EXCHANGES)?.let {
            return HashSet(moshi.adapter<Set<ArbitrageExchange>>(Types.newParameterizedType(Set::class.java, ArbitrageExchange::class.java)).fromJson(it))
        }
        return HashSet()

    }

    override fun setSourceList(filter: Set<ArbitrageExchange>) {
        keyValueStore.storeString(KEY_DATA_SRC_EXCHANGES, moshi.adapter<Set<ArbitrageExchange>>(Types.newParameterizedType(Set::class.java, ArbitrageExchange::class.java)).toJson(filter))
    }

    override fun getDestList(): Set<ArbitrageExchange> {
        keyValueStore.getString(KEY_DATA_DEST_EXCHANGES)?.let {
            return HashSet(moshi.adapter<Set<ArbitrageExchange>>(Types.newParameterizedType(Set::class.java, ArbitrageExchange::class.java)).fromJson(it))
        }
        return HashSet()

    }

    override fun setDestList(filter: Set<ArbitrageExchange>) {
        keyValueStore.storeString(KEY_DATA_DEST_EXCHANGES, moshi.adapter<Set<ArbitrageExchange>>(Types.newParameterizedType(Set::class.java, ArbitrageExchange::class.java)).toJson(filter))
    }

    override fun isArbFilterTutorialShown() = keyValueStore.getBoolean(KEY_ARB_FILTER_SHOWN)

    override fun setArbFilterTutorialShown(b: Boolean) {
        keyValueStore.storeBoolean(KEY_ARB_FILTER_SHOWN, b)
    }

    override fun isRateNShareShown() = keyValueStore.getBoolean(KEY_RATE_SHARE_SHOWN)

    override fun setRateNShareShown(b: Boolean) {
        keyValueStore.storeBoolean(KEY_RATE_SHARE_SHOWN, b)
    }

    override fun getArbitrageUsedCount() = keyValueStore.getLong(KEY_ARB_COUNT)

    override fun setArbitrageUsedCount(b: Long) {
        keyValueStore.storeLong(KEY_ARB_COUNT, b)
    }

    override fun isInternationalArbitrage() = keyValueStore.getBoolean(KEY_ARB_INTERNATIONAL)

    override fun setInternationalArbitrage(b: Boolean) {
        keyValueStore.storeBoolean(KEY_ARB_INTERNATIONAL, b)
    }


    private val KEY_TIMESTAMP_COINSNEXCHANGES = "timestamp_coinsnexchanges"
    private val KEY_DATA_COINSNEXCHANGES = "data_coinsnexchanges"
    private val KEY_DATA_ARB_FILTERS = "data_arb_filters"
    private val KEY_DATA_SRC_EXCHANGES = "data_src_exchanges"
    private val KEY_DATA_DEST_EXCHANGES = "data_dest_exchanges"
    private val KEY_FLAG_NAV_DRAWER_SHOWN = "flag_nav_drawer_shown_v2"
    private val KEY_ARB_SHOWN = "flag_arb_shown"
    private val KEY_RATE_SHARE_SHOWN = "flag_rate_share_shown"
    private val KEY_ARB_COUNT = "flag_arb_count"
    private val KEY_ARB_FILTER_SHOWN = "flag_arb_filter_shown"
    private val KEY_ARB_INTERNATIONAL = "flag_arb_international"
    private val CACHE_EXPIRATION = AlarmManager.INTERVAL_HOUR * 2
}