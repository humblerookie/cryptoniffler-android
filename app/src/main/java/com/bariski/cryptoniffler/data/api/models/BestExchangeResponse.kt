package com.bariski.cryptoniffler.data.api.models

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class BestExchangeResponse(@Json(name = "name") val name: String,
                                @Json(name = "symbol") val symbol: String,
                                @Json(name = "imgUrl") val imgUrl: String?,
                                @Json(name = "exchanges") val values: List<BestCoin>
)