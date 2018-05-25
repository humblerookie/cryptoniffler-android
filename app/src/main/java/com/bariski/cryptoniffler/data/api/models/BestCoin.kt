package com.bariski.cryptoniffler.data.api.models

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class BestCoin(@Json(name = "name") val name: String,
                    @Json(name = "symbol") val symbol: String,
                    @Json(name = "marketRate") val marketRate: Float,
                    @Json(name = "buy") val buy: Float,
                    @Json(name = "sell") val sell: Float,
                    @Json(name = "buyEfficiency") val buyEfficiency: Float,
                    @Json(name = "sellEfficiency") val sellEfficiency: Float,
                    @Json(name = "imgUrl") val imgUrl: String?,
                    @Json(name = "coin") val coin: String?,
                    @Json(name = "exchange") val exchange: String?
)