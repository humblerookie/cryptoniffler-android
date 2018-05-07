package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable

interface ArbitragePresentable : Parcelable {

    fun getLaunchCoin(): String
    fun getLaunchExchange(): String
}