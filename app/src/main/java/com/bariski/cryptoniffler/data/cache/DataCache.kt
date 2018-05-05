package com.bariski.cryptoniffler.data.cache

import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import java.util.*

interface DataCache {

    fun clearAll()
    fun saveCoins(coins: HashMap<String, Coin>)
    fun getCoins(): HashMap<String, Coin>
    fun saveExchanges(exchanges: HashMap<String, Exchange>)
    fun getExchanges(): HashMap<String, Exchange>
}