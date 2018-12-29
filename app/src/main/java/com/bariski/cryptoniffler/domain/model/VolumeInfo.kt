package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VolumeInfo(@Json(name = "binance") val binance: List<VolumeItem>) : Parcelable

@Parcelize
data class VolumeItem(@Json(name = "symbol") val symbol: String, @Json(name = "volumes") val volumes: List<Double>) : Parcelable