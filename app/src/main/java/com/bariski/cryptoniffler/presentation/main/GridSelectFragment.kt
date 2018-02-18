package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.common.BaseFragment
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter

class GridSelectFragment : BaseFragment() {

    lateinit var presenter: MainPresenter
    lateinit var list: RecyclerView
    var adapter: GridItemAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_gridselect, container, false)
        list = view.findViewById(R.id.list)
        val layoutManager = GridLayoutManager(activity, 3)
        list.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 3
                    else -> 1
                }
            }
        }
        list.adapter = adapter
        return view
    }

    companion object {

        fun getInstance(mainPresenter: MainPresenter, adapter: GridItemAdapter, data: ArrayList<GridItem>, title: String?): Fragment {
            val frag = GridSelectFragment()
            frag.presenter = mainPresenter
            frag.adapter = adapter
            frag.adapter?.listener = mainPresenter
            frag.adapter?.setData(data)
            title?.let { frag.adapter?.title = it }
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }
}