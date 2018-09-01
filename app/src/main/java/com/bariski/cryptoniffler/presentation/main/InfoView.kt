package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.presentation.common.BaseView

interface InfoView : BaseView {

    fun displayInfo(info: Info)

}