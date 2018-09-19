package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize @SuppressLint("ParcelCreator")
data class Info(val exchangeInfo: String, val upcomingFeatures: String, val privacyPolicyUrl:String) : Parcelable