package com.bariski.cryptoniffler.data.api

import com.bariski.cryptoniffler.data.api.models.BestCoinResponse
import com.bariski.cryptoniffler.data.api.models.BestExchangeResponse
import com.bariski.cryptoniffler.domain.model.CoinRate
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CryptoNifflerApi {

    @GET
    fun getBtcInrRate(@Url url: String): Single<List<CoinRate>>

    @GET("/getBestCoin")
    fun getBestCoin(@Query("exchange") exchange: String, @Query("amount") amount: Long, @Query("ignoreFees") ignoreFees: Boolean): Single<BestCoinResponse>

    @GET("/getBestRates")
    fun getBestRates(@Query("coin") coin: String?, @Query("amount") amount: Long, @Query("ignoreFees") ignoreFees: Boolean): Single<BestExchangeResponse>
}