package com.bariski.cryptoniffler.analytics

import android.content.Context
import android.os.Bundle
import com.bariski.cryptoniffler.domain.util.Event
import com.bariski.cryptoniffler.domain.util.Key
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsImpl(context: Context) : Analytics {
    override fun logIncludeFeeChanged(b: Boolean) {
        val bundle = Bundle()
        bundle.putInt("includeFee", if (b) 1 else 0)
        analytics.logEvent(Event.INCLUDE_FEE, bundle)
    }

    override fun itemDetailEvent(success: Boolean, error: String?, type: String, value: String, amount: Long, ignoreFees: Boolean) {
        val bundle = Bundle()
        if (error != null) {
            bundle.putString("error", error)
        }
        bundle.putString("type", type)
        bundle.putString("value", value)
        bundle.putLong("amount", amount)
        bundle.putInt("success", if (success) 1 else 0)
        bundle.putInt("ignoreFees", if (ignoreFees) 1 else 0)
        analytics.logEvent(Event.BTC_VAL, bundle)
    }

    override fun logBtcValEvent(b: Boolean, error: String?) {
        val bundle = Bundle()
        if (error != null) {
            bundle.putString("error", error)
        }
        bundle.putInt("success", if (b) 1 else 0)
        analytics.logEvent(Event.BTC_VAL, bundle)
    }


    override fun logQueryEvent(query: String) {
        val bundle = Bundle()
        bundle.putString("query", query)
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    override fun logDonateEvent() {
        analytics.logEvent(Event.DONATE_CLICK, Bundle())
    }

    override fun logDonateCopiedEvent(coin: String) {
        val bundle = Bundle()
        bundle.putString(Key.COIN, coin)
        analytics.logEvent(Event.COPIED_ADDRESS, bundle)
    }

    override fun logInfoClick(isBuy: Boolean) {
        val bundle = Bundle()
        bundle.putInt(Key.IS_BUY, if (isBuy) 1 else 0)
        analytics.logEvent(Event.INFO_CLICK, bundle)
    }


    override fun sendScreenView(name: String) {
        val bundle = Bundle()
        bundle.putString(Key.SCREEN, name)
        analytics.logEvent(Event.SCREEN_VIEW, bundle)
    }

    override fun logShareEvent() {
        analytics.logEvent(Event.SHARE_CLICK, Bundle())
    }

    override fun logEvent(name: String, values: Bundle) {
        analytics.logEvent(name, values)
    }

    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

}