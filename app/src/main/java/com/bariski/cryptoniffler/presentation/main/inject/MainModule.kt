package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.DeviceDataStore
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.MainPresenter
import com.bariski.cryptoniffler.presentation.main.MainPresenterImpl
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @PerActivity
    fun providesPresenter(repository: NifflerRepository, eventsRepository: EventsRepository, deviceDataStore: DeviceDataStore, adapter: GridItemAdapter, schedulers: Schedulers, analytics: Analytics, imageLoader: ImageLoader): MainPresenter {
        return MainPresenterImpl(repository, eventsRepository, deviceDataStore, adapter, schedulers, analytics, imageLoader)
    }

}