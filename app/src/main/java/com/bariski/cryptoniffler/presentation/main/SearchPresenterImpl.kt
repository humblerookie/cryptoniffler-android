package com.bariski.cryptoniffler.presentation.main

import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.Screen
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class SearchPresenterImpl(val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics) : SearchPresenter {

    private val disposable = CompositeDisposable()

    override fun onClick(item: GridItem) {
        view.get()?.finishAndSendData(item as Coin)
    }

    lateinit var view: WeakReference<SearchView>

    override fun initView(searchView: SearchView) {
        view = WeakReference(searchView)
        analytics.sendScreenView(Screen.SEARCH_COIN)
        var input: String? = null
        disposable.add(searchView.getSearchObservable()
                .subscribeOn(schedulers.ui())
                .observeOn(schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .map {
                    val trimmedString = it.trim().toString()
                    input = trimmedString
                    val list = ArrayList<Coin>()
                    if (trimmedString.isNotEmpty()) {
                        analytics.logQueryEvent(trimmedString)
                        repository.getCoins().blockingGet().filterTo(list) { c -> c.coinName.toLowerCase().startsWith(trimmedString.toLowerCase()) || c.symbol.toLowerCase().startsWith(trimmedString.toLowerCase()) }

                    }
                    list

                }
                .observeOn(schedulers.ui())
                .subscribeBy(onNext = {
                    view.get()?.apply {
                        input?.apply {
                            if (isNotEmpty() && it.size == 0) {
                                toggleEmptyView(true)
                            } else {
                                toggleEmptyView(false)
                            }
                        }
                        showData(if (it.size == 0) null else it)
                    }
                }, onError = {

                }, onComplete = {}

                ))

    }

    override fun releaseView() {
        disposable.dispose()
    }

    override fun onRefresh() {
        repository.fetchLatestConfig()
    }


}