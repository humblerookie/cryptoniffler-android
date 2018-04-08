package com.bariski.cryptoniffler.presentation.calendar

import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.presentation.common.BasePresenter


interface CalendarPresenter : BasePresenter<CalendarView> {

    fun loadNextPage()

    fun onFilterApply(coins: Set<FilterItem>?, categories: Set<FilterItem>?, from: Array<Int>?, to: Array<Int>?)

    fun onFilterClear()

    fun onRetry()

    fun onFilterCoinSelected()

    fun onFilterCategorySelected()
}