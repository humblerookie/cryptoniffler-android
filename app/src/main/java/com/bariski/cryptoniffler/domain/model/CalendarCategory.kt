package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class CalendarCategory(@Json(name = "name") val name: String, @Json(name = "id") val id: Int) : FilterItem {
    override fun getDisplayTitle() = name

    override fun getIdentifier() = id.toString()

}