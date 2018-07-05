package com.bariski.cryptoniffler.inject.data

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.domain.model.*
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import io.reactivex.Single

class NifflerRepositoryMockImpl : NifflerRepository {

    var isDrawerMockShown: Boolean = false
    var isArbDialogShown: Boolean = false
    var isArbTutShown: Boolean = false
    var isRateShareShown: Boolean = false
    var isDefaultLocaleSetOnceMock: Boolean = false
    var isInternationalArbitrag: Boolean = false
    var arbUsedCount: Long = 0
    var filter: ArbitrageFilter? = null
    var sourceSet: Set<ArbitrageExchange> = HashSet()
    var destSet: Set<ArbitrageExchange> = HashSet()

    override fun getCoins(): Single<ArrayList<Coin>> {
        return Single.just(ArrayList())
    }

    override fun getExchanges(): Single<ArrayList<Exchange>> {
        return Single.just(ArrayList())
    }

    override fun getArbitrage(src: Set<FilterItem>, dest: Set<FilterItem>, sourceInternational: Set<FilterItem>, destInternational: Set<FilterItem>): Single<Arbitrage> {
        return Single.just(Arbitrage(10000, ArrayList(), ArrayList(), ArrayList(), ArbitrageFilter(ArrayList(), ArrayList())))
    }

    override fun getBtcInrRates(): Single<List<CoinRate>> {
        return Single.just(ArrayList())
    }

    override fun getBestRates(coin: String?, amount: Long, ignoreFees: Boolean): Single<BestExchangeResponse> {
        return Single.just(BestExchangeResponse("Bitbns", "BITBNS", null, ArrayList()))
    }

    override fun getBestCoin(exchange: String, amount: Long, ignoreFees: Boolean): Single<BestCoinResponse> {
        return Single.just(BestCoinResponse("Koinex", "KOINEX", null, ArrayList()))
    }

    override fun fetchLatestConfig() {

    }

    override fun hasDrawerBeenShown(): Boolean {
        return isDrawerMockShown
    }

    override fun setDrawerShown(b: Boolean) {
        isDrawerMockShown = b
    }

    override fun hasArbDialogBeenShown(): Boolean {
        return isArbDialogShown
    }

    override fun setArbDialogBeenShown(b: Boolean) {
        isArbDialogShown = b
    }

    override fun getFiltersList(): Single<ArbitrageFilter>? {
        return Single.just(filter)
    }

    override fun setFiltersList(filter: ArbitrageFilter) {
        this.filter = filter
    }

    override fun getSourceList(): Set<ArbitrageExchange> {
        return this.sourceSet
    }

    override fun setSourceList(filter: Set<ArbitrageExchange>) {
        this.sourceSet = filter
    }

    override fun getDestList(): Set<ArbitrageExchange> {
        return this.destSet
    }

    override fun setDestList(filter: Set<ArbitrageExchange>) {
        this.destSet = filter
    }

    override fun isArbFilterTutorialShown(): Boolean {
        return isArbTutShown
    }

    override fun setArbFilterTutorialShown(b: Boolean) {
        isArbTutShown = b
    }

    override fun isRateNShareShown(): Boolean {
        return isRateShareShown
    }

    override fun setRateNShareShown(b: Boolean) {
        isRateShareShown = b
    }


    override fun getArbitrageUsedCount(): Long {
        return arbUsedCount
    }

    override fun setArbitrageUsedCount(b: Long) {
        arbUsedCount = b
    }

    override fun isInternationalArbitrage(): Boolean {
        return isInternationalArbitrag
    }

    override fun setInternationalArbitrage(b: Boolean) {
        isInternationalArbitrag = b
    }


    override fun isDefaultLocaleSetOnce(): Boolean {
        return isDefaultLocaleSetOnceMock
    }

    override fun setDefaultLocaleOnce(b: Boolean) {
        isDefaultLocaleSetOnceMock = b
    }

    override fun getStaticInfo(): Single<Info> {
        return Single.just(Info("This is", "Nothing"))
    }
}