package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.Keep
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize @SuppressLint("ParcelCreator")
@Keep
@Entity
data class Exchange(
        @ColumnInfo(name = "name")
        @Json(name = "name") val name: String,
        @ColumnInfo(name = "symbol")
        @Json(name = "symbol") val symbol: String,
        @ColumnInfo(name = "fees")
        @Json(name = "fees") val fees: String,
        @ColumnInfo(name = "priority")
        @Json(name = "priority") var priority: Int = 0,
        @ColumnInfo(name = "imageUrl")
        @Json(name = "imageUrl") val imgUrl: String?,
        @ColumnInfo(name = "isHidden")
        @Json(name = "isHidden") val isHidden: Boolean?,
        @ColumnInfo(name = "tradeUrlPattern")
        @Json(name = "tradeUrlPattern") val tradeUrlPattern: String?,
        @Embedded
        @Json(name = "appIdentifier") val appIdentifier: AppIdentifier?,
        @ColumnInfo(name = "inrWithdrawalsActive")
        @Json(name = "inrWithdrawalsActive") val inrWithdrawalsActive: String) : GridItem {

    @PrimaryKey(autoGenerate = true)
    var uid: Long?=null

    override fun getItemImage() = imgUrl
    override fun getItemName() = name
    override fun getItemCode() = symbol

}