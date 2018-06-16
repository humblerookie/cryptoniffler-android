package com.bariski.cryptoniffler.presentation.common

import android.app.Fragment
import android.os.Bundle

open class BaseFragment : Fragment(), BaseView {
    override fun getMessage(resourceId: Int): String {
        return getString(resourceId)
    }

    override fun getScreenShot() = (activity as BaseActivity).screenShot


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    fun isAlive(): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }


}