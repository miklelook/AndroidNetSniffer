package com.planb.sniffer.console.view

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.planb.sniffer.R
import com.planb.sniffer.cache.SnifferLog

class HeadersView : LinearLayout {
    private val keyValueFormat = "%s: %s"
    private var cacheItem: HashSet<TextView> = HashSet()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        id = R.id.sniffer_id_headers
        orientation = VERTICAL
        val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics).toInt()
        setPadding(padding, 0, padding, padding)
    }

    fun notifyDataChanged(snifferLog: SnifferLog?) {
        for (index in 0 until childCount) {
            val childAt = getChildAt(index)
            if (childAt.tag === "HEADER_ITEM") {
                cacheItem.add(childAt as TextView)
            }
        }
        removeAllViews()
        snifferLog?.let {
            addGeneralHeader(snifferLog)
            addRequestHeader(snifferLog)
            addResponseHeader(snifferLog)
        }
    }

    private fun addGeneralHeader(snifferLog: SnifferLog) {
        val title = TextView(context)
        title.setTextAppearance(context, R.style.SnifferTabItem)
        title.typeface = Typeface.DEFAULT_BOLD
        title.text = "General"
        addView(title, LayoutParams(-2, -2))

        addHeaderItem("id", snifferLog.id + " " + snifferLog.request.requestState.desc)
        addHeaderItem("url", snifferLog.url.toString())
        addHeaderItem("method", snifferLog.method)
        addHeaderItem("status", snifferLog.statusCode)
    }

    private fun addRequestHeader(snifferLog: SnifferLog) {
        val title = TextView(context)
        title.setTextAppearance(context, R.style.SnifferTabItem)
        title.typeface = Typeface.DEFAULT_BOLD
        title.text = "Request"
        addView(title, LayoutParams(-2, -2))
        snifferLog.request.headers?.forEach {
            addHeaderItem(it.key, it.value.toString().removePrefix("[").removeSuffix("]"))
        }
    }

    private fun addResponseHeader(snifferLog: SnifferLog) {
        val title = TextView(context)
        title.setTextAppearance(context, R.style.SnifferTabItem)
        title.typeface = Typeface.DEFAULT_BOLD
        title.text = "Response"
        addView(title, LayoutParams(-2, -2))
        snifferLog.response.headers?.forEach {
            addHeaderItem(it.key, it.value.toString().removePrefix("[").removeSuffix("]"))
        }
    }


    private fun addHeaderItem(key: String, value: String) {
        val item: TextView
        if (cacheItem.size > 0) {
            item = cacheItem.first()
            cacheItem.remove(item)
        } else {
            item = EditText(context)
            item.setSingleLine()
            item.tag = "HEADER_ITEM"
            item.setTextIsSelectable(true)
            item.inputType = InputType.TYPE_NULL
            item.background = resources.getDrawable(R.drawable.sniffer_edittext_bg)
            item.setPadding(0, 0, 0, 0)
            item.setTextAppearance(context, R.style.SnifferTabItem)
        }
        item.text = String.format(keyValueFormat, key, value)
        addView(item, MarginLayoutParams(-1, -2).apply {
            leftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics).toInt()
        })
    }
}