package com.bariski.cryptoniffler.analytics

import android.os.Bundle
import com.bariski.cryptoniffler.domain.model.ArbitragePresentable

interface Analytics {

    fun logEvent(name: String, values: Bundle)

    fun logShareEvent()

    fun logQueryEvent(query: String)

    fun logInfoClick(isBuy: Boolean)

    fun sendScreenView(name: String)

    fun logBtcValEvent(b: Boolean, error: String?)
    fun itemDetailEvent(success: Boolean, error: String?, type: String, value: String, amount: Long, ignoreFees: Boolean)
    fun logIncludeFeeChanged(b: Boolean)
    fun logRnREvent(event: String)
    fun logModeChanged(type: Int)
    fun logNavigatedToExchange(arbitrage: ArbitragePresentable)
}