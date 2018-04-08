package com.bariski.cryptoniffler.presentation.arbitrage

import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.presentation.common.BaseView

interface ArbitrageView : BaseView {
    fun toggleProgress(visible: Boolean)
    fun setData(arbitrage: Arbitrage)
    fun toggleError(message: String?)
    fun showInfoDialog()
}