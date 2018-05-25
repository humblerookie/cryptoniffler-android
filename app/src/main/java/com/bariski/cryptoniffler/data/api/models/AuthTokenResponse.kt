package com.bariski.cryptoniffler.data.api.models

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AuthTokenResponse(@Json(name = "access_token") val token: String)