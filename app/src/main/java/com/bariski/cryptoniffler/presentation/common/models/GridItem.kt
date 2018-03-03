package com.bariski.cryptoniffler.presentation.common.models

import android.os.Parcelable
import android.support.annotation.Keep

@Keep
interface GridItem : Parcelable {

    fun getItemName(): String
    fun getItemCode(): String?
    fun getItemImage(): String?

}