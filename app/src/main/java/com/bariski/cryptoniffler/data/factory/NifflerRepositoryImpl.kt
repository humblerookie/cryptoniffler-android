package com.bariski.cryptoniffler.data.factory

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import com.bariski.cryptoniffler.data.api.CryptoNifflerApi
import com.bariski.cryptoniffler.data.api.models.BestCoin
import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.BTC_INR_API
import com.bariski.cryptoniffler.domain.util.COINS
import com.bariski.cryptoniffler.domain.util.EXCHANGES
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single

//http://jsoneditoronline.org/?id=296639de32b80b0cf4aa48aeeb343d43
//http://jsoneditoronline.org/?id=ba02e037b8700a258d14f50f6b462dfc

class NifflerRepositoryImpl(val context: Context, private val api: CryptoNifflerApi, private val remoteConfig: FirebaseRemoteConfig, private val scheduler: Schedulers) : NifflerRepository {

    private var mapOfCoins = HashMap<String, Coin>()
    private val mapOfExchanges = HashMap<String, Exchange>()


    private fun initExchanges() {
        val s = remoteConfig.getString(EXCHANGES).replace(":?qt", "\"")
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val jsonAdapter = moshi.adapter<List<Exchange>>(Types.newParameterizedType(List::class.java, Exchange::class.java))
        val exchanges: ArrayList<Exchange> = ArrayList(jsonAdapter.fromJson(s)!!)
        for (i in exchanges) {
            mapOfExchanges[i.code.toUpperCase()] = i
        }

    }

    private fun initCoins() {
        val s = remoteConfig.getString(COINS).replace(":?qt", "\"")
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        val jsonAdapter = moshi.adapter<List<Coin>>(Types.newParameterizedType(List::class.java, Coin::class.java))
        Log.d("CoinName: ", s)
        val listOfCoins = jsonAdapter.fromJson(s)
        listOfCoins?.let {
            for (c in it) {
                mapOfCoins[c.symbol] = c
            }
        }

    }

    override fun fetchLatestConfig() {
        initIfNeeded()
        var cacheExpiration = if (remoteConfig.info.configSettings.isDeveloperModeEnabled) 0 else 2 * AlarmManager.INTERVAL_HOUR
        remoteConfig.fetch(cacheExpiration).addOnCompleteListener({
            if (it.isSuccessful) {
                initExchanges()
                initCoins()
                remoteConfig.activateFetched()
            }
        })
    }

    private fun initIfNeeded() {
        if (mapOfCoins.size == 0) {
            initCoins()
            initExchanges()
        }
    }

    override fun getCoins(): Single<ArrayList<Coin>> {
        initIfNeeded()
        return Single.just(ArrayList(mapOfCoins.values)).map { it.sortByDescending { it.priority }; it }
    }


    override fun getExchanges(): Single<ArrayList<Exchange>> {
        initIfNeeded()
        return Single.just(ArrayList(mapOfExchanges.values)).map { it.sortByDescending { it.priority }; it }
    }

    override fun getBtcInrRates() = api.getBtcInrRate(remoteConfig.getString(BTC_INR_API))

    override fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse> {
        initIfNeeded()
        return api.getBestRates(coin, amount, ignoreFees)
                .map { it ->
                    val list = ArrayList<BestCoin>()
                    it.values.mapTo(list) { it.copy(imgUrl = mapOfExchanges[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(values = list, imgUrl = mapOfCoins[it.symbol.toUpperCase()]?.imgUrl)
                    response
                }
    }

    override fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse> {
        initIfNeeded()
        return api.getBestCoin(exchange, amount, ignoreFees)
                .map { it ->
                    val list = ArrayList<BestCoin>()
                    it.coins.mapTo(list) { it.copy(imgUrl = mapOfCoins[it.symbol.toUpperCase()]?.imgUrl) }
                    val response = it.copy(coins = list, imgUrl = mapOfExchanges[it.exchangeSymbol.toUpperCase()]?.imgUrl)
                    response
                }
    }

}