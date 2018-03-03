package com.bariski.cryptoniffler.data.api

import com.bariski.cryptoniffler.domain.model.Event
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface EventsApi {

    @GET
    fun getEvents(@Url url: String): Single<List<Event>>

    @GET
    fun getCoins(@Url url: String): Single<List<String>>

    @GET
    fun getCategories(@Url url: String): Single<List<String>>
}