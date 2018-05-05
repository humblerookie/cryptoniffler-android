package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppIdentifier(@Json(name = "android") val android: String?) : Parcelable