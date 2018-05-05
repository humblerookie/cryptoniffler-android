package com.bariski.cryptoniffler.data.api

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.data.api.models.CoinsAndExchanges
import com.bariski.cryptoniffler.data.utils.HEADER_VERSION_2
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.CoinRate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface CryptoNifflerApi {

    @GET
    fun getBtcInrRate(@Url url: String): Single<List<CoinRate>>

    @GET("/getBestCoin")
    fun getBestCoin(@Query("exchange") exchange: String, @Query("amount") amount: Long, @Query("ignoreFees") ignoreFees: Boolean): Single<BestCoinResponse>

    @GET("/getBestRates")
    fun getBestRates(@Query("coin") coin: String?, @Query("amount") amount: Long, @Query("ignoreFees") ignoreFees: Boolean): Single<BestExchangeResponse>

    @GET("/getCoinsAndExchanges")
    @Headers(HEADER_VERSION_2)
    fun getCoinsAndExchanges(): Single<CoinsAndExchanges>

    @GET("/arbitrage")
    @Headers(HEADER_VERSION_2)
    fun getArbitrage(@Query("src") source: String?, @Query("dest") dest: String?): Single<Arbitrage>

}