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
import com.bariski.cryptoniffler.domain.model.TriangleArbitrage
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.arbitrage.ArbitragePresenter
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import kotlinx.android.synthetic.main.item_triangle_timeline.view.*

class TriangleAdapter(val data: List<TriangleArbitrage>, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            TriangleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_triangle_timeline, parent, false), imageLoader, presenter)
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
            (holder as TriangleViewHolder).bind(data[position])
        } else {
            (holder as TextHolder).bindData(holder.view.context.getString(R.string.error_arbitrage_empty))
        }
    }

    class TriangleViewHolder(val view: View, val imageLoader: ImageLoader, val presenter: ArbitragePresenter) : RecyclerView.ViewHolder(view) {
        val summary1 = view.summary1
        val summary2 = view.summary2
        val summary3 = view.summary3
        val ex1 = view.ex1
        val ex2 = view.ex2
        val ex3 = view.ex3
        val coin1 = view.coin1
        val coin2_1 = view.coin2_1
        val coin2_2 = view.coin2_2
        val coin3 = view.coin3
        val profit = view.profit
        val seed = view.amount
        val fees = view.fees
        lateinit var data: TriangleArbitrage
        val res = view.context.resources
        val bigIconSize = ((res.getDimension(R.dimen.width_exchange) - 2 * res.getDimension(R.dimen.dp1)) / 2).toInt()
        val smallIconSize = ((res.getDimension(R.dimen.dp20) - 2 * res.getDimension(R.dimen.dp1)) / 2).toInt()

        init {
            view.setOnClickListener {
                presenter.onTriangleArbitrageClick(data)
            }
        }

        fun bind(data: TriangleArbitrage) {
            this.data = data
            val a1 = data.actions[0]
            val a2 = data.actions[1]
            val a3 = data.actions[2]
            summary1.text = Html.fromHtml(a1.summary)
            summary2.text = Html.fromHtml(a2.summary)
            summary3.text = Html.fromHtml(a3.summary)
            a1.mainImage?.let { imageLoader.loadImage(ImageRequest(ex1, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, bigIconSize)) }
            a2.mainImage?.let { imageLoader.loadImage(ImageRequest(ex2, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, bigIconSize)) }
            a3.mainImage?.let { imageLoader.loadImage(ImageRequest(ex3, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, bigIconSize)) }
            a1.rightImage?.let { imageLoader.loadImage(ImageRequest(coin1, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize)) }
            a2.rightImage?.let { imageLoader.loadImage(ImageRequest(coin2_2, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize)) }
            a3.rightImage?.let { imageLoader.loadImage(ImageRequest(coin3, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize)) }
            if (a2.leftImage == null) {
                coin2_1.visibility = View.GONE
            } else {
                coin2_1.visibility = View.VISIBLE
                a2.leftImage?.let { imageLoader.loadImage(ImageRequest(coin2_1, R.drawable.placeholder, it, null, profit.context as Activity, R.drawable.placeholder, true, smallIconSize)) }
            }

            profit.text = data.amount.toInt().toString()
            fees.text = data.fees.toInt().toString()
            seed.text = data.seed.toInt().toString()
        }


    }

    private class TextHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(s: String) {
            (view as TextView).text = s
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    }
}