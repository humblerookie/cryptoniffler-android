package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface ItemDetailPresenter : BasePresenter<CoinDetailView> {

    fun onSortClicked(type: Int)
    fun onRetry()
    fun loadData(booleanExtra: Boolean, coin: String?, exchange: String?, amount: Long)
}