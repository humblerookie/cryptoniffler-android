package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.presentation.common.BaseView
import com.bariski.cryptoniffler.presentation.main.model.GridDetailWrapper

interface CoinDetailView : BaseView {

    fun toggleProgress(visible: Boolean)
    fun toggleError(visible: Boolean, error: String?)
    fun setData(response:GridDetailWrapper)

}