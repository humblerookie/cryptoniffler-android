package com.bariski.cryptoniffler.presentation.common

import android.os.Bundle
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.util.*

open interface BasePresenter<T> {

    fun initView(view: T, savedState: Bundle?, args: Bundle?)

    fun releaseView()

    fun onRefresh()

    fun saveState(outState: Bundle?)

    companion object {
        val MESSAGE_MAP = hashMapOf(
                ERROR_UNREACHABLE to R.string.error_common_unreachable,
                ERROR_NETWORK to R.string.error_common_network,
                ERROR_INVALID_REQUEST to R.string.error_common_network,
                ERROR_SERVER to R.string.error_common_network,
                ERROR_UNKNOWN to R.string.error_common_something_wrong,
                ERROR_INVALID_CREDENTIALS to R.string.error_login_invalid_creds,
                ERROR_USER_EXISTS to R.string.error_login_invalid_creds,
                ERROR_USER_ABSENT to R.string.error_login_invalid_creds,
                ERROR_RESPONSE_INVALID to R.string.error_common_request_invalid
        )
    }
}

