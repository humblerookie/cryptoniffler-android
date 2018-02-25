package com.bariski.cryptoniffler.presentation

import android.app.Activity
import android.support.multidex.MultiDexApplication
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.injection.AppComponent
import com.bariski.cryptoniffler.domain.injection.DaggerAppComponent
import com.bariski.cryptoniffler.domain.util.LogTree
import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.tspoon.traceur.Traceur
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject




class CryptNifflerApplication : MultiDexApplication(), HasActivityInjector {


    lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Traceur.enableLogging()
        }

        initLogger()
        FirebaseApp.initializeApp(this)
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        FirebaseRemoteConfig.getInstance().setConfigSettings(configSettings)
        FirebaseRemoteConfig.getInstance().setDefaults(R.xml.defaults)
        appComponent = DaggerAppComponent.builder()
                .application(this).build()
        appComponent.inject(this)


    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }


    private fun initLogger() {
        Timber.plant(LogTree())

    }

    companion object {
        lateinit var instance: CryptNifflerApplication

    }

}
