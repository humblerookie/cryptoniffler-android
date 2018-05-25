package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep

@Keep
interface ArbitragePresentable : Parcelable {

    fun getLaunchCoin(): String
    fun getLaunchExchange(): String
    fun getFeeDetails(): List<String>
    fun getType():Int
}