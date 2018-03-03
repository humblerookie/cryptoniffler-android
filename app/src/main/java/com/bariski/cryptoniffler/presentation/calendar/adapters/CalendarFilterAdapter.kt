package com.bariski.cryptoniffler.presentation.calendar.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import com.bariski.cryptoniffler.R

class CalendarFilterAdapter(private val data: List<String>, private var selected: HashSet<String>) : RecyclerView.Adapter<CalendarFilterAdapter.ViewHolder>() {

    private val filteredData = ArrayList(data)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_filter, parent, false), selected)
    }

    fun filterDataSet(s: String) {
        filteredData.clear()
        if (s.isNotEmpty()) {
            data.filterTo(filteredData) { it.toLowerCase().contains(s) }
        } else {
            filteredData.addAll(data)

        }
        notifyDataSetChanged()
    }

    fun getSelected(): HashSet<String> {
        return selected
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setData(filteredData[position], selected.contains(filteredData[position]))
    }

    class ViewHolder(view: View, val selected: HashSet<String>) : RecyclerView.ViewHolder(view) {
        val item: CheckBox = view as CheckBox
        fun setData(s: String, isChecked: Boolean) {
            item.text = s
            item.tag = s
            item.isChecked = isChecked
            item.setOnCheckedChangeListener({ button: CompoundButton, _ ->
                if (button.isChecked) {
                    selected.add(button.tag.toString())
                } else {
                    selected.remove(button.tag.toString())
                }
            })
        }
    }

    override fun getItemCount() = filteredData.size
}