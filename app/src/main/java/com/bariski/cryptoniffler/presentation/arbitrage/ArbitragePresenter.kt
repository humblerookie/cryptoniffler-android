package com.bariski.cryptoniffler.presentation.arbitrage

import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface ArbitragePresenter : BasePresenter<ArbitrageView> {
    fun onRetry()
}