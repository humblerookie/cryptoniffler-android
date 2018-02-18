package com.bariski.cryptoniffler.domain.model

import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.squareup.moshi.Json

data class Exchange(@Json(name = "name") val name: String,
                    @Json(name = "code") val code: String,
                    @Json(name = "fees") val fees: String,
                    @Json(name = "imgUrl") val imgUrl: String?,
                    @Json(name = "coins") val coins: List<String>,
                    @Json(name = "inrWithdrawalsActive") val inrWithdrawalsActive: String) : GridItem {
    override fun getItemImage() = imgUrl
    override fun getItemName() = name
    override fun getItemCode() = code
}