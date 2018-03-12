package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@PerFragment
@Subcomponent(modules = [DeviceModule::class, BaseFragmentModule::class])
interface BaseFragmentComponent : AndroidInjector<BaseInjectFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<BaseInjectFragment>()
}