package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import com.bariski.cryptoniffler.presentation.common.BaseView

interface MainView : BaseView {

    fun moveToNext(fragment: Fragment,isForward:Boolean)
    fun toggleSearch(visible:Boolean)
    fun exit()
    fun moveToDetailsScreen(ignoreFees: Boolean, coin: String?, exchange: String?, amount: Long)
    fun openSearch()
}