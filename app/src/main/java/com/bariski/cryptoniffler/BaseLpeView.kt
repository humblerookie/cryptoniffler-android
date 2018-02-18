package com.bariski.cryptoniffler

import com.bariski.cryptoniffler.presentation.common.BaseView

interface BaseLpeView : BaseView {

    fun toggleProgress(visible: Boolean)
    fun toggleError(error: String?)

}