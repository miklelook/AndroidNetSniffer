package com.wei.sniffer.console.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import com.wei.sniffer.R
import com.wei.sniffer.cache.SnifferLog
import com.wei.sniffer.console.Console

@SuppressLint("ClickableViewAccessibility")
class JsonBodyAdapter(snifferLog: SnifferLog?) : Console.BaseConsoleAdapter(snifferLog) {
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

    override fun onBindView(view: View, snifferLog: SnifferLog?) {
        val textView = view.findViewWithTag<TextView>("tv_content")
        if (snifferLog != null) {
            if (isFormatBody) {
                if (textView.parent == view) {
                    (view as ViewGroup).removeView(textView)
                    val scrollView = HorizontalScrollView(view.context)
                    scrollView.addView(textView)
                    view.addView(scrollView, -1, -1)
                }
                textView.text = snifferLog.response.formatBody
            } else {
                if (textView.parent != view) {
                    (textView.parent as ViewGroup).removeView(textView)
                    (view as ViewGroup).addView(textView, -1, -1)
                }
                textView.text = snifferLog.response.body
            }
        } else {
            textView.text = ""
        }
    }
}