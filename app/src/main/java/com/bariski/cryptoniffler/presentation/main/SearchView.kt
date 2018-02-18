package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.domain.model.Coin
import com.jakewharton.rxbinding2.InitialValueObservable

interface SearchView {

    fun toggleEmptyView(visible: Boolean)

    fun showData(data: List<Coin>?)

    fun getSearchObservable(): InitialValueObservable<CharSequence>

    fun finishAndSendData(coin: Coin)

}