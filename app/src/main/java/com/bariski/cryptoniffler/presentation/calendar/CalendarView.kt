package com.bariski.cryptoniffler.presentation.calendar

import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.presentation.common.BaseView

interface CalendarView : BaseView {
    fun toggleCenterProgress(visible: Boolean)
    fun toggleBottomProgress(visible: Boolean)
    fun toggleError(message: String?)
    fun setData(events: List<Event>, isAppend: Boolean)
    fun setFilterCoinData(data: List<String>, selected: HashSet<String>)
    fun setFilterCategoryData(data: List<String>, selected: HashSet<String>)
    fun toggleFilterMode(mode: Int)
    fun toggleEmptyView(b: Boolean)
    fun showFilterTutorial()

}