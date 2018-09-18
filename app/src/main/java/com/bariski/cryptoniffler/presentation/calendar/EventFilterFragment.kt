package com.bariski.cryptoniffler.presentation.calendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.calendar.adapters.FilterItemAdapter
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
open abstract class EventFilterFragment : BaseInjectFragment() {


    lateinit var fromDate: TextView
    lateinit var toDate: TextView
    lateinit var filterCoin: View
    lateinit var filterCategory: View
    lateinit var filterPeriod: View
    lateinit var listFilter: RecyclerView
    lateinit var periodContainer: View
    lateinit var clearFilter: View
    lateinit var applyFilter: View
    lateinit var search: EditText
    lateinit var progress: View
    lateinit var close: View
    var lastSelected: View? = null

    val formatter = SimpleDateFormat("dd/MM/yyyy")
    var fromPicker: DatePickerDialog? = null
    var toPicker: DatePickerDialog? = null
    var from: Array<Int>? = null
    var to: Array<Int>? = null
    var coinAdapter: FilterItemAdapter? = null
    var categoryAdapter: FilterItemAdapter? = null
    var dialog: Dialog? = null

    var isLoadingFilters = false

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

                fromDate.setOnClickListener {

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
                }

                toDate.setOnClickListener {

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
                }
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
                decoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.dashed_border_bottom)!!)
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
                close.setOnClickListener {
                    dialog?.dismiss()
                }

                clearFilter.setOnClickListener {
                    dismiss()
                    getCalendarPresenter()?.onFilterClear()
                    coinAdapter?.getSelected()?.let { (it as HashSet).clear() }
                    coinAdapter?.notifyDataSetChanged()
                    categoryAdapter?.getSelected()?.let { (it as HashSet).clear() }
                    categoryAdapter?.notifyDataSetChanged()
                    fromDate.text = formatter.format(Calendar.getInstance().time)
                    val cal = Calendar.getInstance()
                    fromPicker?.datePicker?.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                    toDate.text = "-"
                }

                applyFilter.setOnClickListener {

                    val selectedCoins = coinAdapter?.getSelected()
                    val selectedCategory = categoryAdapter?.getSelected()
                    getCalendarPresenter().onFilterApply(selectedCoins, selectedCategory, from, to)
                    dismiss()
                }


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
                        getCalendarPresenter().onFilterCoinSelected()
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
                        getCalendarPresenter().onFilterCategorySelected()
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

    abstract fun getCalendarPresenter(): CalendarPresenter

}