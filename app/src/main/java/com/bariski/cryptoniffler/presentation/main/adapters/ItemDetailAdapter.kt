package com.bariski.cryptoniffler.presentation.main.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.GridItemDetail
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import javax.inject.Inject

class ItemDetailAdapter @Inject constructor(val loader: ImageLoader) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @JvmField
    val data = ArrayList<GridItemDetail>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            TextViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_title, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_detail_grid, parent, false))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as TextViewHolder).setData(holder.name.context.getString(if (position == 0) R.string.common_label_best_buy else R.string.common_label_best_sell))
        } else {
            val vH = (holder as ViewHolder)
            val context = vH.container.context
            val layoutParams = vH.container.layoutParams as RecyclerView.LayoutParams
            layoutParams.leftMargin = context.resources.getDimension(if (position % 2 == 0) R.dimen.dp16 else R.dimen.dp8).toInt()
            layoutParams.rightMargin = context.resources.getDimension(if (position % 2 != 0) R.dimen.dp16 else R.dimen.dp8).toInt()
            layoutParams.bottomMargin = context.resources.getDimension(R.dimen.dp16).toInt()
            vH.setData(data[position - 2], loader)
        }
    }

    override fun getItemCount(): Int {
        return if (data.size == 0) 0 else data.size + 2
    }

    fun setItems(d: ArrayList<GridItemDetail>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 1) 1 else 0
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        val name: TextView = view.findViewById(R.id.itemName)
        @JvmField
        val price: TextView = view.findViewById(R.id.itemPrice)
        @JvmField
        val summary: TextView = view.findViewById(R.id.itemSummary)
        @JvmField
        val icon: ImageView = view.findViewById(R.id.itemIcon)
        @JvmField
        val container: View = view

        fun setData(d: GridItemDetail, loader: ImageLoader) {
            val context = name.context
            name.text = d.name
            price.text = context.getString(R.string.common_label_price, d.price)
            summary.text = d.summary
            d.image?.let { loader.loadImage(ImageRequest(icon, R.drawable.placeholder, it, null, context as Activity?, R.drawable.placeholder)) }

        }

    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        val name: TextView = view as TextView

        fun setData(s: String) {
            name.text = s
        }

    }
}