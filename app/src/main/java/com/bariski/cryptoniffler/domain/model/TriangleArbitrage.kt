package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class TriangleArbitrage(@Json(name = "amount") val amount: Float,
                             @Json(name = "seed") val seed: Float,
                             @Json(name = "profit") val profit: Float,
                             @Json(name = "fees") val fees: Float,
                             @Json(name = "actions") val actions: List<TriangleAction>,
                             @Json(name = "source") val source: String,
                             @Json(name = "exchanges") val exchanges: List<String>
) : ArbitragePresentable {
    override fun getLaunchCoin() = source
    override fun getLaunchExchange() = exchanges[0]
}