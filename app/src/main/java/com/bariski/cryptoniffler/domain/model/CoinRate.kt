package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class CoinRate(@Json(name = "id") val id: String,
                    @Json(name = "symbol") val symbol: String,
                    @Json(name = "price_inr") val price: Float)