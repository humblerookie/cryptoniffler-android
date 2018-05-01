package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class DirectArbitrage(@Json(name = "sell") val sell: Float,
                           @Json(name = "buy") val buy: Float,
                           @Json(name = "amount") val amount: Float,
                           @Json(name = "seed") val seed: Float,
                           @Json(name = "fees") val fees: Float,
                           @Json(name = "profit") val profit: Float,
                           @Json(name = "withdrawalFees") val withdrawalFees: Float?,
                           @Json(name = "coin") val coin: ArbCoin,
                           @Json(name = "from") val from: DirectArbitrageItem,
                           @Json(name = "to") val to: DirectArbitrageItem,
                           var roundOff: Boolean = true
) : Parcelable