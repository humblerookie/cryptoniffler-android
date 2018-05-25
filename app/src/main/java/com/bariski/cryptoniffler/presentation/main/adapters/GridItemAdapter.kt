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
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import java.util.*
import javax.inject.Inject

class GridItemAdapter @Inject constructor(private val imageRepository: ImageLoader) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            holder.let { (it as ViewHolder).setData(items[position - 1]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_text, parent, false), imageRepository, listener)
        } else {
            TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false), title)
        }
    }


    private var items: ArrayList<GridItem> = ArrayList()
    var title: String = ""
    lateinit var listener: OnItemClickListener

    fun setData(items: ArrayList<GridItem>) {
        this.items = items
    }

    class ViewHolder(view: View, private val imageRepository: ImageLoader, itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.itemIcon)
        val name: TextView = view.findViewById(R.id.itemName)
        lateinit var item: GridItem

        init {
            view.setOnClickListener { itemClickListener.onClick(item) }
        }

        fun setData(item: GridItem) {
            this.item = item
            name.text = item.getItemName()
            if (item.getItemImage() != null) {
                imageRepository.loadImage(ImageRequest(icon, R.drawable.placeholder, item.getItemImage().toString(), null, name.context as Activity, R.drawable.select_all, false))
            } else {
                imageRepository.loadImage(ImageRequest(icon, R.drawable.placeholder, "", null, name.context as Activity, R.drawable.select_all, false))
            }

        }

    }

    class TextViewHolder(val view: View, title: String) : RecyclerView.ViewHolder(view) {
        init {
            (view as TextView).text = title
        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else {
            0
        }
    }

    override fun getItemCount() = items.size + 1

    interface OnItemClickListener {
        fun onClick(item: GridItem)
    }


}