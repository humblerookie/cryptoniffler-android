package com.bariski.cryptoniffler.data.factory

import android.app.Activity
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

class Logger @Inject constructor() {

    fun logException(throwable: Throwable) {
        Timber.e(throwable)
    }

    fun d(message: String, vararg args: Any) {
        Timber.d(message, args)
    }

    fun d(message: String) {
        Timber.d(message)
    }

    fun i(message: String, vararg args: Any) {
        Timber.i(message, args)
    }

    fun setBool(k: String, b: Boolean) {
        Crashlytics.setBool(k, b)
    }

    fun setString(s: String, v: String?) {
        Crashlytics.setString(s, v)
    }

    fun setLong(s: String, v: Long) {
        Crashlytics.setLong(s, v)
    }

    fun initLogger(activity: Activity) {
        if (!Fabric.isInitialized()) {
            Fabric.with(activity, Crashlytics())
        }
    }
}