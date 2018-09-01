package com.bariski.cryptoniffler.presentation.calendar.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitrageFragment
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector


@PerFragment
@Subcomponent(modules = [DeviceModule::class, ArbitrageModule::class])
interface ArbitrageComponent : AndroidInjector<ArbitrageFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<ArbitrageFragment>()
}