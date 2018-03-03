package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.domain.model.Event
import io.reactivex.Single

interface EventsRepository {

    fun getCoins(): Single<List<String>>

    fun getEvents(coins: String?, categories: String?, startDate: String?, endDate: String?, page: Int, max: Int): Single<List<Event>>

    fun getCategories(): Single<List<String>>

}