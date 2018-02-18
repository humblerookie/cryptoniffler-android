package com.bariski.cryptoniffler.presentation.common.idlingresources

import android.support.test.espresso.IdlingResource
import java.util.*

class DelayIdlingResource(private val interval: Long) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private val timeStart = Calendar.getInstance().timeInMillis

    override fun getName(): String {
        return "DelayIdlingResource:" + interval
    }

    override fun isIdleNow(): Boolean {

        val isIdle = Calendar.getInstance().timeInMillis - timeStart >= interval

        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }
}