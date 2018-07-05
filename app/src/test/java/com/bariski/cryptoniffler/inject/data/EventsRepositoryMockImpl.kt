package com.bariski.cryptoniffler.inject.data

import com.bariski.cryptoniffler.data.api.models.AuthTokenResponse
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import io.reactivex.Single

class EventsRepositoryMockImpl : EventsRepository {

    private var isFilterTutorialShownMock: Boolean = false

    override fun getCoins(): Single<List<CalendarCoin>> {
        return Single.just(ArrayList())
    }

    override fun getAndSaveToken(): Single<AuthTokenResponse> {
        return Single.just(AuthTokenResponse("token"))
    }

    override fun getEvents(coins: String?, categories: String?, startDate: String?, endDate: String?, page: Int, max: Int): Single<ArrayList<Event>> {
        return Single.just(ArrayList())
    }

    override fun isAuthenticated(): Boolean {
        return false
    }

    override fun getCategories(): Single<List<CalendarCategory>> {
        return Single.just(ArrayList())
    }

    override fun isFilterTutorialShown(): Boolean {
        return isFilterTutorialShownMock
    }

    override fun setFilterTutorialShown(b: Boolean) {
        isFilterTutorialShownMock = b
    }
}