package com.bariski.cryptoniffler.presentation.arbitrage.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.DirectArbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.bariski.cryptoniffler.presentation.common.utils.PERCENTAGE
import kotlinx.android.synthetic.main.item_direct_arbitrage.view.*

class DirectAdapter(private val data: List<DirectArbitrage>, val isInternational: Boolean, private val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            DirectViewHolder(LayoutInflater.from(parent.context).inflate(if (isInternational) R.layout.item_direct_btc_arbitrage else R.layout.item_direct_arbitrage, parent, false), imageLoader, presenter)
        } else {
            TextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false))
        }
    }

    override fun getItemCount() = if (data.isEmpty()) 1 else data.size

    override fun getItemViewType(position: Int): Int {
        return if (data.isNotEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as DirectViewHolder).bind(data[position], !isInternational)
        } else {
            (holder as TextHolder).bindData(holder.view.context.getString(R.string.error_arbitrage_empty))
        }
    }

    class DirectViewHolder(val view: View, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
        val srcImage = view.srcExchange
        val destImage = view.destExchange
        val srcCoinImage = view.srcCoin
        val destCoinImage = view.destCoin
        val summarySource = view.srcText
        val summaryDest = view.destText
        val profit = view.profit
        val seed = view.amount
        val fees = view.fees
        val res = view.context.resources
        val bigIconSize = ((res.getDimension(R.dimen.width_exchange) - 2 * res.getDimension(R.dimen.dp1)) / 2).toInt()
        val smallIconSize = ((res.getDimension(R.dimen.dp20) - 2 * res.getDimension(R.dimen.dp1)) / 2).toInt()
        val profitPercent: TextView? = view.findViewById(R.id.percentProfit)
        lateinit var data: DirectArbitrage

        init {
            view.setOnClickListener {
                presenter.onDirectArbitrageClick(data)
            }
        }

        fun bind(data: DirectArbitrage, roundOff: Boolean) {
            this.data = data
            profit.text = getString(data.amount, roundOff)
            fees.text = getString(data.fees, roundOff)
            seed.text = getString(data.seed, roundOff)
            profitPercent?.text = data.profit.toInt().toString() + PERCENTAGE
            data.coin.imageUrl?.let {
                imageLoader.loadImage(ImageRequest(srcCoinImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize))
                imageLoader.loadImage(ImageRequest(destCoinImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize))
            }
            data.from.imageUrl.let {
                imageLoader.loadImage(ImageRequest(srcImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, bigIconSize))
            }
            data.to.imageUrl.let {
                imageLoader.loadImage(ImageRequest(destImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, bigIconSize))
            }
            summarySource.text = Html.fromHtml(data.from.summary)
            summaryDest.text = Html.fromHtml(data.to.summary)
        }

        fun getString(f: Float, roundOff: Boolean): String {
            return if (roundOff) {
                f.toInt().toString()
            } else {
                var s = "%.8f".format(f)
                if (s.indexOf(".") < 0) {
                    s
                } else {
                    var significantDigitReached = false
                    val sb = StringBuilder()
                    s.reversed().forEach {
                        if (it != '0' || significantDigitReached) {
                            significantDigitReached = true
                            sb.append(it)
                        }
                    }
                    sb.toString().reversed()
                }
            }
        }
    }

    private class TextHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(s: String) {
            (view as TextView).text = s
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    }
}