package com.bariski.cryptoniffler.presentation.calendar

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Event
import com.bariski.cryptoniffler.domain.model.FilterItem
import com.bariski.cryptoniffler.presentation.calendar.adapters.CalendarAdapter
import com.bariski.cryptoniffler.presentation.calendar.adapters.FilterItemAdapter
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import me.toptas.fancyshowcase.FancyShowCaseView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CalendarFragment : BaseInjectFragment(), CalendarView, View.OnClickListener {


    @Inject
    lateinit var presenter: CalendarPresenter


    lateinit var fromDate: TextView
    lateinit var toDate: TextView
    lateinit var filterCoin: View
    lateinit var filterCategory: View
    lateinit var filterPeriod: View
    lateinit var listFilter: RecyclerView
    lateinit var periodContainer: View
    lateinit var close: View
    lateinit var clearFilter: View
    lateinit var applyFilter: View
    lateinit var search: EditText
    lateinit var progress: View
    lateinit var list: RecyclerView
    lateinit var bottomProgress: View
    lateinit var centerProgress: View
    lateinit var empty: TextView
    lateinit var container: View

    var snackbar: Snackbar? = null
    var dialog: Dialog? = null
    var lastSelected: View? = null
    var isLoadingFilters = false
    var coinAdapter: FilterItemAdapter? = null
    var categoryAdapter: FilterItemAdapter? = null
    var fromPicker: DatePickerDialog? = null
    var toPicker: DatePickerDialog? = null
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    lateinit var adapter: CalendarAdapter
    var from: Array<Int>? = null
    var to: Array<Int>? = null


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
    }

    override fun getMessage(resourceId: Int): String {
        return resources.getString(resourceId)
    }


    fun showDialog() {
        if (dialog == null) {
            dialog = Dialog(activity)
            dialog?.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                val view = activity.layoutInflater.inflate(R.layout.segment_filter, null)
                fromDate = view.findViewById(R.id.fromDate)
                fromDate.text = formatter.format(Calendar.getInstance().time)
                toDate = view.findViewById(R.id.toDate)
                toDate.text = "-"

                fromDate.setOnClickListener({

                    if (fromPicker == null) {
                        val calendar = Calendar.getInstance()
                        fromPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            fromDate.text = dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year
                            val newDate = Calendar.getInstance()
                            newDate.set(year, month, dayOfMonth)
                            from = arrayOf(dayOfMonth, month, year)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        fromPicker?.let { it.datePicker.minDate = Calendar.getInstance().timeInMillis - 1000 }
                    }
                    fromPicker?.show()
                })

                toDate.setOnClickListener({

                    if (toPicker == null) {
                        val calendar = Calendar.getInstance()
                        toPicker = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            toDate.text = dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year
                            val newDate = Calendar.getInstance()
                            newDate.set(year, month, dayOfMonth)
                            to = arrayOf(dayOfMonth, month, year)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        toPicker?.let { it.datePicker.minDate = Calendar.getInstance().timeInMillis - 1000 }
                    }
                    toPicker?.show()
                })
                filterCategory = view.findViewById(R.id.category)
                filterCoin = view.findViewById(R.id.coins)
                filterPeriod = view.findViewById(R.id.period)
                periodContainer = view.findViewById(R.id.periodContent)
                close = view.findViewById(R.id.close)
                applyFilter = view.findViewById(R.id.apply)
                clearFilter = view.findViewById(R.id.clearAll)
                listFilter = view.findViewById(R.id.list)
                search = view.findViewById(R.id.search)
                search.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val currentAdapter: FilterItemAdapter? = if (listFilter.adapter != null) listFilter.adapter as FilterItemAdapter else null
                        currentAdapter?.filterDataSet(s.toString().trim().toLowerCase())
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
                val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
                decoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.dashed_border_bottom))
                listFilter.addItemDecoration(decoration)
                progress = view.findViewById(R.id.progress)
                listFilter.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                val listener = View.OnClickListener { it ->
                    if (!isLoadingFilters) {
                        isLoadingFilters = true
                        val color = ContextCompat.getColor(activity, R.color.black_02)
                        filterPeriod.setBackgroundColor(color)
                        filterCoin.setBackgroundColor(color)
                        filterCategory.setBackgroundColor(color)
                        it.setBackgroundColor(Color.WHITE)
                        switchContents(it)
                    }
                }
                close.setOnClickListener({
                    dialog?.dismiss()
                })

                clearFilter.setOnClickListener({
                    dismiss()
                    presenter?.onFilterClear()
                    coinAdapter?.getSelected()?.let { (it as HashSet).clear() }
                    coinAdapter?.notifyDataSetChanged()
                    categoryAdapter?.getSelected()?.let { (it as HashSet).clear() }
                    categoryAdapter?.notifyDataSetChanged()
                    fromDate.text = formatter.format(Calendar.getInstance().time)
                    val cal = Calendar.getInstance()
                    fromPicker?.datePicker?.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                    toDate.text = "-"
                })

                applyFilter.setOnClickListener({

                    val selectedCoins = coinAdapter?.getSelected()
                    val selectedCategory = categoryAdapter?.getSelected()
                    presenter?.onFilterApply(selectedCoins, selectedCategory, from, to)
                    dismiss()
                })


                filterCategory.setOnClickListener(listener)
                filterCoin.setOnClickListener(listener)
                filterPeriod.setOnClickListener(listener)
                setContentView(view)
                setOnDismissListener {

                }

            }
        }
        dialog?.show()
        filterCoin.performClick()
    }

    private fun switchContents(view: View) {
        when (view) {
            filterPeriod -> {
                listFilter.visibility = View.GONE
                periodContainer.visibility = View.VISIBLE
                search.visibility = View.GONE
                isLoadingFilters = false
            }

            filterCoin -> {
                listFilter.visibility = View.VISIBLE
                periodContainer.visibility = View.GONE
                search.visibility = View.VISIBLE
                if (lastSelected == null || lastSelected != filterCoin) {
                    if (coinAdapter == null) {
                        presenter.onFilterCoinSelected()
                    } else {
                        isLoadingFilters = false
                        listFilter.adapter = coinAdapter
                        search.setText("")
                    }
                } else {
                    isLoadingFilters = false
                }

            }

            else -> {
                listFilter.visibility = View.VISIBLE
                periodContainer.visibility = View.GONE
                search.visibility = View.VISIBLE
                if (lastSelected == null || lastSelected != filterPeriod) {
                    if (categoryAdapter == null) {
                        presenter.onFilterCategorySelected()
                    } else {
                        isLoadingFilters = false
                        listFilter.adapter = categoryAdapter
                        search.setText("")
                    }
                } else {
                    isLoadingFilters = false
                }
            }

        }
        lastSelected = view
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


}