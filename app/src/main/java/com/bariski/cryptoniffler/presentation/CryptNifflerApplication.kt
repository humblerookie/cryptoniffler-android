package com.bariski.cryptoniffler.presentation

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.data.cache.DataCache
import com.bariski.cryptoniffler.domain.injection.AppComponent
import com.bariski.cryptoniffler.domain.injection.DaggerAppComponent
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.util.LogTree
import com.bariski.cryptoniffler.presentation.common.notification.NotificationUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.tspoon.traceur.Traceur
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject


class CryptNifflerApplication : MultiDexApplication(), HasActivityInjector {


    lateinit var appComponent: AppComponent

    @Inject
    lateinit var cache: DataCache

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build())
        }
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
                .application(this)
                .build()
        appComponent.inject(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(NotificationUtils.ARBITRAGE_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.ARBITRAGE_DOMESTIC_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.ARBITRAGE_INTERNATIONAL_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.NEWS_ID)
        FirebaseMessaging.getInstance().subscribeToTopic(NotificationUtils.ICO_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(NotificationUtils.ARBITRAGE_DOMESTIC_ID)
            if (channel == null) {
                NotificationUtils(this).createChannels()
            }
        }
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


    override fun onLowMemory() {
        super.onLowMemory()
        cache.clearAll()
        imageLoader.clearCache()
    }
}
