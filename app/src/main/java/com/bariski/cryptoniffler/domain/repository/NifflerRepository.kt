package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.domain.model.*
import io.reactivex.Single

interface NifflerRepository : StaticContentRepository {
    fun getCoins(): Single<ArrayList<Coin>>
    fun getExchanges(): Single<ArrayList<Exchange>>
    fun getArbitrage(src: Set<FilterItem>, dest: Set<FilterItem>, sourceInternational: Set<FilterItem>, destInternational: Set<FilterItem>, intraExchange: Set<FilterItem>): Single<Arbitrage>
    fun getBtcInrRates(): Single<List<CoinRate>>
    fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse>
    fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse>
    fun fetchLatestConfig()
    fun hasDrawerBeenShown(): Boolean
    fun setDrawerShown(b: Boolean)
    fun hasArbDialogBeenShown(): Boolean
    fun setArbDialogBeenShown(b: Boolean)

    fun getFiltersList(): Single<ArbitrageFilter>?
    fun setFiltersList(filter: ArbitrageFilter)
    fun getSourceList(): Set<ArbitrageExchange>
    fun setSourceList(filter: Set<ArbitrageExchange>)
    fun getDestList(): Set<ArbitrageExchange>
    fun setDestList(filter: Set<ArbitrageExchange>)
    fun isArbFilterTutorialShown(): Boolean
    fun setArbFilterTutorialShown(b: Boolean)
    fun isRateNShareShown(): Boolean
    fun setRateNShareShown(b: Boolean)
    fun getArbitrageUsedCount(): Long
    fun setArbitrageUsedCount(b: Long)
    fun getArbitrageMode(): Int
    fun setArbitrageMode(type: Int)
    fun isDefaultLocaleSetOnce(): Boolean
    fun setDefaultLocaleOnce(b: Boolean)
}