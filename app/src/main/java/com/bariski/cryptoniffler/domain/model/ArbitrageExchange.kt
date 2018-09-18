package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class ArbitrageExchange(@Json(name = "label") val name: String, @Json(name = "symbol") val id: String, @Json(name = "mode") val modes: List<Int>?) : FilterItem {
    override fun getDisplayTitle() = name
    override fun getIdentifier() = id

    override fun toString(): String {
        return getIdentifier()
    }
}