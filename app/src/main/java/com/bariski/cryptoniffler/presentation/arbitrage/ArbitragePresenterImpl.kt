package com.bariski.cryptoniffler.presentation.arbitrage

import android.os.Bundle
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

class ArbitragePresenterImpl(val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics) : ArbitragePresenter {

    var arbitrage: Arbitrage? = null
    var disposable = CompositeDisposable()
    var view: WeakReference<ArbitrageView> = WeakReference<ArbitrageView>(null)
    var isRequestInProgress: Boolean = false
    override fun onRetry() {
        fetchData(null)
    }

    override fun initView(v: ArbitrageView, savedState: Bundle?, args: Bundle?) {
        this.view = WeakReference(v)
        if (savedState?.containsKey("data") != null) {
            fetchData(savedState.getParcelable("data"))
        } else {
            fetchData(null)
        }

        if (!repository.hasArbDialogBeenShown()) {
            repository.setArbDialogBeenShown(true)
            view.get()?.showInfoDialog()
        }

    }

    private fun fetchData(data: Arbitrage?) {
        if (!isRequestInProgress) {
            isRequestInProgress = true

            var observable = repository
                    .getArbitrage()
            view.get()?.let {
                it.toggleProgress(true)
                it.toggleError(null)
            }
            if (data != null) {
                observable = Single.just(data)
            }
            disposable.add(
                    observable
                            .subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(onSuccess = { data ->
                                arbitrage = data
                                view.get()?.let {
                                    it.toggleProgress(false)
                                    it.setData(data)
                                }
                                isRequestInProgress = false
                            }, onError = {
                                view.get()?.apply {
                                    toggleProgress(false)
                                    toggleError(getMessage(R.string.error_common_something_wrong))
                                }
                                isRequestInProgress = false
                            })
            )
        }
    }

    override fun releaseView() {
        disposable.clear()
    }

    override fun onRefresh() {

    }

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putParcelable("data", arbitrage)
        }
    }

}