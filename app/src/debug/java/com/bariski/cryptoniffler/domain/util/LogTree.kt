package com.bariski.cryptoniffler.domain.util

import com.google.firebase.crash.FirebaseCrash
import timber.log.Timber

class LogTree : Timber.DebugTree() {
    var sendToRemoteLogger = true

    override fun createStackElementTag(element: StackTraceElement): String {
        return super.createStackElementTag(element) + "-" + element.lineNumber
    }

    override fun log(priority: Int, t: Throwable?) {
        if (sendToRemoteLogger) {
            FirebaseCrash.report(t)
        }
        super.log(priority, t)
    }

    companion object {

        val MAX_LENGTH = 150
    }


}