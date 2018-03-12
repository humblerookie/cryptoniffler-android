package com.bariski.cryptoniffler.presentation.common

import android.app.Activity
import com.bariski.cryptoniffler.domain.repository.AndroidDataStore
import dagger.android.AndroidInjection
import dagger.android.DaggerFragment
import javax.inject.Inject

open class BaseInjectFragment : DaggerFragment() {

    @Inject
    lateinit var storage: AndroidDataStore


    fun isAlive(): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

    override fun onAttach(activity: Activity?) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            AndroidInjection.inject(this)
        }
        super.onAttach(activity)
    }
}