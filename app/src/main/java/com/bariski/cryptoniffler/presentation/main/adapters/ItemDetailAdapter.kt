package com.bariski.cryptoniffler.presentation.main.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.GridItemDetail
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.DecimalFormat
import javax.inject.Inject

class ItemDetailAdapter @Inject constructor(private val loader: ImageLoader) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @JvmField
    val data = ArrayList<GridItemDetail>()

    val formatter = DecimalFormat("0.00")

    var showLoadingCards = false

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            TextViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_title, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(parent?.context).inflate(if (showLoadingCards) R.layout.item_detail_dummy else R.layout.item_detail_grid, parent, false))
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
            if (!showLoadingCards) {
                vH.setData(data[position - 2], loader, formatter)
            } else {
                vH.startShimmerAnimation()
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            showLoadingCards -> 4
            data.size == 0 -> 0
            else -> data.size + 2
        }
    }

    fun setItems(d: ArrayList<GridItemDetail>) {
        Log.d("ItemDetail", "Items Set:${d.size}")
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position > 1 -> if (showLoadingCards) 1 else 2
            else -> 0
        }
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
        @JvmField
        val shimmerFrameLayout: ShimmerFrameLayout? = view.findViewById(R.id.shimmerContainer)

        fun setData(d: GridItemDetail, loader: ImageLoader, formatter: DecimalFormat) {
            val context = name.context
            name.text = d.name
            price.text = context.getString(R.string.common_label_price, formatter.format(d.price))
            summary.text = d.summary
            loader.loadImage(ImageRequest(icon, R.drawable.placeholder, d.image
                    ?: "", null, context as Activity?, R.drawable.placeholder))

        }

        fun startShimmerAnimation() {
            shimmerFrameLayout?.startShimmerAnimation()
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