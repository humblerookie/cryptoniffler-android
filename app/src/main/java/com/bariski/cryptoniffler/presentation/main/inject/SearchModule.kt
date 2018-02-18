package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.SearchPresenter
import com.bariski.cryptoniffler.presentation.main.SearchPresenterImpl
import dagger.Module
import dagger.Provides

@Module
@PerActivity
class SearchModule {
    @Provides
    @PerActivity
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, analytics: Analytics): SearchPresenter {
        return SearchPresenterImpl(repository, schedulers,analytics)
    }

}