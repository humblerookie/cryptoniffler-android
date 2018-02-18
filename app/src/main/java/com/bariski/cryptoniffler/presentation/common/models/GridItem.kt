package com.bariski.cryptoniffler.presentation.common.models

import android.support.annotation.Keep

@Keep
interface GridItem {

    fun getItemName(): String
    fun getItemCode(): String?
    fun getItemImage(): String?

}