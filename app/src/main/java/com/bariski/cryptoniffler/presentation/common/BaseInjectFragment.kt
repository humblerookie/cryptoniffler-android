package com.bariski.cryptoniffler.presentation.common

import com.bariski.cryptoniffler.domain.repository.AndroidDataStore
import dagger.android.DaggerFragment
import javax.inject.Inject

open class BaseInjectFragment : DaggerFragment() {

    @Inject
    lateinit var storage: AndroidDataStore


    fun isAlive(): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }
}