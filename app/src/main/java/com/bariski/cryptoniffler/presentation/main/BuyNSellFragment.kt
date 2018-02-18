package com.bariski.cryptoniffler.presentation.main

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.common.BaseFragment

class BuyNSellFragment : BaseFragment(), View.OnClickListener {

    lateinit var presenter: MainPresenter
    override fun onClick(v: View?) {
        v?.let {
            presenter.onItemClicked(it.id)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_buynsell, container, false)
        view.findViewById<View>(R.id.buyAndSellCrypto).setOnClickListener(this)
        view.findViewById<View>(R.id.viewExchanges).setOnClickListener(this)

        return view
    }

    companion object {

        fun getInstance(mainPresenter: MainPresenter): Fragment {
            val frag = BuyNSellFragment()
            frag.presenter = mainPresenter
            return frag
        }
    }

}