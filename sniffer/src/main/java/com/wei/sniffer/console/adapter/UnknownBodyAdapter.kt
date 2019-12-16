package com.wei.sniffer.console.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wei.sniffer.R
import com.wei.sniffer.cache.SnifferLog
import com.wei.sniffer.console.Console

class UnknownBodyAdapter(snifferLog: SnifferLog?) : Console.BaseConsoleAdapter(snifferLog) {
    override fun onCreateView(parent: View): View {
        val textView = TextView(parent.context)
        textView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        textView.gravity = Gravity.CENTER
        textView.textSize = 16f
        textView.setTextColor(parent.context.resources.getColor(R.color.sniffer_console_text))
        return textView
    }

    override fun onBindView(view: View, snifferLog: SnifferLog?) {
        (view as TextView).text = "暂不支持"
    }

}