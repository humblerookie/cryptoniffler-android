package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ArbitrageFilter(@Json(name = "exchanges") val exchanges: List<ArbitrageExchange>,
                           @Json(name = "internationalExchanges") val internationalExchanges: List<ArbitrageExchange>) : Parcelable