package com.bariski.cryptoniffler.data.api.models

import com.squareup.moshi.Json

data class BestCoinResponse(@Json(name = "name") val exchangeName: String,
                            @Json(name = "symbol") val exchangeSymbol: String,
                            @Json(name = "imgUrl") var imgUrl: String?,
                            @Json(name = "coins") val coins: List<BestCoin>
)