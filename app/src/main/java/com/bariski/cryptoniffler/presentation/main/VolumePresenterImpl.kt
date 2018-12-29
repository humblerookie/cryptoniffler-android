package com.bariski.cryptoniffler.presentation.main

import android.os.Bundle
import android.util.Log
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.VolumeInfo
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference


class VolumePresenterImpl(val repository: NifflerRepository, val schedulers: Schedulers, val analytics: Analytics) : VolumePresenter {


    private val TAG = "InfoPresenter"
    var info: VolumeInfo? = null
    var viewWeak = WeakReference<VolumeView>(null)
    val disposable = CompositeDisposable()

    override fun fetchData() {
        disposable.add(
            repository.getVolumes()
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


    override fun initView(view: VolumeView, savedState: Bundle?, args: Bundle?) {
        viewWeak = WeakReference(view)
        info = savedState?.getParcelable("info")
        if (info != null) {
            viewWeak.get()?.displayInfo(info!!)
        } else {
            fetchData()
        }
    }

    override fun releaseView() {
        disposable.clear()
    }

    override fun onRefresh() {
        fetchData()
    }

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putParcelable("info", info)
        }
    }


}