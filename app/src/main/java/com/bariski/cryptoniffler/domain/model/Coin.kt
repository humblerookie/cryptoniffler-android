package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json

data class Coin(@Json(name = "id") val id: String,
                @Json(name = "imageUrl") val imgUrl: String?,
                @Json(name = "name") val name: String,
                @Json(name = "symbol") val symbol: String,
                @Json(name = "priority") val priority: Int
) : GridItem {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun getItemImage() = imgUrl
    override fun getItemName() = name
    override fun getItemCode() = symbol
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgUrl)
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeInt(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }

}