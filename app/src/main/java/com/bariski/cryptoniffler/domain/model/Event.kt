package com.bariski.cryptoniffler.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep
import com.bariski.cryptoniffler.presentation.calendar.models.CalendarItem
import com.squareup.moshi.Json
import java.lang.StringBuilder
import java.text.SimpleDateFormat

@Keep
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
                 @Json(name = "categories") val categories: List<String>,
                 @Json(name = "can_occur_before") val canOccurPrior: Boolean) : CalendarItem, Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(CalendarCoin),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.createStringArrayList(),
            parcel.readByte() != 0.toByte())

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(eventTitle)
        parcel.writeTypedList(coins)
        parcel.writeString(date)
        parcel.writeString(created)
        parcel.writeString(desc)
        parcel.writeString(proof)
        parcel.writeString(source)
        parcel.writeByte(if (isHot) 1 else 0)
        parcel.writeInt(voteCount)
        parcel.writeInt(positiveCount)
        parcel.writeFloat(percentage)
        parcel.writeStringList(categories)
        parcel.writeByte(if (canOccurPrior) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {

        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")
        val dateFormatOutput = SimpleDateFormat("dd MMM yyyy")

        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

}