package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import com.bariski.cryptoniffler.presentation.common.BaseView
import java.io.File

interface MainView : BaseView {

    fun moveToNext(fragment: Fragment, isForward: Boolean)
    fun toggleSearch(visible: Boolean)
    fun exit()
    fun moveToDetailsScreen(ignoreFees: Boolean, coin: String?, exchange: String?, amount: Long)
    fun openSearch()
    fun reviewApp()
    fun shareApp()
    fun toggleFilter(b: Boolean)
    fun toggleProgress(b: Boolean)
    fun getCommonPresenter(): MainPresenter
    fun toggleDrawer(b: Boolean)
    fun requestStoragePermission(ignoreDialog: Boolean)
    fun toggleInfo(b: Boolean)
    fun sendFeedback()
    fun shareArbitrage(file: File)
    fun showInfo()
    fun showVersionChangeInfo()
}