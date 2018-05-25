package com.bariski.cryptoniffler.presentation.calendar

import android.os.Bundle
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.data.factory.HttpStatus
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.bariski.cryptoniffler.domain.util.Screen
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference

class CalendarPresenterImpl(repository: EventsRepository, schedulers: Schedulers, val analytics: Analytics) : CalendarFilterPresenter(repository, schedulers) {

    var page = 1
    var isLoading = false
    var startDate: String? = null
    var endDate: String? = null
    val MAX = 15
    var isLoadCompleted = false
    var events = ArrayList<Event>()
    val TAG = "CalendarPresenter"

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putString("startDate", startDate)
            putString("endDate", endDate)
            putParcelableArrayList("coins", ArrayList(selectedCoins))
            putParcelableArrayList("categories", ArrayList(selectedCategories))
            putParcelableArrayList("events", events)
        }
    }

    override fun loadNextPage() {
        if (!isLoading && !isLoadCompleted) {
            isLoading = true
            val initPage = page++
            view.get()?.let {
                it.toggleBottomProgress(initPage != 1)
                it.toggleCenterProgress(initPage == 1)
                it.toggleEmptyView(false)
                it.toggleError(null)
            }
            disposable.add(
                    Single.just(1)
                            .observeOn(schedulers.io())
                            .subscribeOn(schedulers.io())
                            .flatMap {
                                val selCoins = java.util.ArrayList<String>()
                                val selCats = java.util.ArrayList<String>()
                                for (k in selectedCoins) {
                                    selCoins.add(k.getIdentifier())
                                }
                                for (k in selectedCategories) {
                                    selCats.add(k.getIdentifier())
                                }
                                val coinsString = selCoins.joinToString(",")
                                val catString = selCats.joinToString(",")
                                if (repository.isAuthenticated()) {
                                    repository.getEvents(coinsString, catString, startDate, endDate, initPage, MAX)
                                } else {
                                    repository.getAndSaveToken().flatMap { repository.getEvents(coinsString, catString, startDate, endDate, initPage, MAX) }
                                }

                            }
                            .observeOn(schedulers.ui())
                            .subscribeBy(onSuccess = { data ->
                                isLoading = false
                                view.get()?.let {
                                    if (initPage != 1) {
                                        it.toggleBottomProgress(false)
                                    } else {
                                        it.toggleCenterProgress(false)
                                    }
                                    it.setData(data, initPage != 1)
                                    if (initPage == 1) {
                                        events.clear()
                                    }
                                    events.addAll(data)

                                }

                            }, onError = {
                                isLoading = false
                                page--
                                view.get()?.let {
                                    if (initPage != 1) {
                                        it.toggleBottomProgress(false)
                                    } else {
                                        it.toggleCenterProgress(false)
                                    }
                                }
                                if (it is HttpException) {
                                    if (it.code() == HttpStatus.SC_NOT_FOUND) {
                                        isLoadCompleted = true
                                        if (initPage == 1) {
                                            view.get()?.apply {
                                                toggleEmptyView(true)
                                                setData(ArrayList(), false)
                                            }

                                        }
                                    }
                                } else {
                                    if (initPage == 1) {
                                        view.get()?.toggleError(view.get()?.getMessage(if (it is IOException) R.string.error_common_network else R.string.error_common_something_wrong))
                                    }
                                }
                                Timber.e(it)
                            }))


        }
    }


    override fun onFilterClear() {
        startDate = null
        endDate = null
        page = 1
        isLoadCompleted = false
        (selectedCoins as HashSet).clear()
        (selectedCategories as HashSet).clear()
        loadNextPage()
    }

    override fun onFilterApply(coins: Set<FilterItem>?, categories: Set<FilterItem>?, from: Array<Int>?, to: Array<Int>?) {
        page = 1
        isLoadCompleted = false
        coins?.let { (selectedCoins as HashSet).addAll(it) }
        categories?.let { (selectedCategories as HashSet).addAll(it) }
        from?.let {
            it[1]++
            val sb = StringBuilder()
            var count = 0
            for (i in it) {
                if (i < 10) {
                    sb.append("0")
                }
                sb.append(i)
                count++
                if (count != it.size) {
                    sb.append("/")
                }
            }
            startDate = sb.toString()
            it[1]--

        }
        to?.let {
            it[1]++
            val sb = StringBuilder()
            var count = 0
            for (i in it) {
                if (i < 10) {
                    sb.append("0")
                }
                sb.append(i)
                count++
                if (count != it.size) {
                    sb.append("/")
                }
            }
            endDate = sb.toString()
            it[1]--
        }
        loadNextPage()

    }

    override fun onRetry() {
        loadNextPage()
    }

    override fun initView(view: CalendarView, state: Bundle?, args: Bundle?) {
        this.view = WeakReference(view)
        if (!repository.isFilterTutorialShown()) {
            repository.setFilterTutorialShown(true)
            view.showFilterTutorial()
        }
        if (state != null) {
            state.apply {
                startDate = getString("startDate")
                endDate = getString("endDate")
                (selectedCoins as HashSet).addAll(getParcelableArrayList<CalendarCoin>("coins"))
                (selectedCategories as HashSet).addAll(getParcelableArrayList<CalendarCategory>("categories"))
                val list: ArrayList<Event>? = getParcelableArrayList<Event>("events")
                if (list == null || list.isEmpty()) {
                    loadNextPage()
                } else {
                    view.toggleCenterProgress(false)
                    view.setData(list, false)
                }
            }

        } else {
            analytics.sendScreenView(Screen.CALENDAR)
            loadNextPage()
        }
    }

    override fun releaseView() {
        view.clear()
        disposable.clear()
    }

    override fun onRefresh() {

    }


}