package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface InfoPresenter : BasePresenter<InfoView> {

    fun onDonateClicked(id: Int)
}