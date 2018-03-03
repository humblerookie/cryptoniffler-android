package com.bariski.cryptoniffler.presentation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.BaseActivity
import com.bariski.cryptoniffler.presentation.common.BasePresenter
import com.bariski.cryptoniffler.presentation.common.BaseView
import com.bariski.cryptoniffler.presentation.main.adapters.SearchAdapter
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : BaseActivity(), SearchView {


    override val layoutResId = R.layout.activity_search

    @Inject
    lateinit var presenter: SearchPresenter

    @Inject
    lateinit var loader: ImageLoader

    lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.elevation = resources.getDimension(R.dimen.margin_tiny)
        }
        adapter = SearchAdapter(loader, presenter)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.adapter = adapter
        presenter.initView(this, savedInstanceState, intent.extras)
        clear.setOnClickListener { search.setText("") }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showData(data: List<Coin>?) {
        if (data != null) {
            list.visibility = View.VISIBLE
            adapter.setData(data)
        } else {
            list.visibility = View.GONE
        }
    }

    override fun getSearchObservable(): InitialValueObservable<CharSequence> {
        return RxTextView.textChanges(search)
    }

    override fun toggleEmptyView(visible: Boolean) {
        empty.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun finishAndSendData(coin: Coin) {
        val intent = Intent()
        intent.putExtra("coin", coin)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun <T : BaseView> getBasePresenter(): BasePresenter<T> {
        return presenter as BasePresenter<T>
    }

}
