package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.InfoFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@PerActivity
@Subcomponent(modules = arrayOf(DeviceModule::class, InfoModule::class))
interface InfoComponent : AndroidInjector<InfoFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<InfoFragment>()
}