package com.bariski.cryptoniffler.data.injection

import android.content.Context
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.analytics.AnalyticsImpl
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides

@Module
class DeviceModule {

    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    @Provides
    fun provideAnalyticsConfig(context: Context): Analytics {
        return AnalyticsImpl(context)
    }

}