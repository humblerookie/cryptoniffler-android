package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class DirectArbitrage(@Json(name = "sell") val sell: Float,
                           @Json(name = "buy") val buy: Float,
                           @Json(name = "amount") val amount: Float,
                           @Json(name = "seed") val seed: Float,
                           @Json(name = "fees") val fees: Float,
                           @Json(name = "profit") val profit: Float,
                           @Json(name = "coin") val coin: ArbCoin,
                           @Json(name = "from") val from: DirectArbitrageItem,
                           @Json(name = "to") val to: DirectArbitrageItem,
                           @Json(name = "feesSplit") val feeSplit: List<String>,
                           @Json(name = "breakEven") val breakEven: String,
                           @Json(name = "currency") val currency: String,
                           @Json(name = "transferTime") val transferTime: Float?

) : ArbitragePresentable {
    override fun getType() = 1
    override fun getFeeDetails() = feeSplit
    override fun getLaunchCoin() = coin.symbol
    override fun getLaunchExchange() = from.symbol

}