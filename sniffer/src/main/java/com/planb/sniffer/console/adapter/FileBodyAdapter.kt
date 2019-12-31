package com.planb.sniffer.console.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.planb.sniffer.R
import com.planb.sniffer.cache.BaseSnifferDetail
import com.planb.sniffer.cache.SnifferRequest
import com.planb.sniffer.cache.SnifferResponse
import com.planb.sniffer.console.Console
import com.planb.sniffer.console.view.setClipListener

class FileBodyAdapter(baseSnifferDetail: BaseSnifferDetail?) : Console.BaseConsoleAdapter(baseSnifferDetail) {
    override fun onCreateView(parent: View): View {
        val textView = TextView(parent.context)
        textView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        textView.textSize = 11f
        textView.tag = "tv_content"
        textView.setTextColor(parent.context.resources.getColor(R.color.sniffer_console_text))
        val contentView = FrameLayout(parent.context)
        contentView.addView(textView)
        contentView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        return contentView
    }

    override fun onBindView(view: View, baseSnifferDetail: BaseSnifferDetail?) {
        val textView = view.findViewWithTag<TextView>("tv_content")
        textView.setClipListener()
        baseSnifferDetail?.let {
            if (baseSnifferDetail is SnifferResponse) {
                textView.text = baseSnifferDetail.body
            } else if (baseSnifferDetail is SnifferRequest) {
                textView.text = baseSnifferDetail.body
            }
            setViewScroll(textView, view)
        }
    }
}