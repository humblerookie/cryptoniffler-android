package com.bariski.cryptoniffler.presentation.main.inject

import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.data.factory.Logger
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.ItemDetailPresenter
import com.bariski.cryptoniffler.presentation.main.ItemDetailPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class CoinDetailModule {

    @Provides
    @PerActivity
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, context: Context,analytics: Analytics,logger: Logger): ItemDetailPresenter {
        return ItemDetailPresenterImpl(repository, schedulers, context,analytics,logger)
    }

}