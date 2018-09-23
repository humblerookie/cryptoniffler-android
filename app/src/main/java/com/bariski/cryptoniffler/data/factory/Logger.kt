package com.bariski.cryptoniffler.data.factory

import com.bugsnag.android.Bugsnag
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
        Bugsnag.leaveBreadcrumb("$k=$b")
    }

    fun setString(k: String, v: String?) {
        Bugsnag.leaveBreadcrumb("$k=$v")
    }

    fun setLong(k: String, v: Long) {
        Bugsnag.leaveBreadcrumb("$k=$v")
    }
}