package com.bariski.cryptoniffler.presentation.arbitrage

import android.content.Intent
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.ArbitrageExchange
import com.bariski.cryptoniffler.domain.model.ArbitragePresentable
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.presentation.common.BaseView

interface ArbitrageView : BaseView {
    fun toggleProgress(visible: Boolean)
    fun setData(arbitrage: Arbitrage, mode: Int)
    fun toggleError(message: String?)
    fun showInfo()
    fun showFilters(mode: Int,src: List<ArbitrageExchange>, srcSelect: Set<FilterItem>, destSelect: Set<FilterItem>)
    fun showMessage(message: String)
    fun showFilterTutorial()
    fun showRateDialog()
    fun navigateToApp(intent: Intent)
    fun launchUrl(url: String)
    fun showFeesDialog(arbitrage: ArbitragePresentable)
}