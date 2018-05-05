package com.bariski.cryptoniffler.presentation.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class DeviceInfoImpl(val context: Context) : DeviceInfo {
    override fun getLaunchIntent(packageIdentifier: String): Intent {
        return context.packageManager.getLaunchIntentForPackage(packageIdentifier)
    }

    override fun hasAppInstalled(packageIdentifier: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageIdentifier, 0) != null && context.packageManager.getApplicationInfo(packageIdentifier, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: NullPointerException) {
            false
        }

    }
}