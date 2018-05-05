package com.bariski.cryptoniffler.data.cache

import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import java.util.*

class MemCache : DataCache {

    private var mapOfCoins = HashMap<String, Coin>()
    private val mapOfExchanges = HashMap<String, Exchange>()

    override fun clearAll() {
        mapOfCoins.clear()
        mapOfExchanges.clear()
    }

    override fun saveCoins(coins: HashMap<String, Coin>) {
        mapOfCoins.putAll(coins)
    }

    override fun getCoins() = mapOfCoins


    override fun saveExchanges(exchanges: HashMap<String, Exchange>) {
        mapOfExchanges.putAll(exchanges)
    }

    override fun getExchanges() = mapOfExchanges

}