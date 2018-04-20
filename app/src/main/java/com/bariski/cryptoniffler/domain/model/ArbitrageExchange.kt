package com.bariski.cryptoniffler.domain.model

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArbitrageExchange(@Json(name = "label") val name: String, @Json(name = "symbol") val id: String) : FilterItem {
    override fun getDisplayTitle() = name
    override fun getIdentifier() = id

    override fun toString(): String {
        return getIdentifier()
    }
}