package com.bariski.cryptoniffler.domain.repository

interface DeviceDataStore {

    fun getFcmToken(): String?

    fun getInstanceId(): String?

    fun isDeviceRegistered(): Boolean

    fun storeDeviceRegistered(value: Boolean): Boolean

    fun getAnonymousToken(): String?

    fun storeDeviceToken(value: String): Boolean

    fun hasPermissionRationaleShown(permission: String): Boolean

    fun setPermissionRationaleShown(permission: String, b: Boolean)
}