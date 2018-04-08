package com.bariski.cryptoniffler.domain.util

import com.bariski.cryptoniffler.BuildConfig

const val VERSION = BuildConfig.VERSION_CODE
const val PLATFORM = "android"


const val SUCCESS = 1
const val ERROR_NETWORK = 0
const val ERROR_UNREACHABLE = -1
const val ERROR_EXPIRED_TOKEN = -10
const val ERROR_INVALID_REQUEST = -2
const val ERROR_SERVER = -3
const val ERROR_UNKNOWN = -4
const val ERROR_INVALID_CREDENTIALS = -5
const val ERROR_USER_EXISTS = -6
const val ERROR_INVALID_EMAIL = -7
const val ERROR_USER_ABSENT = -8
const val ERROR_RESPONSE_INVALID = -9

const val COINS = "coins"
const val EXCHANGES = "exchanges"
const val COIN = "coin"
const val KEY_TOKEN = "access_token"
const val EXCHANGE = "exchange"
const val BTC_INR_API = "btcInrUrl"
const val BASE_URL = "apiUrl"
const val EVENTS_BASE_URL = "eventsBaseUrl"
const val EVENT_CLIENT = "event_client"
const val EVENT_SECRET = "event_secret"
const val EVENT_SUFFIX_URL = "/v1/events"
const val TOKEN_SUFFIX_URL = "/oauth/v2/token"
const val COIN_SUFFIX_URL = "/v1/coins"
const val CATEGORIES_SUFFIX_URL = "/v1/categories"

class Key {
    companion object {
        val SCREEN = "screen"
        val IS_BUY = "isBuy"
        val COIN = "coin"
        val IS_SUCCESS = "isSuccess"
    }
}


class Screen {
    companion object {
        val AMOUNT = "amount"
        val MAIN = "main"
        val PICK_COIN = "pick_coin"
        val PICK_EXCHANGE = "pick_exchange"
        val SEARCH_COIN = "search_coin"
        val ITEM_DETAIL = "item_detail"

    }
}

class Event {
    companion object {
        val SCREEN_VIEW = "screen_viewed"
        val INFO_CLICK = "info_clicked"
        val SHARE_CLICK = "share_click"
        val DONATE_CLICK = "donate_click"
        val COPIED_ADDRESS = "copied_address"
        val ITEM_CLICKED = "item_clicked"
        val PROCEED = "proceed_detail"
        val BTC_VAL = "btc_val"
        val INCLUDE_FEE = "include_fee"
    }
}
