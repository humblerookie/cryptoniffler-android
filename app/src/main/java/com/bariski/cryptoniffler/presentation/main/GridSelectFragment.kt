package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.common.models.GridItem
import com.bariski.cryptoniffler.presentation.main.adapters.GridItemAdapter
import javax.inject.Inject

class GridSelectFragment : BaseInjectFragment() {

    lateinit var presenter: MainPresenter
    lateinit var list: RecyclerView

    @Inject
    lateinit var adapter: GridItemAdapter


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


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainView) {
            presenter = (activity as MainView).getCommonPresenter()
            adapter.title = arguments.getString("title")
            if (arguments.getInt("type") == 0) {
                adapter.setData((arguments.getParcelableArrayList<Coin>("data") as java.util.ArrayList<GridItem>))
            } else {
                adapter.setData((arguments.getParcelableArrayList<Exchange>("data") as java.util.ArrayList<GridItem>))
            }
            adapter.listener = presenter
            list.adapter = adapter

        } else {
            throw RuntimeException("Parent activity needs to override MainView")
        }
    }

    companion object {

        fun getInstance(data: ArrayList<GridItem>, title: String?, type: Int): Fragment {
            val frag = GridSelectFragment()
            val b = Bundle()
            b.putString("title", title)
            b.putInt("type", type)
            b.putParcelableArrayList("data", data)
            frag.arguments = b
            return frag
        }
    }
}