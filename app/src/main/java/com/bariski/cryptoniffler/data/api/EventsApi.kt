package com.bariski.cryptoniffler.data.api

import com.bariski.cryptoniffler.data.api.models.AuthTokenResponse
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Event
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface EventsApi {

    @GET
    fun getEvents(@Url url: String): Single<List<Event>>

    @GET
    fun getCoins(@Url url: String): Single<List<CalendarCoin>>

    @GET
    fun getCategories(@Url url: String): Single<List<CalendarCategory>>

    @GET
    fun getToken(@Url url: String): Single<AuthTokenResponse>
}