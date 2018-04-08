package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.data.api.models.AuthTokenResponse
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Event
import io.reactivex.Single

interface EventsRepository {

    fun getCoins(): Single<List<CalendarCoin>>
    fun getAndSaveToken(): Single<AuthTokenResponse>
    fun getEvents(coins: String?, categories: String?, startDate: String?, endDate: String?, page: Int, max: Int): Single<ArrayList<Event>>
    fun isAuthenticated():Boolean
    fun getCategories(): Single<List<CalendarCategory>>
    fun isFilterTutorialShown(): Boolean
    fun setFilterTutorialShown(b: Boolean)

}