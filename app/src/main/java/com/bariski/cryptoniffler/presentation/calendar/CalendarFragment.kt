package com.bariski.cryptoniffler.presentation.calendar

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.presentation.calendar.adapters.CalendarAdapter
import com.bariski.cryptoniffler.presentation.calendar.adapters.FilterItemAdapter
import me.toptas.fancyshowcase.FancyShowCaseView
import javax.inject.Inject


class CalendarFragment : EventFilterFragment(), CalendarView, View.OnClickListener {


    @Inject
    lateinit var presenter: CalendarPresenter


    lateinit var list: RecyclerView
    lateinit var bottomProgress: View
    lateinit var centerProgress: View
    lateinit var empty: TextView
    lateinit var container: View

    var snackbar: Snackbar? = null


    lateinit var adapter: CalendarAdapter


    override fun toggleCenterProgress(visible: Boolean) {
        if (isAlive()) {
            centerProgress.visibility = if (visible) View.VISIBLE else View.GONE
            centerProgress.isClickable = visible
        }
    }

    override fun toggleBottomProgress(visible: Boolean) {
        if (isAlive()) {
            bottomProgress.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun toggleError(error: String?) {
        if (isAlive()) {
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

    override fun setData(events: List<Event>, isAppend: Boolean) {
        adapter.setData(events, isAppend)
        if (!isAppend) {
            val controller = AnimationUtils.loadLayoutAnimation(list.context, R.anim.layout_animation_fall_down)
            list.layoutAnimation = controller
            list.scheduleLayoutAnimation()
        }
    }

    override fun getMessage(resourceId: Int): String {
        return resources.getString(resourceId)
    }


    override fun setFilterCoinData(data: List<FilterItem>, selected: Set<FilterItem>) {
        if (isAlive()) {
            coinAdapter = FilterItemAdapter(data, selected)
            listFilter.adapter = coinAdapter
            isLoadingFilters = false
            search.setText("")
        }
    }

    override fun setFilterCategoryData(data: List<FilterItem>, selected: Set<FilterItem>) {
        if (isAlive()) {
            categoryAdapter = FilterItemAdapter(data, selected)
            listFilter.adapter = categoryAdapter
            isLoadingFilters = false
            search.setText("")
        }
    }

    override fun toggleFilterMode(mode: Int) {
        when (mode) {
            0 -> {
                progress.visibility = View.VISIBLE
                listFilter.visibility = View.GONE
                periodContainer.visibility = View.GONE
            }
            1 -> {
                progress.visibility = View.GONE
                listFilter.visibility = View.VISIBLE
                search.visibility = View.VISIBLE
                periodContainer.visibility = View.GONE
            }
            2 -> {
                progress.visibility = View.GONE
                listFilter.visibility = View.GONE
                periodContainer.visibility = View.VISIBLE
            }
        }
    }

    companion object {

        fun getInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleError(null)
        presenter.releaseView()
    }

    override fun toggleEmptyView(b: Boolean) {
        if (isAlive()) {
            empty.visibility = if (b) View.VISIBLE else View.GONE
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.filter) {
            showDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        adapter = CalendarAdapter()
        list = view.findViewById(R.id.list)
        bottomProgress = view.findViewById(R.id.bottomProgress)
        centerProgress = view.findViewById(R.id.centerProgress)
        empty = view.findViewById(R.id.empty)
        this.container = view.findViewById(R.id.container)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = (list.layoutManager as LinearLayoutManager)
                if (lm.findLastVisibleItemPosition() == adapter.itemCount - 2 && adapter.itemCount != 0) {
                    presenter.loadNextPage()
                }
            }
        })
        presenter.initView(this, savedInstanceState, arguments)
        return view

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

    override fun getCalendarPresenter() = presenter
}