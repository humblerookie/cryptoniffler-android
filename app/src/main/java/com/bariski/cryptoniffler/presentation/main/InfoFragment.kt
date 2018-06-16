package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import kotlinx.android.synthetic.main.fragment_info.view.*
import javax.inject.Inject


class InfoFragment : BaseInjectFragment(), InfoView {


    lateinit var container: View
    @Inject
    lateinit var presenter: InfoPresenter

    val clickListener = View.OnClickListener { view ->
        presenter.onDonateClicked(view.id)
    }


    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_info, parent, false)
        container = view
        presenter.initView(this, arguments, null)
        container.btcTip.setOnClickListener(clickListener)
        container.ltcTip.setOnClickListener(clickListener)
        container.dogeTip.setOnClickListener(clickListener)
        container.xrpTagTip.setOnClickListener(clickListener)
        container.xrpTip.setOnClickListener(clickListener)
        return view
    }

    override fun displayInfo(info: Info) {
        if (isAlive()) {
            container.exchangesValue.text = Html.fromHtml(info.exchangeInfo)
            container.featuresValue.text = Html.fromHtml(info.upcomingFeatures)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    companion object {

        fun getInstance(info: Info?): Fragment {
            val frag = InfoFragment()
            val b = Bundle()
            info?.let { b.putParcelable("info", it) }
            frag.arguments = b
            return frag
        }
    }

    override fun showAddressCopiedMessage() {
        if (isAlive()) {
            Toast.makeText(activity, R.string.info_donation_address_copied, Toast.LENGTH_SHORT).show()
        }
    }

}