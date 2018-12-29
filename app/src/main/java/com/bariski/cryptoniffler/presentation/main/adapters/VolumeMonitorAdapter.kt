package com.bariski.cryptoniffler.presentation.main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.VolumeInfo
import com.bariski.cryptoniffler.domain.model.VolumeItem
import com.bariski.cryptoniffler.presentation.common.extensions.color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.item_volume.view.*

class VolumeMonitorAdapter() : RecyclerView.Adapter<ViewHolder>() {

    val data = mutableListOf<VolumeItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_volume, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    fun setData(items: List<VolumeItem>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }
}

class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    lateinit var item: VolumeItem
    val chart = v.chart
    val symbol = v.coinName

    init {
        chart.setDrawBorders(true)
        chart.setBorderColor(chart.context.color(R.color.white_87))
        chart.setBackgroundColor(chart.context.color(R.color.white_87))
    }
    fun bindData(item: VolumeItem) {
        this.item = item
        initViews()
    }

    private fun initViews() {
        symbol.text = item.symbol
        val entries = ArrayList<Entry>()

        for ((index, data) in item.volumes.iterator().withIndex()) {
            if (index % 2 == 0) {
                entries.add(Entry(index.toFloat(), data.toFloat()))
            }
        }

        val context = chart.context
        val dataSet = LineDataSet(entries, context.getString(R.string.volume_title))
        dataSet.setDrawValues(false)
        dataSet.color = context.color(R.color.colorAccent)
        dataSet.valueTextColor = context.color(R.color.colorPrimary)
        chart.description = Description()
        chart.description.text = ""
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.notifyDataSetChanged()
    }
}