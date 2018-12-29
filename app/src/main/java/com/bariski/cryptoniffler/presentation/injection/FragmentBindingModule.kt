package com.bariski.cryptoniffler.presentation.injection

import com.bariski.cryptoniffler.presentation.arbitrage.ArbitrageFragment
import com.bariski.cryptoniffler.presentation.calendar.CalendarFragment
import com.bariski.cryptoniffler.presentation.calendar.inject.ArbitrageModule
import com.bariski.cryptoniffler.presentation.calendar.inject.CalendarModule
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import com.bariski.cryptoniffler.presentation.main.GridSelectFragment
import com.bariski.cryptoniffler.presentation.main.InfoFragment
import com.bariski.cryptoniffler.presentation.main.VolumeMonitorFragment
import com.bariski.cryptoniffler.presentation.main.inject.BaseFragmentModule
import com.bariski.cryptoniffler.presentation.main.inject.GridSelectModule
import com.bariski.cryptoniffler.presentation.main.inject.InfoModule
import com.bariski.cryptoniffler.presentation.main.inject.VolumeModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [CalendarModule::class])
    abstract fun calendarFragment(): CalendarFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [ArbitrageModule::class])
    abstract fun arbitrageFragment(): ArbitrageFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [GridSelectModule::class])
    abstract fun gridSelectFragment(): GridSelectFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [InfoModule::class])
    abstract fun infoFragment(): InfoFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [VolumeModule::class])
    abstract fun volumeMonitor(): VolumeMonitorFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [BaseFragmentModule::class])
    abstract fun baseInjectFragment(): BaseInjectFragment

}