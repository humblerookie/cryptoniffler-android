package com.bariski.cryptoniffler.presentation.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.BaseActivity
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.BaseView
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.bariski.cryptoniffler.presentation.main.adapters.ItemDetailAdapter
import com.bariski.cryptoniffler.presentation.main.model.GridDetailWrapper
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_coin_detail.*
import java.util.*
import javax.inject.Inject


class CoinDetailActivity : BaseActivity(), CoinDetailView {

    @Inject
    lateinit var presenter: ItemDetailPresenter
    @Inject
    lateinit var adapter: ItemDetailAdapter
    @Inject
    lateinit var loader: ImageLoader

    var snackbar: Snackbar? = null

    override val layoutResId: Int
        get() = R.layout.activity_coin_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        list.layoutManager = GridLayoutManager(this, 2)
        list.adapter = adapter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        try {
            Crashlytics.setString("c", intent.getStringExtra("coin"))
            Crashlytics.setString("e", intent.getStringExtra("exchange"))
            Crashlytics.setLong("l", intent.getLongExtra("amount", 0))
            Crashlytics.setLong("time", Calendar.getInstance().timeInMillis)
        } catch (e: Exception) {
            Log.e("Setting Crashlytics", e.toString())
        }
        presenter.initView(this, savedInstanceState, intent.extras)
    }

    override fun setData(gridDetailWrapper: GridDetailWrapper) {
        if (isAlive()) {
            app_bar.visibility = View.VISIBLE
            adapter = ItemDetailAdapter(loader)
            list.adapter = adapter
            adapter.setItems(gridDetailWrapper.items)
            toolbar.title = gridDetailWrapper.name
            toolbar_layout.title = gridDetailWrapper.name
            title = gridDetailWrapper.name.substring(0, 1).toUpperCase() + gridDetailWrapper.name.substring(1).toLowerCase()
            if (gridDetailWrapper.img != null) {
                loader.loadImage(ImageRequest(mainImg, R.drawable.placeholder, gridDetailWrapper.img, null, this, R.drawable.placeholder, false))
            } else {
                loader.loadImage(ImageRequest(mainImg, R.drawable.placeholder, "", null, this, R.drawable.placeholder, false))
            }
        }
    }

    override fun toggleDummyCards(visible: Boolean) {
        if (isAlive()) {
            app_bar.visibility = View.VISIBLE
            val exchange = intent.getStringExtra("exchange")
            title = if (exchange == null || exchange.isEmpty()) intent.getStringExtra("coin") else exchange
            toolbar.title = if (exchange == null || exchange.isEmpty()) intent.getStringExtra("coin") else exchange
            loader.loadImage(ImageRequest(mainImg, R.drawable.placeholder, "", null, this, R.drawable.placeholder, false))
            adapter.showLoadingCards = visible
            adapter.notifyDataSetChanged()
        }
    }


    override fun toggleProgress(visible: Boolean) {
        if (isAlive()) {
            progress.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun toggleError(visible: Boolean, error: String?) {
        if (isAlive()) {
            if (visible) {
                if (snackbar == null) {
                    snackbar = Snackbar.make(container, error!!, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.common_label_retry)) {
                                presenter.onRetry()
                            }
                            .setDuration(Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                }
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        }
    }

    override fun <T : BaseView> getBasePresenter(): BasePresenter<T> {
        return presenter as BasePresenter<T>
    }
}
