package com.bariski.cryptoniffler.presentation.common.utils

import android.content.Intent

interface DeviceInfo {

    fun hasAppInstalled(packageIdentifier: String): Boolean

    fun getLaunchIntent(packageIdentifier: String): Intent
    fun getWidth(): Int
    fun getHeight(): Int
}