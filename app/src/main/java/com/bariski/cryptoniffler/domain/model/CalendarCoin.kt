package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class CalendarCoin(@Json(name = "name") val name: String, @Json(name = "symbol") val symbol: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarCoin> {
        override fun createFromParcel(parcel: Parcel): CalendarCoin {
            return CalendarCoin(parcel)
        }

        override fun newArray(size: Int): Array<CalendarCoin?> {
            return arrayOfNulls(size)
        }
    }
}