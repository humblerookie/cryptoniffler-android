package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.models.AmountInput
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter
import io.reactivex.Observable
import io.reactivex.Observer

interface MainPresenter : BasePresenter<MainView>, GridItemAdapter.OnItemClickListener {


    fun onDrawerItemSelected(id: Int)
    fun onItemClicked(id: Int)
    fun onAmountScreenRefresh()
    fun onAmountScreenCreated(textChanges: Observable<CharSequence>, observer: Observer<AmountInput>)
    fun onAmountScreenDestroyed()
    fun onBackPressed()
    fun onNext()
    fun onSearchClicked()
    fun onSearchResult(coin: Coin?)
    fun onIncludeFeeChanged(b: Boolean)
    fun infoClicked()
    fun onMainViewResumed()
}