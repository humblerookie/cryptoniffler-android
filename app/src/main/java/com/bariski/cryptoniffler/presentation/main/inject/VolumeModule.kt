package com.bariski.cryptoniffler.presentation.main.inject

import android.content.ClipboardManager
import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import com.bariski.cryptoniffler.presentation.main.InfoPresenter
import com.bariski.cryptoniffler.presentation.main.InfoPresenterImpl
import com.bariski.cryptoniffler.presentation.main.VolumePresenter
import com.bariski.cryptoniffler.presentation.main.VolumePresenterImpl
import dagger.Module
import dagger.Provides

@Module
@PerActivity
class VolumeModule {
    @Provides
    @PerFragment
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, analytics: Analytics): VolumePresenter {
        return VolumePresenterImpl(repository, schedulers, analytics)
    }


}