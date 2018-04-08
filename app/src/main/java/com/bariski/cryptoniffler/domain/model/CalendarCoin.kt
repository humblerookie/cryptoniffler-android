package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class CalendarCoin(@Json(name = "name") val name: String, @Json(name = "symbol") val symbol: String, @Json(name = "id") val id: String) : FilterItem {
    override fun getDisplayTitle() = name

    override fun getIdentifier() = id

}