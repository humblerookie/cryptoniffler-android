package com.bariski.cryptoniffler.presentation.calendar

import com.bariski.cryptoniffler.presentation.common.BasePresenter


interface CalendarPresenter : BasePresenter<CalendarView> {

    fun loadNextPage()

    fun onFilterApply(coins: HashSet<String>?, categories: HashSet<String>?, from: Array<Int>?, to: Array<Int>?)

    fun onFilterClear()

    fun onRetry()

    fun onFilterCoinSelected()

    fun onFilterCategorySelected()
}