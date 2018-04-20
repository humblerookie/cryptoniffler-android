package com.bariski.cryptoniffler.presentation.arbitrage

import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface ArbitragePresenter : BasePresenter<ArbitrageView> {
    fun onRetry()
    fun onFilterClicked()
    fun onFilterApply(selectedFrom: Set<FilterItem>, selectedTo: Set<FilterItem>)
    fun onFilterClear()
    fun onButtonClicked(id: Int)
}