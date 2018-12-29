package com.bariski.cryptoniffler.data.api

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.data.api.models.CoinsAndExchanges
import com.bariski.cryptoniffler.data.utils.HEADER_VERSION_2
import com.bariski.cryptoniffler.data.utils.HEADER_VERSION_3
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.CoinRate
import com.bariski.cryptoniffler.domain.model.VolumeInfo
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

    /***
     * Version 1: Had only domestic/Indian exchange filters
     * Version 2: This version had Indian and international filters
     * Version 3: This version has Indian, International and Intra filters
     */
    @GET("/arbitrage")
    @Headers(HEADER_VERSION_3)
    fun getArbitrage(@Query("src") source: String?, @Query("dest") dest: String?, @Query("srcInternational") sourceInternational: String?, @Query("destInternational") destInternational: String?, @Query("intraExchanges") intraExchanges: String?): Single<Arbitrage>


    @GET("/getVolumeData")
    fun getVolumeInfo(): Single<VolumeInfo>

}