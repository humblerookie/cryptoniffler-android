package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.domain.model.VolumeInfo
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import com.bariski.cryptoniffler.presentation.main.adapters.VolumeMonitorAdapter
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.segment_filter.view.*
import javax.inject.Inject


class VolumeMonitorFragment : BaseInjectFragment(), VolumeView {


    lateinit var container: View
    @Inject
    lateinit var presenter: VolumePresenter

    val adapter = VolumeMonitorAdapter()

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_volume_monitor, parent, false)
        container = view
        presenter.initView(this, arguments, null)
        view.list.apply {
            adapter = this@VolumeMonitorFragment.adapter
            list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchData()
    }

    override fun displayInfo(info: VolumeInfo) {
        if (isAlive()) {
            adapter.setData(info.binance)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    companion object {

        fun getInstance(info: VolumeInfo?): Fragment {
            val frag = VolumeMonitorFragment()
            val b = Bundle()
            info?.let { b.putParcelable("info", it) }
            frag.arguments = b
            return frag
        }
    }

}