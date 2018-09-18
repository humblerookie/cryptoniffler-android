package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class AppIdentifier(@ColumnInfo(name = "android") @Json(name = "android") val android: String?) : Parcelable