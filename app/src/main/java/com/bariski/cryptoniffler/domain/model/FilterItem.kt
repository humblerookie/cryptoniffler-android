package com.bariski.cryptoniffler.domain.model

import android.os.Parcelable

interface FilterItem :Parcelable{

    fun getDisplayTitle(): String

    fun getIdentifier(): String
}