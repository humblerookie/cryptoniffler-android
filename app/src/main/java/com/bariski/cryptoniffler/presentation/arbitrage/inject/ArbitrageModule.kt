package com.bariski.cryptoniffler.presentation.calendar.inject

import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenterImpl
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import dagger.Module
import dagger.Provides

@Module
@PerFragment
class ArbitrageModule {
    @Provides
    @PerFragment
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, analytics: Analytics): ArbitragePresenter {
        return ArbitragePresenterImpl(repository, schedulers, analytics)
    }
}