package com.bariski.cryptoniffler.presentation.injection

import com.bariski.cryptoniffler.presentation.calendar.CalendarFragment
import com.bariski.cryptoniffler.presentation.calendar.inject.CalendarModule
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import com.bariski.cryptoniffler.presentation.main.GridSelectFragment
import com.bariski.cryptoniffler.presentation.main.inject.BaseFragmentModule
import com.bariski.cryptoniffler.presentation.main.inject.GridSelectModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [CalendarModule::class])
    abstract fun calendarFragment(): CalendarFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [GridSelectModule::class])
    abstract fun gridSelectFragment(): GridSelectFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun baseInjectFragment(): BaseInjectFragment

}