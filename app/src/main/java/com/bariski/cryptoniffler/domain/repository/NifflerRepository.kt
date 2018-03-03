package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.CoinRate
import com.bariski.cryptoniffler.domain.model.Exchange
import io.reactivex.Single

interface NifflerRepository {
    fun getCoins(): Single<ArrayList<Coin>>
    fun getExchanges(): Single<ArrayList<Exchange>>
    fun getBtcInrRates(): Single<List<CoinRate>>
    fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse>
    fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse>
    fun fetchLatestConfig()
    fun hasDrawerBeenShown(): Boolean
    fun setDrawerShown(b: Boolean)
}