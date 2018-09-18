package com.bariski.cryptoniffler.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.Keep
import com.bariski.cryptoniffler.presentation.calendar.models.CalendarItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.lang.StringBuilder
import java.text.SimpleDateFormat

@Keep
@Parcelize
@SuppressLint("ParcelCreator", "SimpleDateFormat")
data class Event(@Json(name = "id") val id: String,
                 @Json(name = "title") val eventTitle: String?,
                 @Json(name = "coins") val coins: List<CalendarCoin>?,
                 @Json(name = "date_event") val date: String,
                 @Json(name = "created_date") val created: String,
                 @Json(name = "description") val desc: String?,
                 @Json(name = "proof") val proof: String?,
                 @Json(name = "source") val source: String?,
                 @Json(name = "is_hot") val isHot: Boolean,
                 @Json(name = "vote_count") val voteCount: Int,
                 @Json(name = "positive_vote_count") val positiveCount: Int,
                 @Json(name = "percentage") val percentage: Float,
                 @Json(name = "categories") val categories: List<CalendarCategory>,
                 @Json(name = "can_occur_before") val canOccurPrior: Boolean) : CalendarItem, Parcelable {

    override fun getTitle(): String {
        val sb = StringBuilder()
        if (coins != null && coins.isNotEmpty()) {
            var i = 0
            for (c in coins) {
                i++
                sb.append(c.name)
                if (i != coins.size) {
                    sb.append(", ")
                }
            }
        }
        return sb.toString()
    }

    override fun getTime(): String {
        return dateFormatOutput.format(dateFormatInput.parse(date))
    }

    override fun getSubTitle(): String? {
        return eventTitle
    }

    override fun getDescription(): String? {
        return desc
    }

    override fun getCount(): Int {
        return voteCount
    }

    override fun getPercentage(): Int {
        return percentage.toInt()
    }

    override fun getUrl() = source

    override fun getEventTimeInMillis() = dateFormatInput.parse(date).time

    companion object {

        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")
        val dateFormatOutput = SimpleDateFormat("dd MMM yyyy")
    }

}