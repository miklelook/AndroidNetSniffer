package com.planb.sniffer.console.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.planb.sniffer.R
import com.planb.sniffer.cache.BaseSnifferDetail
import com.planb.sniffer.console.Console

class UnknownBodyAdapter(snifferLog: BaseSnifferDetail?) : Console.BaseConsoleAdapter(snifferLog) {
    override fun onCreateView(parent: View): View {
        val textView = TextView(parent.context)
        textView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        textView.gravity = Gravity.CENTER
        textView.textSize = 16f
        textView.setTextColor(parent.context.resources.getColor(R.color.sniffer_console_text))
        return textView
    }

    override fun onBindView(view: View, baseSnifferDetail: BaseSnifferDetail?) {
        (view as TextView).text = "暂不支持"
    }
}