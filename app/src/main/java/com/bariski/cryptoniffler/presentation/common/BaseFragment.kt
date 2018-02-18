package com.bariski.cryptoniffler.presentation.common

import android.app.Fragment

open class BaseFragment : Fragment() {
    fun isAlive(): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }
}