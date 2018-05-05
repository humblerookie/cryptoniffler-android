package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Exchange(@Json(name = "name") val name: String,
                    @Json(name = "symbol") val symbol: String,
                    @Json(name = "fees") val fees: String,
                    @Json(name = "priority") var priority: Int = 0,
                    @Json(name = "imageUrl") val imgUrl: String?,
                    @Json(name = "isHidden") val isHidden: Boolean?,
                    @Json(name = "coins") val coins: List<String>,
                    @Json(name = "tradeUrlPattern") val tradeUrlPattern: String?,
                    @Json(name = "appIdentifier") val appIdentifier: AppIdentifier?,
                    @Json(name = "inrWithdrawalsActive") val inrWithdrawalsActive: String) : GridItem {
    override fun getItemImage() = imgUrl
    override fun getItemName() = name
    override fun getItemCode() = symbol

}