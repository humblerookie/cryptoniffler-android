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
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import kotlinx.android.synthetic.main.item_direct_arbitrage.view.*

class DirectAdapter(private val data: List<DirectArbitrage>, private val imageLoader: ImageLoader) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DirectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_direct_arbitrage, parent, false), imageLoader)
    }

    override fun getItemCount() = if (data.isEmpty()) 1 else data.size

    override fun getItemViewType(position: Int): Int {
        return if (data.isNotEmpty()) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as DirectViewHolder)?.bind(data[position])
        } else {
            (holder as TextHolder)?.bindData(holder.view.context.getString(R.string.error_arbitrage_empty))
        }
    }

    class DirectViewHolder(val view: View, val imageLoader: ImageLoader) : RecyclerView.ViewHolder(view) {
        val srcImage = view.srcExchange
        val destImage = view.destExchange
        val srcCoinImage = view.srcCoin
        val destCoinImage = view.destCoin
        val summarySource = view.srcText
        val summaryDest = view.destText
        val profit = view.profit
        val seed = view.amount
        val fees = view.fees

        fun bind(data: DirectArbitrage) {
            profit.text = data.amount.toInt().toString()
            fees.text = data.fees.toInt().toString()
            seed.text = data.seed.toInt().toString()

            data.coin.imageUrl?.let {
                imageLoader.loadImage(ImageRequest(srcCoinImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true))
                imageLoader.loadImage(ImageRequest(destCoinImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true))
            }
            data.from.imageUrl.let {
                imageLoader.loadImage(ImageRequest(srcImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true))
            }
            data.to.imageUrl.let {
                imageLoader.loadImage(ImageRequest(destImage, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true))
            }
            summarySource.text = Html.fromHtml(data.from.summary)
            summaryDest.text = Html.fromHtml(data.to.summary)


        }
    }

    private class TextHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(s: String) {
            (view as TextView).text = s
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    }
}