package com.bariski.cryptoniffler.presentation.arbitrage.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.DirectArbitrage
import com.bariski.cryptoniffler.domain.model.TriangleArbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import kotlinx.android.synthetic.main.item_label_arbitrage.view.*

class ArbitrageAdapter(val arbitrage: Arbitrage, val isInternational: Boolean, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> DisclaimerViewHolder(inflater.inflate(R.layout.item_disclaimer, parent, false), presenter)
            1 -> TitleViewHolder(inflater.inflate(R.layout.item_label_arbitrage, parent, false))
            2 -> DirectViewHolder(inflater.inflate(R.layout.item_list, parent, false), isInternational, imageLoader, presenter)
            else -> TriangleViewHolder(inflater.inflate(R.layout.item_list, parent, false), imageLoader, presenter)
        }
    }

    override fun getItemCount(): Int = if (isInternational) 3 else 5

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            1, 3 -> 1
            2 -> 2
            else -> 3
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            0 -> {
                (holder as DisclaimerViewHolder).bindData(presenter.isModeInternational())
            }
            1 -> {
                val context = (holder as TitleViewHolder).label.context
                holder.bindData(context.getString(if (position == 1) R.string.arbitrage_label_direct else R.string.arbitrage_label_triangle))
            }
            2 -> (holder as DirectViewHolder).bindData(if (!presenter.isModeInternational()) arbitrage.direct else arbitrage.btcArbitrage)
            else -> (holder as TriangleViewHolder).bindData(arbitrage.triangle)
        }
    }

    private class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.label

        fun bindData(data: String) {
            label.text = data
        }
    }

    private class DisclaimerViewHolder(view: View, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
        val indian: TextView = view.findViewById(R.id.indian)
        val international: TextView = view.findViewById(R.id.international)
        var isSelected = false
        var selectedView: View? = null
        val listener: View.OnClickListener = View.OnClickListener { view ->
            if (!isSelected && (selectedView == null || view != selectedView)) {
                isSelected = true
                changeSelectedUi(view)
                presenter.onModeChanged(view == international)
                view.postDelayed({
                    isSelected = false
                }, 300)
            }
        }

        init {
            indian.setOnClickListener(listener)
            international.setOnClickListener(listener)
        }

        private fun changeSelectedUi(view: View) {
            var selected = indian
            var unSelected = international
            if (view == international) {
                selected = international
                unSelected = indian
            }
            val context = selected.context
            selected.setBackgroundColor(ContextCompat.getColor(context, R.color.gold_900))
            selected.setTextColor(ContextCompat.getColor(context, R.color.white_87))
            unSelected.setBackgroundColor(ContextCompat.getColor(context, R.color.black_24))
            unSelected.setTextColor(ContextCompat.getColor(context, R.color.white_38))

        }

        fun bindData(isInternational: Boolean) {
            val view = if (isInternational) international else indian
            changeSelectedUi(view)
            selectedView = view
        }
    }

    private class DirectViewHolder(view: View, val isInternational: Boolean, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
        val list: RecyclerView = view as RecyclerView

        fun bindData(data: List<DirectArbitrage>) {
            val controller = AnimationUtils.loadLayoutAnimation(list.context, R.anim.layout_animation_fall_right)
            list.layoutAnimation = controller
            list.layoutManager = LinearLayoutManager(list.context, LinearLayoutManager.HORIZONTAL, false)
            list.adapter = DirectAdapter(data, isInternational, imageLoader, presenter)
            list.scheduleLayoutAnimation()
        }
    }

    private class TriangleViewHolder(view: View, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
        val list: RecyclerView = view as RecyclerView

        fun bindData(data: List<TriangleArbitrage>) {
            val controller = AnimationUtils.loadLayoutAnimation(list.context, R.anim.layout_animation_fall_right)
            list.layoutAnimation = controller
            list.layoutManager = LinearLayoutManager(list.context, LinearLayoutManager.HORIZONTAL, false)
            list.adapter = TriangleAdapter(data, imageLoader, presenter)
            list.scheduleLayoutAnimation()
        }
    }

}