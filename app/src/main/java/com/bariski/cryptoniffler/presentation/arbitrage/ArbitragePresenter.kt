package com.bariski.cryptoniffler.presentation.arbitrage

import com.bariski.cryptoniffler.domain.model.ArbitragePresentable
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.model.IntraArbitrage
import com.bariski.cryptoniffler.presentation.arbitrage.listeners.ArbitrageClickListener
import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface ArbitragePresenter : BasePresenter<ArbitrageView>, ArbitrageClickListener {
    fun onRetry()
    fun onFilterClicked()
    fun onFilterApply(selectedFrom: Set<FilterItem>, selectedTo: Set<FilterItem>)
    fun onFilterClear()
    fun onButtonClicked(id: Int)
    fun onModeChanged(mode: Int)
    fun isModeInternational(): Boolean
    fun isModeIntra(): Boolean
    fun isModeIndian(): Boolean
    fun onArbitrageConfirmed(arbitrage: ArbitragePresentable)
    fun onIntraArbitrageClicked(data: IntraArbitrage)
}