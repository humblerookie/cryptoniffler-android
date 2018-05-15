package com.bariski.cryptoniffler.domain.model

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ResponseWrapper(@Json(name = "code") val code: Int, @Json(name = "data") val data: Any? = null, @Json(name = "message") val message: String? = null) {
    companion object {
        val SUCCESS = com.bariski.cryptoniffler.domain.util.SUCCESS
        val ERROR_NETWORK = com.bariski.cryptoniffler.domain.util.ERROR_NETWORK
        val ERROR_UNREACHABLE = com.bariski.cryptoniffler.domain.util.ERROR_UNREACHABLE
        val ERROR_INVALID_REQUEST = com.bariski.cryptoniffler.domain.util.ERROR_INVALID_REQUEST
        val ERROR_SERVER = com.bariski.cryptoniffler.domain.util.ERROR_SERVER
        val ERROR_UNKNOWN = com.bariski.cryptoniffler.domain.util.ERROR_UNKNOWN
        val ERROR_INVALID_CREDENTIALS = com.bariski.cryptoniffler.domain.util.ERROR_INVALID_CREDENTIALS
        val ERROR_USER_EXISTS = com.bariski.cryptoniffler.domain.util.ERROR_USER_EXISTS
        val ERROR_INVALID_EMAIL = com.bariski.cryptoniffler.domain.util.ERROR_INVALID_EMAIL
        val ERROR_USER_ABSENT = com.bariski.cryptoniffler.domain.util.ERROR_USER_ABSENT
        val ERROR_RESPONSE_INVALID = com.bariski.cryptoniffler.domain.util.ERROR_RESPONSE_INVALID
    }

}