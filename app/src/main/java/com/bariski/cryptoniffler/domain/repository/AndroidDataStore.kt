package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.data.storage.KeyValueStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidDataStore @Inject constructor(val keyStore: KeyValueStore) {

    private val PERMISSION_RATIONALE = "permission_rationale"


    fun isRationaleShown(permission: String): Boolean {
        return keyStore.getBoolean(PERMISSION_RATIONALE + permission)
    }

    fun setRationaleShown(permission: String): Boolean {
        return keyStore.storeBoolean(PERMISSION_RATIONALE + permission, true)
    }

}
