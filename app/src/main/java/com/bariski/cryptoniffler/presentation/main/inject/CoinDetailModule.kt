package com.bariski.cryptoniffler.presentation.main.inject

import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.CoinDetailPresenter
import com.bariski.cryptoniffler.presentation.main.CoinDetailPresenterImpl
import dagger.Module
import dagger.Provides

@Module
@PerActivity
class CoinDetailModule {

    @Provides
    @PerActivity
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, context: Context,analytics: Analytics): CoinDetailPresenter {
        return CoinDetailPresenterImpl(repository, schedulers, context,analytics)
    }

}