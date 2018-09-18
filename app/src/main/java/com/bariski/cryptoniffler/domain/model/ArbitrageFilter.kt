package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class ArbitrageFilter(@Json(name = "exchanges") val exchanges: List<ArbitrageExchange>) : Parcelable