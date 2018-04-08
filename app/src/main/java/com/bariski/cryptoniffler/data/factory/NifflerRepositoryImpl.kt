package com.bariski.cryptoniffler.data.factory

import android.app.AlarmManager
import android.content.Context
import com.bariski.cryptoniffler.data.api.CryptoNifflerApi
import com.bariski.cryptoniffler.data.api.models.BestCoin
import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.data.api.models.CoinsAndExchanges
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.data.utils.getAssetFromDevice
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.BTC_INR_API
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList

//http://jsoneditoronline.org/?id=296639de32b80b0cf4aa48aeeb343d43
//http://jsoneditoronline.org/?id=ba02e037b8700a258d14f50f6b462dfc

class NifflerRepositoryImpl(val context: Context, private val api: CryptoNifflerApi, private val remoteConfig: FirebaseRemoteConfig, private val keyValueStore: KeyValueStore, private val moshi: Moshi) : NifflerRepository {


    private var mapOfCoins = HashMap<String, Coin>()
    private val mapOfExchanges = HashMap<String, Exchange>()
    private val KEY_TIMESTAMP_COINSNEXCHANGES = "timestamp_coinsnexchanges"
    private val KEY_DATA_COINSNEXCHANGES = "data_coinsnexchanges"
    private val KEY_FLAG_NAV_DRAWER_SHOWN = "flag_nav_drawer_shown_v2"
    private val KEY_ARB_SHOWN = "flag_arb_shown"
    private val CACHE_EXPIRATION = AlarmManager.INTERVAL_HOUR * 2


    override fun fetchLatestConfig() {
        var cacheExpiration = if (remoteConfig.info.configSettings.isDeveloperModeEnabled) 0 else AlarmManager.INTERVAL_DAY / 1000
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener({
            if (it.isSuccessful) {
                remoteConfig.activateFetched()
                //interceptor.setHost(remoteConfig.getString(BASE_URL))
            }
        })
    }

    override fun getArbitrage(): Single<Arbitrage> {
        return api.getArbitrage()
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
                val cNe = getCachedData()
                updateMaps(cNe)
                ArrayList(cNe.coins)
            }


        } else {
            return Single.just(1).map {
                if (mapOfCoins.size == 0) {
                    updateMaps(getCachedData())
                }
                ArrayList(mapOfCoins.values)
            }.map { it.sortByDescending { it.priority }; it }
        }
    }

    private fun updateMaps(it: CoinsAndExchanges) {
        for (c in it.coins) {
            mapOfCoins[c.symbol.toUpperCase()] = c
        }
        for (e in it.exchanges) {
            mapOfExchanges[e.symbol.toUpperCase()] = e
        }
    }

    private fun getCachedData(): CoinsAndExchanges {
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
                val cNe = getCachedData()
                updateMaps(cNe)
                ArrayList(cNe.exchanges)
            }
        } else {
            return Single.just(1).map {
                if (mapOfCoins.size == 0) {
                    updateMaps(getCachedData())
                }
                ArrayList(mapOfExchanges.values)
            }.map { it.sortByDescending { it.priority }; it }
        }
    }

    override fun getBtcInrRates() = api.getBtcInrRate(remoteConfig.getString(BTC_INR_API))

    override fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse> {
        return api.getBestRates(coin, amount, ignoreFees)
                .map { it ->
                    getExchanges().blockingGet()
                    val list = ArrayList<BestCoin>()
                    it.values.mapTo(list) { it.copy(imgUrl = mapOfExchanges[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(values = list, imgUrl = mapOfCoins[it.symbol.toUpperCase()]?.imgUrl)
                    response
                }
    }

    override fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse> {
        return api.getBestCoin(exchange, amount, ignoreFees)
                .map { it ->

                    getCoins().blockingGet() //updates Map
                    val list = ArrayList<BestCoin>()
                    it.coins.mapTo(list) { it.copy(imgUrl = mapOfCoins[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(coins = list, imgUrl = mapOfExchanges[it.exchangeSymbol.toUpperCase()]?.imgUrl)
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

}