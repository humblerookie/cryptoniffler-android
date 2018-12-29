package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.domain.model.VolumeInfo
import com.bariski.cryptoniffler.presentation.common.BaseView

interface VolumeView : BaseView {

    fun displayInfo(info: VolumeInfo)

}