package com.wei.sniffer.console.view

import android.content.Context
import android.util.AttributeSet
import com.wei.sniffer.R

class RequestView : BaseBodyView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        id = R.id.sniffer_id_request
    }
}