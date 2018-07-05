package com.bariski.cryptoniffler.inject.data

import com.bariski.cryptoniffler.domain.repository.DeviceDataStore

class DeviceDataStoreMockImpl : DeviceDataStore {

    var fcmTokenMock: String? = null
    var instanceIdMock: String? = null
    var isDeviceRegisteredMock: Boolean = false
    var permissionMap = HashMap<String, Boolean>()
    override fun getFcmToken() = fcmTokenMock

    override fun getInstanceId(): String? = instanceIdMock

    override fun isDeviceRegistered(): Boolean {
        return isDeviceRegisteredMock
    }

    override fun storeDeviceRegistered(value: Boolean): Boolean {
        isDeviceRegisteredMock = value
        return true
    }

    override fun getAnonymousToken(): String? {
        return ""
    }

    override fun storeDeviceToken(value: String): Boolean {
        return true
    }

    override fun hasPermissionRationaleShown(permission: String): Boolean {
        return permissionMap.containsKey(permission) && permissionMap[permission]!!
    }

    override fun setPermissionRationaleShown(permission: String, b: Boolean) {
        permissionMap[permission]
    }
}