package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.presentation.common.BasePresenter

interface VolumePresenter : BasePresenter<VolumeView> {
    fun fetchData()
}