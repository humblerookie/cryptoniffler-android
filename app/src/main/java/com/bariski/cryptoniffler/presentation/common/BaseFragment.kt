package com.bariski.cryptoniffler.presentation.common

import android.app.Fragment

open class BaseFragment : Fragment(), BaseView {
    override fun getMessage(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun getScreenShot() = (activity as BaseActivity).screenShot


    fun isAlive(): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }


}