package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json

data class Coin(@Json(name = "Id") val id: String,
                @Json(name = "ImageUrl") val imgUrl: String?,
                @Json(name = "Name") val name: String,
                @Json(name = "CoinName") val coinName: String,
                @Json(name = "FullName") val fullName: String,
                @Json(name = "Symbol") val symbol: String,
                @Json(name = "Algorithm") val algorithm: String,
                @Json(name = "FullyPremined") val fullyPremined: String,
                @Json(name = "TotalCoinSupply") val totalCoinSupply: String,
                @Json(name = "PreMinedValue") val preMinedValue: String,
                @Json(name = "Url") val url: String,
                @Json(name = "TotalCoinsFreeFloat") val totalCoinsFreeFloat: String,
                @Json(name = "ProofType") val proofType: String,
                @Json(name = "buyRate") val buyPrice: Float?,
                @Json(name = "sellRate") val sellPrice: Float?,
                @Json(name = "buyPremium") val buyPremium: Float?,
                @Json(name = "sellPremium") val sellPremium: Float?,
                @Json(name = "priority") val priority: Int
) : GridItem, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readInt())

    override fun getItemImage() = imgUrl
    override fun getItemName() = coinName
    override fun getItemCode() = symbol
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgUrl)
        parcel.writeString(name)
        parcel.writeString(coinName)
        parcel.writeString(fullName)
        parcel.writeString(symbol)
        parcel.writeString(algorithm)
        parcel.writeString(fullyPremined)
        parcel.writeString(totalCoinSupply)
        parcel.writeString(preMinedValue)
        parcel.writeString(url)
        parcel.writeString(totalCoinsFreeFloat)
        parcel.writeString(proofType)
        parcel.writeValue(buyPrice)
        parcel.writeValue(sellPrice)
        parcel.writeValue(buyPremium)
        parcel.writeValue(sellPremium)
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