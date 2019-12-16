package com.wei.sniffer.console.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wei.sniffer.R
import com.wei.sniffer.cache.SnifferLog
import com.wei.sniffer.console.Console

class ResponseView : FrameLayout {

    var baseConsoleAdapter: Console.BaseConsoleAdapter? = null
        set(value) {
            field = value
            removeAllViews()
            baseConsoleAdapter?.let {
                val contentView = it.createView(this)
                contentView.layoutParams = ViewGroup.LayoutParams(-1, -1)
                addView(contentView)
                it.notifyDataChanged()
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        id = R.id.sniffer_id_response
    }

}