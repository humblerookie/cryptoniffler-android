package com.bariski.cryptoniffler.presentation.common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
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
        manager.deleteNotificationChannel(ARBITRAGE_ID)
        if (manager.notificationChannelGroups.size == 0) {
            manager.createNotificationChannelGroup(NotificationChannelGroup(ARBITRAGE_GROUP_ID, ARBITRAGE_GROUP))
        }
        if (manager.getNotificationChannel(ARBITRAGE_INTERNATIONAL_ID) == null) {
            val arbitrageIntChanneld = NotificationChannel(ARBITRAGE_INTERNATIONAL_ID,
                    ARBITRAGE_INTERNATIONAL_NAME, NotificationManager.IMPORTANCE_HIGH)
            arbitrageIntChanneld.enableLights(true)
            arbitrageIntChanneld.enableVibration(true)
            arbitrageIntChanneld.lightColor = Color.GREEN
            arbitrageIntChanneld.group = ARBITRAGE_GROUP_ID
            arbitrageIntChanneld.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(arbitrageIntChanneld)
        }

        if (manager.getNotificationChannel(ARBITRAGE_DOMESTIC_ID) == null) {
            val arbitrageIndChanneld = NotificationChannel(ARBITRAGE_DOMESTIC_ID,
                    ARBITRAGE_DOMESTIC_NAME, NotificationManager.IMPORTANCE_HIGH)
            arbitrageIndChanneld.enableLights(true)
            arbitrageIndChanneld.enableVibration(true)
            arbitrageIndChanneld.lightColor = Color.GREEN
            arbitrageIndChanneld.group = ARBITRAGE_GROUP_ID
            arbitrageIndChanneld.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(arbitrageIndChanneld)

        }
        if (manager.getNotificationChannel(NEWS_ID) == null) {
            val newsChannel = NotificationChannel(NEWS_ID,
                    NEWS_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            newsChannel.enableLights(true)
            newsChannel.enableVibration(true)
            newsChannel.lightColor = Color.BLUE
            newsChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(newsChannel)
        }
        if (manager.getNotificationChannel(ICO_ID) == null) {
            val icoChannel = NotificationChannel(ICO_ID,
                    ICO_NAME, NotificationManager.IMPORTANCE_LOW)
            icoChannel.enableLights(true)
            icoChannel.enableVibration(true)
            icoChannel.lightColor = Color.CYAN
            icoChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(icoChannel)
        }
    }

    companion object {
        private const val ANDROID_PREFIX = "android_" + BuildConfig.BUILD_PREFIX
        const val ARBITRAGE_ID = ANDROID_PREFIX + "arbitrage"
        const val ARBITRAGE_GROUP = "Arbitrage"
        const val ARBITRAGE_GROUP_ID = ANDROID_PREFIX + "arbitrage_group"
        const val ARBITRAGE_DOMESTIC_ID = ANDROID_PREFIX + "ind_arbitrage"
        const val ARBITRAGE_DOMESTIC_NAME = "Indian"
        const val ARBITRAGE_INTERNATIONAL_ID = ANDROID_PREFIX + "int_arbitrage"
        const val ARBITRAGE_INTERNATIONAL_NAME = "International"
        const val NEWS_ID = ANDROID_PREFIX + "news"
        const val NEWS_NAME = "Crypto News"
        const val ICO_ID = ANDROID_PREFIX + "ico"
        const val ICO_NAME = "ICO"
    }
}