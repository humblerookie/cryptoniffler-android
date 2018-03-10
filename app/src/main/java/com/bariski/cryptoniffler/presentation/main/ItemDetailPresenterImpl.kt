package com.bariski.cryptoniffler.presentation.main

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.analytics.Analytics
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.bariski.cryptoniffler.domain.util.COIN
import com.bariski.cryptoniffler.domain.util.EXCHANGE
import com.bariski.cryptoniffler.presentation.common.models.GridItemDetail
import com.bariski.cryptoniffler.presentation.common.utils.ALL
import com.bariski.cryptoniffler.presentation.common.utils.AT
import com.bariski.cryptoniffler.presentation.common.utils.HERE
import com.bariski.cryptoniffler.presentation.common.utils.PERCENTAGE
import com.bariski.cryptoniffler.presentation.main.model.GridDetailWrapper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class ItemDetailPresenterImpl(val repository: NifflerRepository, private val schedulerProvider: Schedulers, private val context: Context, val analytics: Analytics) : ItemDetailPresenter {


    var ignoreFees = false
    var coin: String? = null
    var exchange: String? = null
    var amount: Long = 0
    val disposable = CompositeDisposable()
    var loadFinished = false

    override fun onRetry() {
        loadData(ignoreFees, coin, exchange, amount)
    }

    override fun loadData(ignoreFees: Boolean, coinName: String?, exchange: String?, amount: Long) {
        Log.i("ItemDetail", "1")
        this.ignoreFees = ignoreFees
        this.coin = coinName
        this.exchange = exchange
        this.amount = amount
        disposable.add(Single.just(1)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeBy {
                    Log.i("ItemDetail", "1.1")
                    if (!loadFinished) {
                        Log.i("ItemDetail", "1.2")
                        view.get()?.apply {
                            toggleProgress(false)
                            toggleDummyCards(true)
                        }
                    }
                })
        if (exchange == null || exchange.trim().isEmpty()) {
            disposable.add(repository.getBestRates(coinName, amount, ignoreFees)
                    .subscribeOn(schedulerProvider.io())
                    .doOnSubscribe {
                        Log.i("ItemDetail", "2.1")
                        view.get()?.toggleProgress(true)
                        view.get()?.toggleError(false, null)
                    }
                    .observeOn(schedulerProvider.io())
                    .map {
                        Log.i("ItemDetail", "2.2")
                        val bestBuy = ArrayList(it.values).sortedWith(compareByDescending({ it.buyEfficiency }))
                        val bestSell = ArrayList(it.values).sortedWith(compareByDescending({ it.sellEfficiency }))

                        val displayItems = ArrayList<GridItemDetail>()
                        for (i in bestBuy.indices) {
                            var coin = bestBuy[i]
                            var summaryName = if (coinName?.toLowerCase() != ALL) {
                                coin.name
                            } else {
                                coin.coin!!
                            }
                            displayItems.add(GridItemDetail(summaryName, coin.buy, getDisplayString(coin.name, coin.buyEfficiency - 100, coin.coin!!, true), coin.imgUrl))
                            coin = bestSell[i]
                            summaryName = if (coinName?.toLowerCase() != ALL) {
                                coin.name
                            } else {
                                coin.coin!!
                            }
                            displayItems.add(GridItemDetail(summaryName, coin.sell, getDisplayString(coin.name, coin.sellEfficiency - 100, coin.coin!!, false), coin.imgUrl))
                        }
                        GridDetailWrapper(it.name, it.imgUrl, displayItems)
                    }
                    .observeOn(schedulerProvider.ui())
                    .doAfterTerminate { view.get()?.toggleProgress(false) }
                    .subscribeBy(onSuccess = {
                        loadFinished = true
                        Log.i("ItemDetail", "2.3")
                        analytics.itemDetailEvent(true, null, COIN, coinName!!, amount, ignoreFees)
                        view.get()?.apply {
                            toggleDummyCards(false)
                            setData(it)
                        }
                    },
                            onError = {
                                loadFinished = true
                                Log.i("ItemDetail", "2.4")
                                analytics.itemDetailEvent(false, it.toString(), COIN, coinName!!, amount, ignoreFees)
                                view.get()?.apply {
                                    toggleDummyCards(false)
                                    toggleError(true, view.get()?.getMessage(if (it is IOException) R.string.error_common_network else R.string.error_common_something_wrong))
                                }

                            }

                    )
            )


        } else {
            disposable.add(repository.getBestCoin(exchange, amount, ignoreFees)
                    .subscribeOn(schedulerProvider.io())
                    .doOnSubscribe {
                        Log.i("ItemDetail", "3.1")
                        view.get()?.toggleProgress(true)
                        view.get()?.toggleError(false, null)
                    }
                    .observeOn(schedulerProvider.io())
                    .map {
                        Log.i("ItemDetail", "3.2")
                        val bestBuy = ArrayList(it.coins).sortedWith(compareByDescending({ it.buyEfficiency }))
                        val bestSell = ArrayList(it.coins).sortedWith(compareByDescending({ it.sellEfficiency }))

                        val displayItems = ArrayList<GridItemDetail>()
                        for (i in bestBuy.indices) {
                            var coin = bestBuy[i]
                            displayItems.add(GridItemDetail(coin.name, coin.buy, getDisplayString(coin.exchange, coin.buyEfficiency - 100, coin.symbol, true), coin.imgUrl))
                            coin = bestSell[i]
                            displayItems.add(GridItemDetail(coin.name, coin.sell, getDisplayString(coin.exchange, coin.sellEfficiency - 100, coin.symbol, false), coin.imgUrl))
                        }
                        GridDetailWrapper(it.exchangeName, it.imgUrl, displayItems)
                    }
                    .observeOn(schedulerProvider.ui())
                    .doAfterTerminate { view.get()?.toggleProgress(false) }
                    .subscribeBy(onSuccess = {
                        Log.i("ItemDetail", "3.3")
                        loadFinished = true
                        analytics.itemDetailEvent(true, null, EXCHANGE, exchange, amount, ignoreFees)
                        view.get()?.setData(it)
                    },
                            onError = {
                                Log.i("ItemDetail", "3.4")
                                loadFinished = true
                                analytics.itemDetailEvent(false, it.toString(), EXCHANGE, exchange, amount, ignoreFees)
                                view.get()?.toggleError(true, view.get()?.getMessage(if (it is IOException) R.string.error_common_network else R.string.error_common_something_wrong))
                            }

                    ))
        }


    }

    private fun getDisplayString(exchange: String?, delta: Float, coin: String, isBuy: Boolean): SpannableString? {
        view.get()?.let {
            var color: Int = ContextCompat.getColor(context, R.color.black_54)
            val resource: Int = when {
                delta > 0 -> if (isBuy) {
                    color = ContextCompat.getColor(context, R.color.green_400)
                    R.string.detail_content_buying_below
                } else {
                    color = ContextCompat.getColor(context, R.color.green_400)
                    R.string.detail_content_selling_above
                }
                delta < 0 -> if (isBuy) {
                    color = ContextCompat.getColor(context, R.color.red_400)
                    R.string.detail_content_buying_above
                } else {
                    color = ContextCompat.getColor(context, R.color.red_400)
                    R.string.detail_content_selling_below
                }
                else -> if (isBuy) {
                    R.string.detail_content_buying_exactly
                } else {
                    R.string.detail_content_selling_exactly
                }
            }
            val form = DecimalFormat("0.00")
            val ret = if (delta != 0.0f) {
                it.getString(resource, coin, form.format(Math.abs(delta)) + PERCENTAGE)
            } else {
                it.getString(resource, coin)
            }
            ret?.let {
                var string = it
                return if (delta != 0.0f) {

                    var exchangeReplaceMent: String? = null
                    if (exchange != null) {
                        exchangeReplaceMent = " " + context.getString(R.string.detail_content_on_exchange, exchange) + " "
                        string = string.replace(HERE, exchangeReplaceMent)
                    }
                    val index = string.indexOf(AT)
                    val wordtoSpan = SpannableString(string)
                    wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), 0, coin.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    wordtoSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_54)), 0, coin.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    wordtoSpan.setSpan(ForegroundColorSpan(color), index + AT.length, string.indexOf(PERCENTAGE) + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), index + AT.length, string.indexOf(PERCENTAGE) + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    exchangeReplaceMent?.let {
                        wordtoSpan.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.black_54)), string.indexOf(it) + 3, string.indexOf(it) + it.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), string.indexOf(it) + 3, string.indexOf(it) + it.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    }
                    wordtoSpan
                } else {
                    SpannableString(it)
                }


            }
        }
        return null
    }

    private var view: WeakReference<CoinDetailView> = WeakReference<CoinDetailView>(null)

    override fun onSortClicked(type: Int) {

    }


    override fun releaseView() {
        disposable.clear()
    }

    override fun onRefresh() {
        repository.fetchLatestConfig()
    }

    override fun initView(view: CoinDetailView, savedState: Bundle?, args: Bundle?) {
        this.view = WeakReference(view)
        val state = savedState ?: args!!
        state?.apply {
            ignoreFees = getBoolean("ignoreFees")
            coin = getString("coin")
            exchange = getString("exchange")
            amount = getLong("amount")
        }
        loadData(ignoreFees, coin, exchange, amount)
    }

    override fun saveState(outState: Bundle?) {
        outState?.apply {
            putBoolean("ignoreFees", ignoreFees)
            putString("coin", coin)
            putString("exchange", exchange)
            putLong("amount", amount)
        }
    }
}