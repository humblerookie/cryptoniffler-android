package com.bariski.cryptoniffler.presentation.main

import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.common.BaseFragment
import com.bariski.cryptoniffler.presentation.common.models.AmountInput
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_amount.*


class AmountFragment : BaseFragment() {


    var presenter: MainPresenter? = null
    lateinit var inrInput: EditText
    lateinit var btcEquivalent: TextView
    lateinit var next: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_amount, container, false)
        inrInput = view.findViewById(R.id.inrAmount)
        btcEquivalent = view.findViewById(R.id.btcEquivalent)
        next = view.findViewById(R.id.next)
        view.findViewById<View>(R.id.help).setOnClickListener({
            presenter?.infoClicked()
            showInfo()
        })
        view.findViewById<CheckBox>(R.id.includeFees).setOnCheckedChangeListener({ _, b -> presenter?.onIncludeFeeChanged(b) })

        next.setOnClickListener {
            val s = inrInput.text.toString()
            if (!s.isEmpty() && s.toLong() >= 1000) {
                presenter?.onNext()
            } else {
                Toast.makeText(activity, R.string.error_input_minamount, Toast.LENGTH_SHORT).show()
            }

        }
        if (!arguments.getBoolean("showFees")) {
            includeFees.visibility = View.GONE
        }
        inrInput.setOnEditorActionListener({ _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                next.performClick()
                true
            }
            false
        })
        presenter?.onAmountScreenCreated(RxTextView.textChanges(inrInput).map({
            if (it.toString().isNotEmpty()) {
                next.alpha = if (it.toString().toLong() >= 1000) 1.0f else 0.5f
                it
            } else {
                "0"
            }
        }), getObserver())
        inrInput.requestFocus()
        return view
    }

    private fun getObserver(): Observer<AmountInput> {
        return object : Observer<AmountInput> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: AmountInput) {
                if (isAlive()) {
                    btcEquivalent.text = Html.fromHtml(getString(R.string.common_label_equivalent_amount, t.btcAmount))
                }
            }

            override fun onError(e: Throwable) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.onAmountScreenDestroyed()
    }

    override fun onResume() {
        super.onResume()
        if (isAlive()) {
            val imgr: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        presenter?.onAmountScreenRefresh()
    }

    override fun onPause() {
        super.onPause()
        if (isAlive()) {
            val view = activity.currentFocus
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    companion object {

        fun getInstance(mainPresenter: MainPresenter, showFees: Boolean): Fragment {
            val frag = AmountFragment()
            frag.presenter = mainPresenter
            val b = Bundle()
            b.putBoolean("showFees", showFees)
            frag.arguments = b
            return frag
        }
    }

    private fun showInfo() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog_Alert)
        builder.setTitle(getString(R.string.common_label_fee_info))
                .setMessage(getString(R.string.common_info_fee_info))
                .setPositiveButton(android.R.string.ok, { dialog, _ ->
                    dialog.dismiss()
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

}