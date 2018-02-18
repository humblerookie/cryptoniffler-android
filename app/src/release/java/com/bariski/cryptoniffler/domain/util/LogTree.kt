package com.bariski.cryptoniffler.domain.util

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import timber.log.Timber

class LogTree : Timber.DebugTree() {

    var sendToRemoteLogger = false

    override fun createStackElementTag(element: StackTraceElement): String {
        return super.createStackElementTag(element) + "-" + element.lineNumber
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            if (message.length < MAX_LENGTH) {
                assertAndLog(priority, tag, message, t)
            } else {
                var i = 0
                val length = message.length
                while (i < length) {
                    var newLineIndex = message.indexOf("\n", i)
                    newLineIndex = if (newLineIndex != -1) newLineIndex else length
                    do {
                        val end = Math.min(newLineIndex, i + MAX_LENGTH)
                        val part = message.substring(i, end)
                        assertAndLog(priority, tag, part, t)
                        i = end
                    } while (i < newLineIndex)
                    i++

                }
            }
        }
    }

    override fun log(priority: Int, t: Throwable?) {
        if (sendToRemoteLogger) {
            FirebaseCrash.report(t)
        }
        super.log(priority, t)
    }

    private fun assertAndLog(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ASSERT) {
            super.wtf(tag, message)
        } else {
            Log.println(priority, tag, message)
        }
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {

        return priority == Log.ERROR || priority == Log.WARN

    }

    companion object {
        val MAX_LENGTH = 150
    }

}
