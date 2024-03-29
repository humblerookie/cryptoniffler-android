package com.bariski.cryptoniffler.presentation.calendar.inject

import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenterImpl
import com.bariski.cryptoniffler.presentation.common.utils.DeviceInfo
import com.bariski.cryptoniffler.presentation.common.utils.DeviceInfoImpl
import com.bariski.cryptoniffler.presentation.injection.scopes.PerFragment
import dagger.Module
import dagger.Provides

@Module
class ArbitrageModule {
    @Provides
    @PerFragment
    fun providesPresenter(repository: NifflerRepository, schedulers: Schedulers, analytics: Analytics, deviceInfo: DeviceInfo): ArbitragePresenter {
        return ArbitragePresenterImpl(repository, schedulers, analytics, deviceInfo)
    }

    @Provides
    @PerFragment
    fun providesDeviceInfo(context: Context): DeviceInfo {
        return DeviceInfoImpl(context)
    }
}