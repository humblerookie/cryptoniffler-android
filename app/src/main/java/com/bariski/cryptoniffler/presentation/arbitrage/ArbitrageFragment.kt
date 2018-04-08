package com.bariski.cryptoniffler.presentation.arbitrage

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.arbitrage.adapters.ArbitrageAdapter
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import kotlinx.android.synthetic.main.fragment_arbitrage.view.*
import javax.inject.Inject


class ArbitrageFragment : BaseInjectFragment(), ArbitrageView, View.OnClickListener {


    override fun getMessage(resourceId: Int) = getString(resourceId)

    @Inject
    lateinit var presenter: ArbitragePresenter
    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var list: RecyclerView
    var snackbar: Snackbar? = null
    lateinit var container: View
    lateinit var progress: View
    lateinit var swipeRefresh: SwipeRefreshLayout
    var alertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_arbitrage, parent, false)
        list = view.list
        list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        progress = view.progress
        container = view.container
        swipeRefresh = view.swipeRefresh
        presenter.initView(this, savedInstanceState, arguments)
        swipeRefresh.setOnRefreshListener {
            presenter.onRetry()
        }
        return view
    }

    override fun setData(arbitrage: Arbitrage) {
        if (isAlive()) {
            list.adapter = ArbitrageAdapter(arbitrage, imageLoader)
            swipeRefresh.isRefreshing = false
        }
    }

    override fun toggleError(error: String?) {
        if (isAlive()) {
            swipeRefresh.isRefreshing = false
            if (error != null) {
                snackbar = Snackbar.make(container, error!!, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.common_label_retry)) {
                            snackbar?.dismiss()
                            presenter?.onRetry()
                        }
                        .setDuration(Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleError(null)
        presenter.releaseView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }


    override fun toggleProgress(visible: Boolean) {
        if (isAlive()) {
            progress.visibility = if (visible) View.VISIBLE else View.GONE
            progress.isClickable = visible
        }
    }

    companion object {

        fun getInstance(): ArbitrageFragment {
            return ArbitrageFragment()
        }
    }

    override fun showInfoDialog() {
        if (isAlive()) {
            if (alertDialog == null) {
                alertDialog = AlertDialog.Builder(activity).create()
                alertDialog?.apply {
                    setTitle(getString(R.string.drawer_title_arbitrage))
                    setMessage(getString(R.string.arbitrage_description))
                    setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.common_label_ok),
                            { dialog, _ -> dialog.dismiss() })
                }
            }
            alertDialog?.show()
        }

    }

    override fun onClick(p0: View?) {
        showInfoDialog()
    }


}