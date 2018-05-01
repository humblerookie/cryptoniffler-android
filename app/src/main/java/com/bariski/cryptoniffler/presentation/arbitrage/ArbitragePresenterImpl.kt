package com.bariski.cryptoniffler.presentation.arbitrage

import android.os.Bundle
import android.util.Log
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.ArbitrageFilter
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.Screen
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

class ArbitragePresenterImpl(val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics) : ArbitragePresenter {


    var arbitrage: Arbitrage? = null
    var disposable = CompositeDisposable()
    var view: WeakReference<ArbitrageView> = WeakReference<ArbitrageView>(null)
    var isRequestInProgress: Boolean = false
    var selectedSrc: Set<FilterItem> = HashSet()
    var selectedDest: Set<FilterItem> = HashSet()

    override fun onRetry() {
        fetchData(null)
    }

    override fun initView(v: ArbitrageView, savedState: Bundle?, args: Bundle?) {
        this.view = WeakReference(v)
        if (savedState?.containsKey("data") != null) {
            selectedSrc = HashSet(savedState.getParcelableArrayList("selectedSrc"))
            selectedDest = HashSet(savedState.getParcelableArrayList("selectedDest"))
            fetchData(savedState.getParcelable("data"))
        } else {
            analytics.sendScreenView(Screen.ARBITRAGE)
            fetchData(null)
        }

        if (!repository.hasArbDialogBeenShown()) {
            repository.setArbDialogBeenShown(true)
            view.get()?.showInfo()
        }

    }

    override fun onFilterClicked() {
        if (isRequestInProgress) {
            view.get()?.apply {
                showMessage(getMessage(R.string.error_common_request_progress_wait))
            }

        }  else if(isModeInternational()){
            view.get()?.apply {
                showMessage(getMessage(R.string.error_arbitrage_international_no_filter))
            }
        }else {
            if (arbitrage != null) {
                view.get()?.apply {
                    showFilters(arbitrage!!.filters.exchanges, selectedSrc, selectedDest)
                }
            } else {
                repository.getFiltersList()?.apply {
                    subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(onSuccess = { data: ArbitrageFilter? ->
                                view.get()?.apply {
                                    data?.let { showFilters(it.exchanges, selectedSrc, selectedDest) }
                                }
                            }, onError = {
                                Log.e("Arbitrage", it.toString())
                            })
                }

            }
        }
    }

    override fun onFilterApply(selectedFrom: Set<FilterItem>, selectedTo: Set<FilterItem>) {
        (selectedSrc as HashSet).addAll(selectedFrom)
        (selectedDest as HashSet).addAll(selectedTo)
        fetchData(null)
    }

    override fun onFilterClear() {
        (selectedSrc as HashSet).clear()
        (selectedDest as HashSet).clear()
        fetchData(null)
    }

    private fun fetchData(dat: Arbitrage?) {
        if (!isRequestInProgress) {
            isRequestInProgress = true

            var observable = repository
                    .getArbitrage(selectedSrc, selectedDest)
            view.get()?.let {
                it.toggleProgress(true)
                it.toggleError(null)
            }
            if (dat != null) {
                observable = Single.just(dat)
            }
            disposable.add(
                    observable
                            .subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(onSuccess = { data ->
                                arbitrage = data
                                if (dat == null) {
                                    if (!repository.isRateNShareShown()) {
                                        var used = repository.getArbitrageUsedCount()
                                        if (++used < 0) {
                                            used = 1
                                        }
                                        repository.setArbitrageUsedCount(used)
                                        var maxProfit = 0.0f
                                        arbitrage?.let {
                                            if (it.triangle.isNotEmpty()) {
                                                maxProfit = it.triangle[0].profit
                                            }
                                        }
                                        if (used % 10L == 0L || maxProfit > 15) {
                                            view.get()?.let {
                                                analytics.sendScreenView(Screen.RATE_REVIEW)
                                                it.showRateDialog()
                                            }
                                        }
                                    }
                                    repository.setFiltersList(data.filters)
                                }
                                view.get()?.let {
                                    arbitrage?.triangle?.let { list ->
                                        if (!repository.isArbFilterTutorialShown() && list.isNotEmpty()) {
                                            repository.setArbFilterTutorialShown(true)
                                            it.showFilterTutorial()
                                        }
                                    }
                                    it.toggleProgress(false)
                                    it.setData(data, repository.isInternationalArbitrage())
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

    override fun onButtonClicked(id: Int) {
        analytics.logRnREvent(when (id) {
            R.id.review -> "review"
            else -> "rate"
        })
        repository.setRateNShareShown(true)
    }

    override fun releaseView() {
        disposable.clear()
    }

    override fun onRefresh() {

    }

    override fun onModeChanged(isOn: Boolean) {
        repository.setInternationalArbitrage(isOn)
        analytics.logModeChanged(isOn)
        view.get()?.apply { arbitrage?.let { setData(it, repository.isInternationalArbitrage()) } }
    }

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putParcelable("data", arbitrage)
            putParcelableArrayList("selectedSrc", ArrayList(selectedSrc))
            putParcelableArrayList("selectedDest", ArrayList(selectedDest))
        }
    }

    override fun isModeInternational() = repository.isInternationalArbitrage()

}