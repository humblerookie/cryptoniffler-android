package com.bariski.cryptoniffler.data.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtilImpl(val context: Context) : NetworkUtil {

    override fun online(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo != null && manager.activeNetworkInfo.isConnected
    }



}