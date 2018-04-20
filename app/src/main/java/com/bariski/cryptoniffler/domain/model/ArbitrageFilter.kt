package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArbitrageFilter(@Json(name = "exchanges") val exchanges: List<ArbitrageExchange>) : Parcelable