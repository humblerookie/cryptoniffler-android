package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class TriangleArbitrage(@Json(name = "amount") val amount: Float,
                             @Json(name = "seed") val seed: Float,
                             @Json(name = "profit") val profit: Float,
                             @Json(name = "fees") val fees: Float,
                             @Json(name = "actions") val actions: List<TriangleAction>
) : Parcelable