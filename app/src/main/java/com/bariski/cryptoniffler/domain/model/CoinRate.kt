package com.bariski.cryptoniffler.domain.model

import com.squareup.moshi.Json

data class CoinRate(@Json(name = "id") val id: String,
                    @Json(name = "symbol") val symbol: String,
                    @Json(name = "price_inr") val price: Float)