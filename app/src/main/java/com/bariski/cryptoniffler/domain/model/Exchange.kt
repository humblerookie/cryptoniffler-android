package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json

data class Exchange(@Json(name = "name") val name: String,
                    @Json(name = "symbol") val symbol: String,
                    @Json(name = "fees") val fees: String,
                    @Json(name = "priority") var priority: Int = 0,
                    @Json(name = "imageUrl") val imgUrl: String?,
                    @Json(name = "coins") val coins: List<String>,
                    @Json(name = "inrWithdrawalsActive") val inrWithdrawalsActive: String) : GridItem {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString())

    override fun getItemImage() = imgUrl
    override fun getItemName() = name
    override fun getItemCode() = symbol
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(symbol)
        parcel.writeString(fees)
        parcel.writeInt(priority)
        parcel.writeString(imgUrl)
        parcel.writeStringList(coins)
        parcel.writeString(inrWithdrawalsActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exchange> {
        override fun createFromParcel(parcel: Parcel): Exchange {
            return Exchange(parcel)
        }

        override fun newArray(size: Int): Array<Exchange?> {
            return arrayOfNulls(size)
        }
    }
}