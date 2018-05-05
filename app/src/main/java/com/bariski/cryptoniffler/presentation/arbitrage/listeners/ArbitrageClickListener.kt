package com.bariski.cryptoniffler.presentation.arbitrage.listeners

import com.bariski.cryptoniffler.domain.model.DirectArbitrage
import com.bariski.cryptoniffler.domain.model.TriangleArbitrage

interface ArbitrageClickListener {
    fun onDirectArbitrageClick(arbitrage: DirectArbitrage)
    fun onTriangleArbitrageClick(arbitrage: TriangleArbitrage)
}