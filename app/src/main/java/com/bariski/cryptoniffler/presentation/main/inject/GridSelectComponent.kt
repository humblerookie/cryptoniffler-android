package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.data.injection.NetworkModule
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.GridSelectFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector


@PerActivity
@Subcomponent(modules = arrayOf(DeviceModule::class, NetworkModule::class))
interface GridSelectComponent : AndroidInjector<GridSelectFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<GridSelectFragment>()
}