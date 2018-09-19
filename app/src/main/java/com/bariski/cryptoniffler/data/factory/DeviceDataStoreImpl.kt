package com.bariski.cryptoniffler.data.factory

import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.domain.repository.DeviceDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DeviceDataStoreImpl @Inject constructor(private val keyValue: KeyValueStore) : DeviceDataStore {

    private val FCM_TOKEN = "fcm_token"
    private val INSTANCE_ID = "instance_id"
    private val DEVICE_TOKEN = "device_token"
    private val APP_VERSION = "app_version"
    private val IS_DEVICE_REGISTERED = "is_device_registered"
    private val IS_PERMISSION_RATIONALE_SHOWN = "is_permission_rationale_shown"

    override fun getFcmToken(): String? {
        return keyValue.getString(FCM_TOKEN)
    }

    override fun isDeviceRegistered(): Boolean {
        return keyValue.getBoolean(IS_DEVICE_REGISTERED)
    }

    override fun getAnonymousToken(): String? {
        return keyValue.getString(DEVICE_TOKEN)
    }

    override fun storeDeviceToken(value: String): Boolean {
        return keyValue.storeString(DEVICE_TOKEN, value)
    }

    override fun getInstanceId(): String? {
        return keyValue.getString(INSTANCE_ID)
    }

    override fun storeDeviceRegistered(value: Boolean): Boolean {
        return keyValue.storeBoolean(IS_DEVICE_REGISTERED, value)
    }

    override fun hasPermissionRationaleShown(permission: String) = keyValue.getBoolean(IS_PERMISSION_RATIONALE_SHOWN + permission)

    override fun setPermissionRationaleShown(permission: String, b: Boolean) {
        keyValue.storeBoolean(IS_PERMISSION_RATIONALE_SHOWN + permission, b)
    }

    override fun getLastAppVersion(): Int {
        return keyValue.getInt(APP_VERSION)
    }

    override fun setLastAppVersion(version:Int) {
         keyValue.storeInt(APP_VERSION,version)
    }


}