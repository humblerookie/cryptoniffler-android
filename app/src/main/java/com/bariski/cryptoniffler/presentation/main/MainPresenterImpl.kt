package com.bariski.cryptoniffler.presentation.main

import android.os.Bundle
import android.util.Log
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.COIN
import com.bariski.cryptoniffler.domain.util.EXCHANGE
import com.bariski.cryptoniffler.domain.util.Event
import com.bariski.cryptoniffler.domain.util.Screen
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.models.AmountInput
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

class MainPresenterImpl(val repository: NifflerRepository, val adapter: GridItemAdapter, private val schedulerProvider: Schedulers, val analytics: Analytics) : BasePresenter<MainView>, MainPresenter {


    private lateinit var viewWeak: WeakReference<MainView>
    val disposable = CompositeDisposable()
    val amountDisposable = CompositeDisposable()
    var btcRate: Float = 0f
    var amountSubscriber: PublishProcessor<AmountInput>? = null
    var state = 0
    var amount = 0L
    var coin = ""
    var exchange = ""
    var ignoreFees = false

    override fun onItemClicked(id: Int) {

        when (id) {
            R.id.buyAndSellCrypto -> {
                navigateToCoinSelection(true)
            }
            R.id.viewExchanges -> {
                navigateToExchangeSelection(true)
            }

        }
    }


    override fun initView(mainView: MainView) {
        viewWeak = WeakReference(mainView)
        analytics.sendScreenView(Screen.MAIN)
        viewWeak.get()?.moveToNext(BuyNSellFragment.getInstance(this), true)
    }

    override fun releaseView() {
        disposable.clear()
    }

    override fun onDrawerItemSelected(id: Int) {

    }

    override fun onClick(item: GridItem) {
        if (item is Coin) {
            coin = item.getItemCode()
            state = 2
            val b = Bundle()
            b.putString("type", COIN)
            analytics.logEvent(Event.ITEM_CLICKED, b)
            navigateToAmountInput(true)
        } else if (item is Exchange) {
            exchange = item.getItemCode()
            amount = Long.MAX_VALUE
            val b = Bundle()
            b.putString("type", EXCHANGE)
            analytics.logEvent(Event.ITEM_CLICKED, b)
            onNext()
        }
    }

    override fun onNext() {

        viewWeak.get()?.let {
            if (coin.trim().isNotEmpty()) {
                analytics.logEvent(Event.PROCEED, Bundle())
            }
            it.moveToDetailsScreen(ignoreFees, coin, exchange, amount)
            it.moveToNext(BuyNSellFragment.getInstance(this), false)
            ignoreFees = false
            coin = ""
            exchange = ""
            amount = 0
            state = 0
        }

    }


    override fun onRefresh() {
        repository.fetchLatestConfig()
    }

    override fun onAmountScreenRefresh() {
        repository.getBtcInrRates()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy(
                        onSuccess = {
                            btcRate = it[0].price
                            analytics.logBtcValEvent(true, null)

                        }, onError = {
                    analytics.logBtcValEvent(true, it.toString())
                    Log.e("MainPresenter", it.toString())
                }

                )

    }

    override fun onAmountScreenCreated(textChanges: Observable<CharSequence>, observer: Observer<AmountInput>) {
        amountSubscriber = PublishProcessor.create()
        amountDisposable.add(textChanges
                .subscribeOn(schedulerProvider.ui())
                .observeOn(schedulerProvider.io())
                .subscribeBy {
                    amount = it.toString().toLong()
                    if (btcRate != 0.0f) {
                        amountSubscriber?.onNext(AmountInput((it.toString().toLong() / btcRate).toString(), amount >= 1000))
                    }
                })
        amountSubscriber?.let {
            it
                    .toObservable()
                    .observeOn(schedulerProvider.ui())
                    .subscribe(observer)
        }
    }

    override fun onBackPressed() {
        when (--state) {
            1 -> {
                if (exchange.isNotEmpty()) {
                    navigateToExchangeSelection(false)
                } else {
                    navigateToCoinSelection(false)
                }
            }
            0 -> {
                navigateToMain(false)
            }
            else -> viewWeak.get()?.exit()
        }
    }

    private fun navigateToMain(isForward: Boolean) {
        state = 0
        viewWeak.get()?.let {
            it.toggleSearch(false)
            it.moveToNext(BuyNSellFragment.getInstance(this), isForward)
        }
    }

    private fun navigateToAmountInput(isForward: Boolean) {
        state = 2
        viewWeak.get()?.let {
            analytics.sendScreenView(Screen.AMOUNT)
            it.toggleSearch(false)
            it.moveToNext(AmountFragment.getInstance(this, coin.trim().isNotEmpty()), isForward)
        }
    }


    private fun navigateToExchangeSelection(isForward: Boolean) {
        disposable.add(repository.getExchanges().observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribeBy(
                onSuccess = { data ->
                    state = 1
                    viewWeak.get()?.let {
                        analytics.sendScreenView(Screen.PICK_EXCHANGE)
                        it.toggleSearch(false)
                        it.moveToNext(GridSelectFragment.getInstance(this, adapter, ArrayList(data), it.getMessage(R.string.common_label_buy_at)), isForward)
                    }
                })
        )

    }

    private fun navigateToCoinSelection(isForward: Boolean) {
        disposable.add(repository.getCoins().observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribeBy(
                onSuccess = { data ->
                    state = 1
                    viewWeak.get()?.let {
                        analytics.sendScreenView(Screen.PICK_COIN)
                        it.toggleSearch(true)
                        it.moveToNext(GridSelectFragment.getInstance(this, adapter, ArrayList(data), it.getMessage(R.string.common_label_i_like_to_buy)), isForward)
                    }
                })
        )

    }

    override fun onAmountScreenDestroyed() {
        amountDisposable.clear()
    }

    override fun onSearchClicked() {
        viewWeak.get()?.openSearch()
    }

    override fun onSearchResult(coin: Coin?) {
        coin?.let {
            onClick(it)
        }
    }

    override fun onIncludeFeeChanged(b: Boolean) {
        analytics.logIncludeFeeChanged(b)
        ignoreFees = !b
    }

    override fun infoClicked() {
        analytics.logInfoClick(false)
    }

}