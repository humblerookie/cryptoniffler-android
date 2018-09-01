package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.CoinDetailActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector


@PerActivity
@Subcomponent(modules = [DeviceModule::class, CoinDetailModule::class])
interface CoinDetailComponent : AndroidInjector<CoinDetailActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<CoinDetailActivity>()
}