package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Info(val exchangeInfo: String, val upcomingFeatures: String) : Parcelable