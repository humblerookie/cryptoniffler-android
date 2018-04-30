package com.bariski.cryptoniffler.presentation.calendar

import android.util.Log
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

abstract class CalendarFilterPresenter(val repository: EventsRepository, val schedulers: Schedulers) : BasePresenter<CalendarView>, CalendarPresenter {

    val selectedCoins: Set<FilterItem> = HashSet()
    val selectedCategories: Set<FilterItem> = HashSet()

    var view = WeakReference<CalendarView>(null)
    var disposable = CompositeDisposable()

    override fun onFilterCoinSelected() {
        view.get()?.toggleFilterMode(0)
        disposable.add(
                if (repository.isAuthenticated()) {
                    repository.getCoins()
                            .subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(
                                    onSuccess = {
                                        view.get()?.toggleFilterMode(1)
                                        view.get()?.setFilterCoinData(it, HashSet(selectedCoins))
                                    }
                                    , onError = { Log.e("Filter Coins", it.toString()) }
                            )
                } else {
                    repository.getAndSaveToken().flatMap { repository.getCoins() }
                            .subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(
                                    onSuccess = {
                                        view.get()?.toggleFilterMode(1)
                                        view.get()?.setFilterCoinData(it, HashSet(selectedCoins))
                                    }
                                    , onError = { Log.e("Filter Coins", it.toString()) }
                            )
                }
        )
    }

    override fun onFilterCategorySelected() {
        view.get()?.toggleFilterMode(0)
        disposable.add(
                if (repository.isAuthenticated()) {
                    repository.getCategories()
                            .subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(
                                    onSuccess = {
                                        view.get()?.toggleFilterMode(1)
                                        view.get()?.setFilterCategoryData(it, HashSet(selectedCategories))
                                    }
                                    , onError = { Log.e("Filter Coins", it.toString()) }
                            )
                } else {
                    repository.getAndSaveToken().flatMap {
                        repository.getCategories()
                    }.subscribeOn(schedulers.io())
                            .observeOn(schedulers.ui())
                            .subscribeBy(
                                    onSuccess = {
                                        view.get()?.toggleFilterMode(1)
                                        view.get()?.setFilterCategoryData(it, HashSet(selectedCategories))
                                    }
                                    , onError = {
                                Log.e("Filter Coins", it.toString())
                            })


                })
    }
}