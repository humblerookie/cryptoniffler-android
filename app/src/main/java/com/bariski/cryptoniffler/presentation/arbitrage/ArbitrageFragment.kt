package com.bariski.cryptoniffler.presentation.arbitrage

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
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
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.ArbitrageExchange
import com.bariski.cryptoniffler.domain.model.ArbitragePresentable
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.util.ArbitrageMode
import com.bariski.cryptoniffler.presentation.arbitrage.adapters.ArbitrageAdapter
import com.bariski.cryptoniffler.presentation.calendar.adapters.FilterItemAdapter
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.common.listeners.ItemIdClickListener
import com.bariski.cryptoniffler.presentation.common.utils.DeviceInfo
import kotlinx.android.synthetic.main.fragment_arbitrage.view.*
import me.toptas.fancyshowcase.FancyShowCaseQueue
import me.toptas.fancyshowcase.FancyShowCaseView
import java.lang.StringBuilder
import javax.inject.Inject


class ArbitrageFragment : BaseInjectFragment(), ArbitrageView, View.OnClickListener {


    @Inject
    lateinit var presenter: ArbitragePresenter
    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var deviceInfo: DeviceInfo

    lateinit var list: RecyclerView
    var snackbar: Snackbar? = null
    lateinit var container: View
    lateinit var progress: View
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var applyFilter: View
    lateinit var fabShare: View
    lateinit var from: RecyclerView
    lateinit var to: RecyclerView
    lateinit var clear: View
    lateinit var navigationView: BottomNavigationView

    var alertDialog: AlertDialog? = null
    var filterDialog: Dialog? = null
    var filterDialogDomestic: Dialog? = null
    var filterDialogInternational: Dialog? = null
    var filterDialogIntra: Dialog? = null
    var rateDialog: Dialog? = null

