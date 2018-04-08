package com.bariski.cryptoniffler.presentation.main.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest

class SearchAdapter constructor(val loader: ImageLoader, private val itemClickListener: GridItemAdapter.OnItemClickListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    val data = ArrayList<Coin>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false), loader, itemClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.setData(data[position])
    }


    class ViewHolder(view: View, private val imageRepository: ImageLoader, itemClickListener: GridItemAdapter.OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
        val symbol: TextView = view.findViewById(R.id.symbol)
        lateinit var item: GridItem

        init {
            view.setOnClickListener { itemClickListener.onClick(item) }
        }

        fun setData(item: Coin) {
            this.item = item
            name.text = item.getItemName()
            symbol.text = item.symbol
            if (item.getItemImage() != null) {
                imageRepository.loadImage(ImageRequest(icon, R.drawable.placeholder, item.getItemImage().toString(), null, name.context as Activity, R.drawable.select_all, false))
            } else {
                imageRepository.loadImage(ImageRequest(icon, R.drawable.placeholder, "", null, name.context as Activity, R.drawable.select_all, false))
            }

        }

    }

    fun setData(data: List<Coin>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}