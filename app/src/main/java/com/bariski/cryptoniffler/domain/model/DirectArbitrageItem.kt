package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
data class DirectArbitrageItem (@Json(name = "symbol")val symbol:String,
                                @Json(name = "name")val name:String,
                                @Json(name = "text")val summary:String,
                                @Json(name = "imageUrl")val imageUrl:String):Parcelable
