package com.bariski.cryptoniffler.presentation.main

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import com.bariski.cryptoniffler.R
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


    override fun onDonateClicked(id: Int) {
        donateIndex[id]?.let {
            donateAnalyticsMap[id]?.let { analytics.logDonateCopiedEvent(it) }
            val clip = ClipData.newPlainText("Crypto Address", viewWeak.get()?.getMessage(it))
            clipboardManager.primaryClip = clip
            viewWeak.get()?.showAddressCopiedMessage()
        }
    }

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putParcelable("info", info)
        }
    }

    companion object {
        val donateIndex = mapOf(
                R.id.btcTip to R.string.info_donation_btc_address,
                R.id.dogeTip to R.string.info_donation_doge_address,
                R.id.ltcTip to R.string.info_donation_ltc_address,
                R.id.xrpTip to R.string.info_donation_xrp_address,
                R.id.xrpTagTip to R.string.info_donation_xrp_tag
        )

        val donateAnalyticsMap = mapOf(
                R.id.btcTip to 1,
                R.id.dogeTip to 2,
                R.id.ltcTip to 3,
                R.id.xrpTip to 4,
                R.id.xrpTagTip to 4
        )
    }
}