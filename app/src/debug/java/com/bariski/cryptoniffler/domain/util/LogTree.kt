package com.bariski.cryptoniffler.domain.util

import com.crashlytics.android.Crashlytics
import timber.log.Timber

class LogTree : Timber.DebugTree() {
    var sendToRemoteLogger = true

    override fun createStackElementTag(element: StackTraceElement): String {
        return super.createStackElementTag(element) + "-" + element.lineNumber
    }

    override fun log(priority: Int, t: Throwable?) {
        if (sendToRemoteLogger) {
            Crashlytics.logException(t)
        }
        super.log(priority, t)
    }

    companion object {

        val MAX_LENGTH = 150
    }


}