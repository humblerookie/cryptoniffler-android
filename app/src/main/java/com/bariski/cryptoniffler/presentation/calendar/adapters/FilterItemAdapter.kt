package com.bariski.cryptoniffler.presentation.calendar.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.FilterItem
import java.util.HashSet
import kotlin.collections.ArrayList

class FilterItemAdapter(private val data: List<FilterItem>, private var selected: Set<FilterItem>) : RecyclerView.Adapter<FilterItemAdapter.ViewHolder>() {

    private val filteredData = ArrayList(data)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_filter, parent, false), selected)
    }

    fun filterDataSet(s: String) {
        filteredData.clear()
        if (s.isNotEmpty()) {
            data.filterTo(filteredData) { it.getDisplayTitle().toLowerCase().contains(s) }
        } else {
            filteredData.addAll(data)

        }
        notifyDataSetChanged()
    }

    fun getSelected(): Set<FilterItem> {
        return selected
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setData(filteredData[position], selected.contains(filteredData[position]))
    }

    class ViewHolder(view: View, val selected: Set<FilterItem>) : RecyclerView.ViewHolder(view) {
        val item: CheckBox = view as CheckBox
        fun setData(s: FilterItem, isChecked: Boolean) {
            item.text = s.getDisplayTitle()
            item.tag = s
            item.isChecked = isChecked
            item.setOnCheckedChangeListener({ button: CompoundButton, _ ->
                if (button.isChecked) {
                    (selected as HashSet).add(button.tag as FilterItem)
                } else {
                    (selected as HashSet).remove(button.tag as FilterItem)
                }
            })
        }
    }

    override fun getItemCount() = filteredData.size
}