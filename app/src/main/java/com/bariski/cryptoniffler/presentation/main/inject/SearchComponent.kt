package com.bariski.cryptoniffler.presentation.main.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.SearchActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@PerActivity
@Subcomponent(modules = arrayOf(DeviceModule::class, SearchModule::class))
interface SearchComponent : AndroidInjector<SearchActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SearchActivity>()
}