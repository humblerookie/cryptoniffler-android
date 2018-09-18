package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@SuppressLint("ParcelCreator")
data class TriangleAction(@Json(name = "rightImage") val rightImage: String?,
                          @Json(name = "leftImage") val leftImage: String?,
                          @Json(name = "mainImage") val mainImage: String?,
                          @Json(name = "text") val summary: String
) : Parcelable