package com.bariski.cryptoniffler.presentation.arbitrage.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.DirectArbitrage
import com.bariski.cryptoniffler.domain.model.TriangleArbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import kotlinx.android.synthetic.main.item_label_arbitrage.view.*

class ArbitrageAdapter(val arbitrage: Arbitrage, val imageLoader: ImageLoader) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> TitleViewHolder(inflater.inflate(R.layout.item_label_arbitrage, parent, false))
            1 -> DirectViewHolder(inflater.inflate(R.layout.item_list, parent, false), imageLoader)
            else -> TriangleViewHolder(inflater.inflate(R.layout.item_list, parent, false), imageLoader)
        }
    }

    override fun getItemCount(): Int = 4

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> 0
            1 -> 1
            else -> 2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            0 -> {
                val context = (holder as TitleViewHolder).label.context
                holder.bindData(context.getString(if (position == 0) R.string.arbitrage_label_direct else R.string.arbitrage_label_triangle))
            }
            1 -> (holder as DirectViewHolder).bindData(arbitrage.direct)
            else -> (holder as TriangleViewHolder).bindData(arbitrage.triangle)
        }
    }

    private class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.label

        fun bindData(data: String) {
            label.text = data
        }
    }

    private class DirectViewHolder(view: View, val imageLoader: ImageLoader) : RecyclerView.ViewHolder(view) {
        val list: RecyclerView = view as RecyclerView

        fun bindData(data: List<DirectArbitrage>) {
            list.layoutManager = LinearLayoutManager(list.context, LinearLayoutManager.HORIZONTAL, false)
            list.adapter = DirectAdapter(data, imageLoader)
        }
    }

    private class TriangleViewHolder(view: View, val imageLoader: ImageLoader) : RecyclerView.ViewHolder(view) {
        val list: RecyclerView = view as RecyclerView

        fun bindData(data: List<TriangleArbitrage>) {
            list.layoutManager = LinearLayoutManager(list.context, LinearLayoutManager.HORIZONTAL, false)
            list.adapter = TriangleAdapter(data, imageLoader)
        }
    }

}