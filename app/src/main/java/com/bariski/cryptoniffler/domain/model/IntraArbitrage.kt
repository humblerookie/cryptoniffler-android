package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class IntraArbitrage(@Json(name = "sell") val sell: String,
                          @Json(name = "buy") val buy: String,
                          @Json(name = "fees") val fees: List<String>,
                          @Json(name = "coin") val coin: String,
                          @Json(name = "src") val sourceMarket: String,
                          @Json(name = "exchange") val exchange: String,
                          @Json(name = "dest") val destinationMarket: String,
                          @Json(name = "profit") val profitPercentage: Float,
                          @Json(name = "coinImageUrl") val coinImageUrl: String?
) : ArbitragePresentable {
    override fun getLaunchCoin() = coin
    override fun getLaunchExchange() = exchange
    override fun getFeeDetails(): List<String> = fees
    override fun getType() = 1
}