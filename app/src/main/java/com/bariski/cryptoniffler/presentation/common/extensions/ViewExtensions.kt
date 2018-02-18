package com.bariski.cryptoniffler.presentation.common.extensions

import android.view.View


fun View.makeVisible(vis: Boolean) = {
    this.visibility = if (vis) View.VISIBLE else View.GONE
}

fun View.makeInvisible() = { this.visibility = View.INVISIBLE }