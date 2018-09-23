package com.bariski.cryptoniffler.domain.util

import com.bugsnag.android.Bugsnag
import timber.log.Timber

class LogTree : Timber.DebugTree() {
    var sendToRemoteLogger = true

    override fun createStackElementTag(element: StackTraceElement): String {
        return super.createStackElementTag(element) + "-" + element.lineNumber
    }

    override fun log(priority: Int, t: Throwable?) {
        if (sendToRemoteLogger) {
            t?.let { Bugsnag.notify(it) }
        }
        super.log(priority, t)
    }

    companion object {

        val MAX_LENGTH = 150
    }


}