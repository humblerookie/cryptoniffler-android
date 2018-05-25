package com.bariski.cryptoniffler.data.api.models

import android.support.annotation.Keep
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.squareup.moshi.Json

@Keep
data class CoinsAndExchanges(@Json(name = "exchanges") val exchanges: List<Exchange>,
                             @Json(name = "coins") val coins: List<Coin>)