    lateinit var fromAdapter: FilterItemAdapter
    lateinit var toAdapter: FilterItemAdapter


    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity !is ItemIdClickListener) {
            throw IllegalStateException("Parent activity must override ItemIdClickListener")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_arbitrage, parent, false)
        list = view.list
        list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        progress = view.progress
        container = view.container
        fabShare = view.shareScreen
        swipeRefresh = view.swipeRefresh
        navigationView = view.navigationView
        navigationView.visibility = View.GONE
        navigationView.selectedItemId = when {
            presenter.isModeIndian() -> R.id.navigation_domestic
            presenter.isModeInternational() -> R.id.navigation_international
            else -> R.id.navigation_intra
        }
        navigationView.setOnNavigationItemSelectedListener { item ->
            val type = when (item.itemId) {
                R.id.navigation_domestic -> ArbitrageMode.INDIAN
                R.id.navigation_international -> ArbitrageMode.INTERNATIONAL
                else -> ArbitrageMode.INTRA_EXCHANGE
            }
            presenter.onModeChanged(type)
            true
        }
        presenter.initView(this, savedInstanceState, arguments)
        swipeRefresh.setOnRefreshListener {
            presenter.onRetry()
        }

        fabShare.setOnClickListener {
            (activity as ItemIdClickListener).onItemClick(it.id)
        }
        return view
    }

    override fun setData(arbitrage: Arbitrage, mode: Int) {
        if (isAlive()) {
            if (fabShare.visibility == View.GONE) {
                val anim = AnimationUtils.loadAnimation(activity, R.anim.fab_reveal)
                fabShare.visibility = View.VISIBLE
                fabShare.startAnimation(anim)
            }

            list.adapter = ArbitrageAdapter(arbitrage, mode, imageLoader, presenter)
            swipeRefresh.isRefreshing = false
            navigationView.visibility = View.VISIBLE
            if (mode == ArbitrageMode.INTRA_EXCHANGE) {
                val controller = AnimationUtils.loadLayoutAnimation(list.context, R.anim.layout_animation_fall_down)
                list.layoutAnimation = controller
                list.scheduleLayoutAnimation()
            }
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
                    setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.common_label_ok)
                    ) { dialog, _ -> dialog.dismiss() }
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

    override fun showFilters(mode: Int, src: List<ArbitrageExchange>, srcSelect: Set<FilterItem>, destSelect: Set<FilterItem>) {
        if (isAlive()) {
            filterDialog = when (mode) {
                ArbitrageMode.INDIAN -> filterDialogDomestic
                ArbitrageMode.INTERNATIONAL -> filterDialogInternational
                else -> filterDialogIntra
            }
            if (filterDialog == null) {
                filterDialog = Dialog(activity)
                when (mode) {
                    ArbitrageMode.INDIAN -> filterDialogDomestic = filterDialog
                    ArbitrageMode.INTERNATIONAL -> filterDialogInternational = filterDialog
                    else -> filterDialogIntra = filterDialog
                }
                filterDialog?.apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    val view = activity.layoutInflater.inflate(R.layout.segment_filter_arbitrage, null)
                    applyFilter = view.findViewById(R.id.apply)
                    from = view.findViewById(R.id.fromList)
                    to = view.findViewById(R.id.toList)
                    clear = view.findViewById(R.id.clearFilter)
                    from.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
                    fromAdapter = FilterItemAdapter(src, srcSelect)
                    from.adapter = fromAdapter
                    if (mode != ArbitrageMode.INTRA_EXCHANGE) {
                        to.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
                        toAdapter = FilterItemAdapter(src, destSelect)
                        to.adapter = toAdapter
                    } else {
                        view.findViewById<TextView>(R.id.fromTitle).text = getString(R.string.info_label_exchanges)
                        view.findViewById<View>(R.id.toTitle).visibility = View.GONE
                        to.visibility = View.GONE
                    }

                    applyFilter.setOnClickListener {
                        val selectedFrom = fromAdapter.getSelected()
                        val selectedTo = if (mode != ArbitrageMode.INTRA_EXCHANGE) toAdapter.getSelected() else HashSet()
                        presenter.onFilterApply(selectedFrom, selectedTo)
                        dismiss()
                    }
                    clear.setOnClickListener {
                        presenter.onFilterClear()
                        fromAdapter.getSelected().let { (it as HashSet<*>).clear() }
                        fromAdapter.notifyDataSetChanged()
                        if (mode != ArbitrageMode.INTRA_EXCHANGE) {
                            toAdapter.getSelected().let { (it as HashSet).clear() }
                            toAdapter.notifyDataSetChanged()
                        }
                        dismiss()
                    }


                    setContentView(view)
                }
            }

            val width = resources.displayMetrics.widthPixels
            filterDialog?.window?.setLayout(((6.0f * width) / 7).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            filterDialog?.show()

        }

    }

    override fun showRateDialog() {
        if (isAlive()) {

            if (rateDialog == null) {
                rateDialog =
                        MaterialDialog.Builder(activity)
                                .title(R.string.rating_title_spread_the_word)
                                .content(getString(R.string.rating_title_lets_be_honest) + "\n\n" + getString(R.string.rating_title_like_money) + "\n" + getString(R.string.rating_title_weve_targets) + "\n\n" + getString(R.string.rating_title_work_together))
                                .positiveText(R.string.rating_title_share)
                                .neutralText(R.string.rating_title_rate)
                                .iconRes(R.drawable.ic_volume)
                                .onPositive { _, _ ->
                                    presenter.onButtonClicked(R.id.share)
                                    (activity as ItemIdClickListener).onItemClick(R.id.share)
                                    rateDialog?.dismiss()
                                }
                                .onNeutral { _, _ ->
                                    presenter.onButtonClicked(R.id.review)
                                    (activity as ItemIdClickListener).onItemClick(R.id.review)
                                    rateDialog?.dismiss()
                                }
                                .show()
            } else {
                rateDialog?.show()
            }
        }

    }


    override fun showFeesDialog(arbitrage: ArbitragePresentable) {
        if (isAlive()) {

            val fees = StringBuilder()
            for (s in arbitrage.getFeeDetails()) {
                fees.append("- ")
                fees.append(s)
                fees.append("\n\n")
            }
            MaterialDialog.Builder(activity)
                    .title(R.string.common_title_fees)
                    .content(fees.toString())
                    .positiveText(R.string.common_title_execute)
                    .onPositive { _, _ ->
                        presenter.onArbitrageConfirmed(arbitrage)
                    }
                    .show()
        }

    }

    override fun showMessage(message: String) {
        if (isAlive()) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun navigateToApp(intent: Intent) {
        if (isAlive()) {
            startActivity(intent)
        }
    }

    override fun launchUrl(url: String) {
        if (isAlive()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
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
            val f1 = FancyShowCaseView.Builder(activity)
                    .focusRectAtPosition(deviceInfo.getWidth() / 2, deviceInfo.getHeight() / 2, 30, 30)
                    .fitSystemWindows(true)
                    .title(getString(R.string.common_tutorial_arbitrage))
                    .build()
            val f2 = FancyShowCaseView.Builder(activity)
                    .focusOn(activity.findViewById(R.id.filter))
                    .title(getString(R.string.events_tutorial_filters))
                    .build()
            FancyShowCaseQueue().add(f1).add(f2).show()

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


}