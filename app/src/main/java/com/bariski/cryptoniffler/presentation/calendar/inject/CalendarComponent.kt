package com.bariski.cryptoniffler.presentation.calendar.inject

import com.bariski.cryptoniffler.data.injection.DeviceModule
import com.bariski.cryptoniffler.presentation.calendar.CalendarFragment
import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector


@PerActivity
@Subcomponent(modules = arrayOf(DeviceModule::class, CalendarModule::class))
interface CalendarComponent : AndroidInjector<CalendarFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<CalendarFragment>()
}