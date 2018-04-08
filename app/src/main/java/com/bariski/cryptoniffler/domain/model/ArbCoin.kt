package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ArbCoin(@Json(name = "name") val name: String, @Json(name = "imageUrl") val imageUrl: String, @Json(name = "symbol") val symbol: String) : Parcelable