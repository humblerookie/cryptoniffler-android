package com.bariski.cryptoniffler.presentation.arbitrage

import android.app.Dialog
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
import android.view.Window
import android.widget.Toast
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.ArbitrageExchange
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.arbitrage.adapters.ArbitrageAdapter
import com.bariski.cryptoniffler.presentation.calendar.adapters.FilterItemAdapter
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import kotlinx.android.synthetic.main.fragment_arbitrage.view.*
import me.toptas.fancyshowcase.FancyShowCaseView
import java.util.*
import javax.inject.Inject


class ArbitrageFragment : BaseInjectFragment(), ArbitrageView, View.OnClickListener {

    @Inject
    lateinit var presenter: ArbitragePresenter
    @Inject
    lateinit var imageLoader: ImageLoader

    lateinit var list: RecyclerView
    var snackbar: Snackbar? = null
    lateinit var container: View
    lateinit var progress: View
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var applyFilter: View
    lateinit var from: RecyclerView
    lateinit var to: RecyclerView
    lateinit var clear: View

    var alertDialog: AlertDialog? = null
    var filterDialog: Dialog? = null
    var rateDialog: Dialog? = null

    lateinit var fromAdapter: FilterItemAdapter
    lateinit var toAdapter: FilterItemAdapter


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

    override fun setData(arbitrage: Arbitrage, isInternational: Boolean) {
        if (isAlive()) {
            list.adapter = ArbitrageAdapter(arbitrage, isInternational, imageLoader, presenter)
            swipeRefresh.isRefreshing = false
        }
    }

    override fun toggleError(error: String?) {
        if (isAlive()) {
            swipeRefresh.isRefreshing = false
            if (error != null) {
                snackbar = Snackbar.make(container, error, Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.common_label_retry)) {
                            snackbar?.dismiss()
                            presenter.onRetry()
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


    override fun showInfo() {
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.info -> showInfo()
            R.id.filter -> presenter.onFilterClicked()
        }

    }

    override fun showFilters(src: List<ArbitrageExchange>, srcSelect: Set<FilterItem>, destSelect: Set<FilterItem>) {
        if (isAlive()) {
            if (filterDialog == null) {
                filterDialog = Dialog(activity)
                filterDialog?.apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    val view = activity.layoutInflater.inflate(R.layout.segment_filter_arbitrage, null)
                    applyFilter = view.findViewById(R.id.apply)
                    from = view.findViewById(R.id.fromList)
                    to = view.findViewById(R.id.toList)
                    clear = view.findViewById(R.id.clearFilter)
                    from.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
                    to.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
                    fromAdapter = FilterItemAdapter(src, srcSelect)
                    from.adapter = fromAdapter
                    toAdapter = FilterItemAdapter(src, destSelect)
                    to.adapter = toAdapter
                    applyFilter.setOnClickListener({
                        val selectedFrom = fromAdapter.getSelected()
                        val selectedTo = toAdapter.getSelected()
                        presenter.onFilterApply(selectedFrom, selectedTo)
                        dismiss()
                    })
                    clear.setOnClickListener({
                        presenter.onFilterClear()
                        fromAdapter.getSelected().let { (it as HashSet).clear() }
                        fromAdapter.notifyDataSetChanged()
                        toAdapter.getSelected().let { (it as HashSet).clear() }
                        toAdapter.notifyDataSetChanged()
                        dismiss()
                    })


                    setContentView(view)
                }
            }
            filterDialog?.show()
        }

    }

    override fun showRateDialog() {
        if (isAlive()) {
            if (rateDialog == null) {
                rateDialog = Dialog(activity)
                rateDialog?.apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    val view = activity.layoutInflater.inflate(R.layout.item_rate_review, null)
                    setContentView(view)
                    val rateView = view.findViewById<View>(R.id.review)
                    val reviewView = view.findViewById<View>(R.id.share)
                    rateView.setOnClickListener {
                        presenter.onButtonClicked(it.id)
                        (activity as View.OnClickListener).onClick(it)
                        dismiss()
                    }
                    reviewView.setOnClickListener {
                        presenter.onButtonClicked(it.id)
                        (activity as View.OnClickListener).onClick(it)
                        dismiss()
                    }
                }
            }
            rateDialog?.show()
        }

    }

    override fun showMessage(message: String) {
        if (isAlive()) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun getMessage(resourceId: Int) = getString(resourceId)

    companion object {

        fun getInstance(): ArbitrageFragment {
            return ArbitrageFragment()
        }
    }

    override fun showFilterTutorial() {
        if (isAlive()) {
            FancyShowCaseView.Builder(activity)
                    .focusOn(activity.findViewById(R.id.filter))
                    .title(getString(R.string.events_tutorial_filters))
                    .build()
                    .show()
        }
    }

}