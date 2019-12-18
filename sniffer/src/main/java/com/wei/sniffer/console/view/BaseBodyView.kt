package com.wei.sniffer.console.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.wei.sniffer.console.Console

open class BaseBodyView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    var baseConsoleAdapter: Console.BaseConsoleAdapter? = null
        set(value) {
            field = value
            removeAllViews()
            baseConsoleAdapter?.let {
                val contentView = it.createView(this)
                contentView.layoutParams = LayoutParams(-1, -1)
                addView(contentView)
                it.notifyDataChanged()
            }
        }
}