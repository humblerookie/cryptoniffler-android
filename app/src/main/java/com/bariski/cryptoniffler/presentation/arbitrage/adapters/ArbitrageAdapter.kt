package com.bariski.cryptoniffler.presentation.arbitrage.adapters

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Arbitrage
import com.bariski.cryptoniffler.domain.model.DirectArbitrage
import com.bariski.cryptoniffler.domain.model.IntraArbitrage
import com.bariski.cryptoniffler.domain.model.TriangleArbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.util.ArbitrageMode
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import kotlinx.android.synthetic.main.item_intraexchange.view.*
import kotlinx.android.synthetic.main.item_label_arbitrage.view.*

class ArbitrageAdapter(val arbitrage: Arbitrage, val mode: Int, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> DisclaimerViewHolder(inflater.inflate(R.layout.item_disclaimer, parent, false), presenter)
            1 -> TitleViewHolder(inflater.inflate(R.layout.item_label_arbitrage, parent, false))
            2 -> DirectViewHolder(inflater.inflate(R.layout.item_list, parent, false), mode == ArbitrageMode.INTERNATIONAL, imageLoader, presenter)
            3 -> TriangleViewHolder(inflater.inflate(R.layout.item_list, parent, false), imageLoader, presenter)
            else -> IntraExchangeViewHolder(inflater.inflate(R.layout.item_intraexchange, parent, false), imageLoader, presenter)
        }
    }

    override fun getItemCount(): Int = when (mode) {
        ArbitrageMode.INDIAN -> 5
        ArbitrageMode.INTERNATIONAL -> 3
        else -> 1 + arbitrage.intraExchange.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mode != ArbitrageMode.INTRA_EXCHANGE) {
            when (position) {
                0 -> 0
                1, 3 -> 1
                2 -> 2
                else -> 3
            }
        } else {
            when (position) {
                0 -> 0
                else -> 4
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> {
            }
            1 -> {
                val context = (holder as TitleViewHolder).label.context
                holder.bindData(context.getString(if (position == 1) R.string.arbitrage_label_direct else R.string.arbitrage_label_triangle))
            }
            2 -> (holder as DirectViewHolder).bindData(if (!presenter.isModeInternational()) arbitrage.direct else arbitrage.btcArbitrage)
            3 -> (holder as TriangleViewHolder).bindData(arbitrage.triangle)
            else -> (holder as IntraExchangeViewHolder).bindData(arbitrage.intraExchange[position - 1])
        }
    }

    private class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.label

        fun bindData(data: String) {
            label.text = data
        }
    }

    private class DisclaimerViewHolder(view: View, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
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

    private class IntraExchangeViewHolder(val view: View, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {

        lateinit var data: IntraArbitrage
        init {
            view.setOnClickListener {
                presenter.onIntraArbitrageClicked(data)
            }
        }

        fun bindData(data: IntraArbitrage) {
            this.data = data
            view.exchange.text = data.exchange
            view.coin.text = data.coin
            view.sourceMarket.text = view.context.getString(R.string.arbitrage_market_buy_placeholder, data.sourceMarket)
            view.destMarket.text = view.context.getString(R.string.arbitrage_market_sell_placeholder, data.destinationMarket)
            view.destRate.text = data.sell
            view.percentProfit.text = (if (data.profitPercentage > 1) data.profitPercentage.toInt().toString() else data.profitPercentage.toString()) + "%"
            view.sourceRate.text = data.buy
            view.icon.setImageResource(R.drawable.placeholder)
            data.coinImageUrl?.let {
                imageLoader.loadImage(ImageRequest(view.icon, R.drawable.placeholder, it, null, view.context as Activity, R.drawable.placeholder, false))
            }
        }
    }

}