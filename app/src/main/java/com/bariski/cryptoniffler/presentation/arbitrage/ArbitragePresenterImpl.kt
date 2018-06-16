package com.bariski.cryptoniffler.presentation.arbitrage

import android.os.Bundle
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.*
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.Screen
import com.bariski.cryptoniffler.presentation.common.utils.DeviceInfo
import com.bariski.cryptoniffler.presentation.common.utils.INDIAN_TIMEZONE
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

class ArbitragePresenterImpl(val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics, val deviceInfo: DeviceInfo) : ArbitragePresenter {


    var arbitrage: Arbitrage? = null
    var disposable = CompositeDisposable()
    private var view: WeakReference<ArbitrageView> = WeakReference<ArbitrageView>(null)
    var isRequestInProgress: Boolean = false
    var selectedSrc: Set<FilterItem> = HashSet()
    var selectedDest: Set<FilterItem> = HashSet()
    var selectedSrcInternational: Set<FilterItem> = HashSet()
    var selectedDestInternational: Set<FilterItem> = HashSet()
    val mapExchange = HashMap<String, Exchange>()

    val TAG = "ArbitragePresenter"

    override fun onRetry() {
        fetchData(null)
    }

    override fun initView(v: ArbitrageView, savedState: Bundle?, args: Bundle?) {
        this.view = WeakReference(v)
        if (!repository.isDefaultLocaleSetOnce()) {
            try {
                repository.setInternationalArbitrage(Calendar.getInstance().timeZone.id != INDIAN_TIMEZONE)
                repository.setDefaultLocaleOnce(true)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        if (savedState?.containsKey("data") != null) {
            selectedSrc = HashSet(savedState.getParcelableArrayList("selectedSrc"))
            selectedDest = HashSet(savedState.getParcelableArrayList("selectedDest"))
            selectedSrcInternational = HashSet(savedState.getParcelableArrayList("selectedSrcInternational"))
            selectedDestInternational = HashSet(savedState.getParcelableArrayList("selectedDestInternational"))
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

        } else {
            if (arbitrage != null) {
                view.get()?.apply {
                    arbitrage?.let {
                        if (isModeInternational()) {
                            showFilters(isModeInternational(), it.filters.internationalExchanges, selectedSrcInternational, selectedDestInternational)
                        } else {
                            showFilters(isModeInternational(), it.filters.exchanges, selectedSrc, selectedDest)
                        }
                    }
                }
            } else {
                repository.getFiltersList()?.apply {
                    subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(onSuccess = { data: ArbitrageFilter? ->
                                view.get()?.apply {
                                    data?.let {
                                        if (isModeInternational()) {
                                            showFilters(isModeInternational(), it.internationalExchanges, selectedSrcInternational, selectedDestInternational)
                                        } else {
                                            showFilters(isModeInternational(), it.exchanges, selectedSrc, selectedDest)
                                        }
                                    }
                                }
                            }, onError = { e ->
                                Timber.e(e)
                            })
                }

            }
        }
    }

    override fun onFilterApply(selectedFrom: Set<FilterItem>, selectedTo: Set<FilterItem>) {
        if (isModeInternational()) {
            (selectedSrcInternational as HashSet).addAll(selectedFrom)
            (selectedDestInternational as HashSet).addAll(selectedTo)
        } else {
            (selectedSrc as HashSet).addAll(selectedFrom)
            (selectedDest as HashSet).addAll(selectedTo)
        }

        fetchData(null)
    }

    override fun onFilterClear() {
        if (isModeInternational()) {
            (selectedSrcInternational as HashSet).clear()
            (selectedDestInternational as HashSet).clear()
        } else {
            (selectedSrc as HashSet).clear()
            (selectedDest as HashSet).clear()
        }
        fetchData(null)
    }

    private fun fetchData(dat: Arbitrage?) {
        if (!isRequestInProgress) {
            isRequestInProgress = true

            var observable = repository
                    .getArbitrage(selectedSrc, selectedDest, selectedSrcInternational, selectedDestInternational)
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
                                    arbitrage?.direct?.let { list ->
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

    override fun onDirectArbitrageClick(arbitrage: DirectArbitrage) {
        view.get()?.showFeesDialog(arbitrage)
    }

    private fun navigateToExchange(arbitrage: ArbitragePresentable) {
        if (mapExchange.size > 0 && mapExchange.containsKey(arbitrage.getLaunchExchange())) {
            mapExchange[arbitrage.getLaunchExchange()]?.let { ex ->
                if (ex.appIdentifier?.android != null && deviceInfo.hasAppInstalled(ex.appIdentifier.android)) {
                    view.get()?.navigateToApp(deviceInfo.getLaunchIntent(ex.appIdentifier.android))
                } else {
                    ex.tradeUrlPattern?.let {
                        view.get()?.launchUrl(String.format(it, arbitrage.getLaunchCoin()))
                    }
                }
            }

        } else {
            disposable.add(repository.getExchanges()
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribeBy(onSuccess = { data ->
                        data.forEach {
                            mapExchange[it.symbol] = it
                        }
                        mapExchange[arbitrage.getLaunchExchange()]?.let { ex ->
                            if (ex.appIdentifier?.android != null && deviceInfo.hasAppInstalled(ex.appIdentifier.android)) {
                                view.get()?.navigateToApp(deviceInfo.getLaunchIntent(ex.appIdentifier.android))
                            } else {
                                ex.tradeUrlPattern?.let {
                                    view.get()?.launchUrl(String.format(it, arbitrage.getLaunchCoin()))
                                }
                            }
                        }
                    }, onError = { e ->
                        Timber.e(e)
                    }))
        }


    }

    override fun onTriangleArbitrageClick(arbitrage: TriangleArbitrage) {
        view.get()?.showFeesDialog(arbitrage)
    }

    override fun onButtonClicked(id: Int) {
        analytics.logRnREvent(when (id) {
            R.id.review -> "review"
            else -> "share"
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
            putParcelableArrayList("selectedSrcInternational", ArrayList(selectedSrcInternational))
            putParcelableArrayList("selectedDestInternational", ArrayList(selectedDestInternational))
        }
    }

    override fun onArbitrageConfirmed(arbitrage: ArbitragePresentable) {
        analytics.logNavigatedToExchange(arbitrage)
        navigateToExchange(arbitrage)
    }


    override fun isModeInternational() = repository.isInternationalArbitrage()

}