package com.bariski.cryptoniffler.domain.injection

import android.app.Application
import com.bariski.cryptoniffler.data.injection.*
import com.bariski.cryptoniffler.domain.common.SchedulerModule
import com.bariski.cryptoniffler.presentation.CryptNifflerApplication
import com.bariski.cryptoniffler.presentation.common.BaseActivity
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.common.notification.FcmMessageListenerService
import com.bariski.cryptoniffler.presentation.injection.ActivityBindingModule
import com.bariski.cryptoniffler.presentation.injection.FragmentBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class,
        DataStoreModule::class,
        DeviceModule::class,
        NetworkModule::class,
        SchedulerModule::class,
        CoinModule::class,
        EventModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class))

interface AppComponent {

    fun inject(baseActivity: BaseActivity)

    fun inject(messageService: FcmMessageListenerService)

    fun inject(baseFragment: BaseInjectFragment)

    fun inject(application: CryptNifflerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}