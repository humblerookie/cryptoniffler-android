package com.bariski.cryptoniffler.data.api.models

import com.squareup.moshi.Json

data class BestExchangeResponse(@Json(name = "name") val name: String,
                                @Json(name = "symbol") val symbol: String,
                                @Json(name = "imgUrl") val imgUrl: String?,
                                @Json(name = "exchanges") val values: List<BestCoin>
)