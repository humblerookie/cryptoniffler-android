package com.bariski.cryptoniffler.presentation.calendar.inject

import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.presentation.calendar.CalendarPresenter
import com.bariski.cryptoniffler.presentation.calendar.CalendarPresenterImpl
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import dagger.Module
import dagger.Provides

@Module
@PerFragment
class CalendarModule {
    @Provides
    @PerFragment
    fun providesPresenter(repository: EventsRepository, schedulers: Schedulers, analytics: Analytics): CalendarPresenter {
        return CalendarPresenterImpl(repository, schedulers, analytics)
    }
}