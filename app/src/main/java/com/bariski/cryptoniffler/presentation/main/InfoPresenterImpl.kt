package com.bariski.cryptoniffler.presentation.main

import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference


class InfoPresenterImpl(val clipboardManager: ClipboardManager, val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics) : InfoPresenter {


    private val TAG = "InfoPresenter"
    var info: Info? = null
    var viewWeak = WeakReference<InfoView>(null)
    val disposable = CompositeDisposable()

    fun fetchInfo() {
        disposable.add(
                repository.getStaticInfo()
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.ui())
                        .subscribeBy(onSuccess = {
                            info = it
                            viewWeak.get()?.displayInfo(it)
                        }, onError = {
                            Log.e(TAG, it.toString())
                        })
        )
    }

    override fun initView(view: InfoView, savedState: Bundle?, args: Bundle?) {
        viewWeak = WeakReference(view)
        info = savedState?.getParcelable("info")
        if (info != null) {
            viewWeak.get()?.displayInfo(info!!)
        } else {
            fetchInfo()
        }
    }

    override fun releaseView() {
        disposable.clear()
    }

    override fun onRefresh() {

    }


    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putParcelable("info", info)
        }
    }


}