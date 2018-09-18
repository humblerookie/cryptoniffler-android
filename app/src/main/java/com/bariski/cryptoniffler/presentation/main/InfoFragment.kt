package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.presentation.common.BaseInjectFragment
import kotlinx.android.synthetic.main.fragment_info.view.*
import javax.inject.Inject


class InfoFragment : BaseInjectFragment(), InfoView {


    lateinit var container: View
    @Inject
    lateinit var presenter: InfoPresenter

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_info, parent, false)
        container = view
        container.labelPrivacy.movementMethod = LinkMovementMethod.getInstance()
        presenter.initView(this, arguments, null)
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

}