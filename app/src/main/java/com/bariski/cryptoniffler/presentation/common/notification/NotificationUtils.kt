package com.bariski.cryptoniffler.presentation.common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.bariski.cryptoniffler.BuildConfig

class NotificationUtils(base: Context) : ContextWrapper(base) {

    private val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels() {
        // create android channel
        val androidChannel = NotificationChannel(ARBITRAGE_ID,
                ARBITRAGE_NAME, NotificationManager.IMPORTANCE_HIGH)
        androidChannel.enableLights(true)
        androidChannel.enableVibration(true)
        androidChannel.lightColor = Color.GREEN
        androidChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(androidChannel)

        val newsChannel = NotificationChannel(NEWS_ID,
                NEWS_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        newsChannel.enableLights(true)
        newsChannel.enableVibration(true)
        newsChannel.lightColor = Color.BLUE
        newsChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(newsChannel)

        val icoChannel = NotificationChannel(ICO_ID,
                ICO_NAME, NotificationManager.IMPORTANCE_LOW)
        icoChannel.enableLights(true)
        icoChannel.enableVibration(true)
        icoChannel.lightColor = Color.CYAN
        icoChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(icoChannel)
    }

    companion object {
        private const val ANDROID_PREFIX = "android_"+ BuildConfig.BUILD_PREFIX
        const val ARBITRAGE_ID = ANDROID_PREFIX + "arbitrage"
        const val ARBITRAGE_NAME = "Arbitrage Updates"
        const val NEWS_ID = ANDROID_PREFIX + "news"
        const val NEWS_NAME = "Crypto News"
        const val ICO_ID = ANDROID_PREFIX + "ico"
        const val ICO_NAME = "ICO"
    }
}