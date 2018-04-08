package com.bariski.cryptoniffler.data.api.models

import com.squareup.moshi.Json

data class AuthTokenResponse(@Json(name = "access_token") val token: String)