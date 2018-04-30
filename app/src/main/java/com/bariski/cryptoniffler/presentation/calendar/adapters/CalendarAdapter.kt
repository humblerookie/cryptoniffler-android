package com.bariski.cryptoniffler.presentation.calendar.adapters

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.presentation.calendar.models.CalendarItem
import com.crashlytics.android.Crashlytics
import java.util.*


class CalendarAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<CalendarItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false))
        } else {
            CreditsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_credits, parent, false))
        }
    }

    override fun getItemViewType(position: Int) = if (position != itemCount - 1) 0 else 1
    override fun getItemCount() = if (data.size > 0) data.size + 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position != itemCount - 1) {
            holder?.let { (it as ViewHolder).setData(data[position]) }
        }
    }

    fun setData(items: List<CalendarItem>, isAppend: Boolean) {

        if (isAppend) {
            val index = data.size
            data.addAll(items)
            notifyItemRangeInserted(index, items.size)
        } else {
            data.clear()
            data.addAll(items)
            notifyDataSetChanged()
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        val coinTitle: TextView = view.findViewById(R.id.coinTitle)
        @JvmField
        val subtitle: TextView = view.findViewById(R.id.event)
        @JvmField
        val time: TextView = view.findViewById(R.id.calendar)
        @JvmField
        val desc: TextView = view.findViewById(R.id.coinDescription)
        @JvmField
        val votes: TextView = view.findViewById(R.id.votes)
        @JvmField
        val confidenceText: TextView = view.findViewById(R.id.progressText)
        @JvmField
        val confidence: ProgressBar = view.findViewById(R.id.progress)

        var item: CalendarItem? = null

        init {
            view.setOnClickListener {
                val context = it.context
                item?.apply {
                    val calendarIcon = view.findViewById<View>(R.id.addReminder)
                    if (calendarIcon is ImageView) {
                        (calendarIcon?.parent as View).post({
                            val delegateArea = Rect()
                            val myButton = calendarIcon
                            myButton.getHitRect(delegateArea)
                            delegateArea.top -= 100
                            delegateArea.bottom += 100
                            val touchDelegate = TouchDelegate(delegateArea,
                                    myButton)
                            if (View::class.java.isInstance(myButton.parent)) {
                                (myButton.parent as View).touchDelegate = touchDelegate
                            }
                        })
                        val calendarHeader = view.findViewById<View>(R.id.calendarHeader)
                        (calendarHeader?.parent as View).post({
                            val delegateArea = Rect()
                            val myButton = calendarHeader
                            myButton.getHitRect(delegateArea)
                            delegateArea.top -= 100
                            delegateArea.bottom += 100
                            val touchDelegate = TouchDelegate(delegateArea,
                                    myButton)
                            if (View::class.java.isInstance(myButton.parent)) {
                                (myButton.parent as View).touchDelegate = touchDelegate
                            }
                        })
                    }
                    val listener = View.OnClickListener { view ->
                        val intent = Intent(Intent.ACTION_EDIT)
                        intent.type = "vnd.android.cursor.item/event"
                        intent.putExtra("beginTime", getEventTimeInMillis())
                        intent.putExtra("allDay", true)
                        intent.putExtra("title", getTitle() + " : " + getSubTitle())
                        view.context.startActivity(intent)
                    }
                    calendarIcon.setOnClickListener(listener)
                    time.setOnClickListener(listener)

                    getUrl()?.let {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(it)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Log.e("Calendar Url", e.toString())
                            Crashlytics.logException(e)
                        }
                    }
                }

            }


        }

        fun setData(item: CalendarItem) {
            this.item = item
            val context = coinTitle.context
            coinTitle.text = item.getTitle()
            desc.text = item.getDescription()
            time.text = item.getTime()
            subtitle.text = item.getSubTitle()
            votes.text = context.getString(R.string.calendar_suffix_votes, item.getCount().toString())

            val p = item.getPercentage()
            confidenceText.text = context.getString(R.string.calendar_suffix_percentage, p.toString())
            confidence.progress = p
            confidence.progressDrawable = ContextCompat.getDrawable(context, when {
                p >= 70 -> R.drawable.green_progress
                p <= 30 -> R.drawable.red_progress
                else -> R.drawable.yellow_progress
            })
            confidenceText.setTextColor(ContextCompat.getColor(context, when {
                p >= 70 -> R.color.white_87
                p <= 30 -> R.color.black_87
                else -> R.color.black_87
            }))


        }

    }

    class CreditsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}