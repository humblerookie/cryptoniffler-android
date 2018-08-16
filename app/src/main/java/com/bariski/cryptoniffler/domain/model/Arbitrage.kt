package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Arbitrage(@Json(name = "amount") val amount: Int,
                     @Json(name = "direct") val direct: List<DirectArbitrage>,
                     @Json(name = "triangle") val triangle: List<TriangleArbitrage>,
                     @Json(name = "btcArbitrage") val btcArbitrage: List<DirectArbitrage>,
                     @Json(name = "intra") val intraExchange: List<IntraArbitrage>,
                     @Json(name = "filters") val filters: ArbitrageFilter) : Parcelable