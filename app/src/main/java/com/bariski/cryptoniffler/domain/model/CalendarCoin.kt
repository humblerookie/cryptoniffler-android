package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize @SuppressLint("ParcelCreator")
@Entity
data class CalendarCoin(@ColumnInfo(name = "name")
                        @Json(name = "name") val name: String,
                        @ColumnInfo(name = "symbol")
                        @Json(name = "symbol") val symbol: String,
                        @ColumnInfo(name = "id")
                        @Json(name = "id") val id: String) : FilterItem {
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null

    override fun getDisplayTitle() = name

    override fun getIdentifier() = id

}