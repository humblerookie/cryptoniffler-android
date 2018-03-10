package com.bariski.cryptoniffler.presentation.calendar.models

import android.support.annotation.Keep

@Keep
interface CalendarItem {

    fun getTitle(): String

    fun getTime(): String

    fun getSubTitle(): String?

    fun getDescription(): String?

    fun getCount(): Int

    fun getPercentage(): Int

    fun getUrl():String?

    fun getEventTimeInMillis():Long
}