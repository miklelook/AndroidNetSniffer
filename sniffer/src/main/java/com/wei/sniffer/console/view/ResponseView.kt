package com.wei.sniffer.console.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wei.sniffer.R
import com.wei.sniffer.cache.SnifferLog
import com.wei.sniffer.console.Console

class ResponseView : BaseBodyView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        id = R.id.sniffer_id_response
    }

}