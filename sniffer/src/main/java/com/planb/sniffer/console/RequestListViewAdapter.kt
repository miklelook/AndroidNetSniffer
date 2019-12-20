package com.planb.sniffer.console

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.planb.sniffer.R
import com.planb.sniffer.cache.RequestStore
import com.planb.sniffer.cache.SnifferLog

/**
 * v1.0 of the file created on 2019-11-28 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求列表Adapter
 */
class RequestListViewAdapter @JvmOverloads constructor(private var onItemClickListener: AdapterView.OnItemClickListener? = null,
                                                       private var onItemLongClickListener: AdapterView.OnItemLongClickListener? = null) : BaseAdapter() {
    var temp: SnifferLog? = null

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view === null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.sniffer_adapter_item_request, null, false)
        }
        val item = getItem(position)
        view?.findViewById<TextView>(R.id.tv_title)?.let {
            it.setOnClickListener {
                onItemClickListener?.onItemClick(null, view, position, getItemId(position))
            }
            it.text = "${(RequestStore.totalCount) - position}-${item.id}-${item.url}"
            it.setTextColor(view.resources.getColor(if (item === temp) R.color.sniffer_background else R.color.sniffer_console_text))
        }
        view?.setBackgroundColor(if (item === temp) view.resources.getColor(R.color.sniffer_list_divider) else Color.TRANSPARENT)
        view?.findViewById<View>(R.id.iv_del)?.setOnClickListener {
            RequestStore.removeAt(count - 1 - position)
        }
        return view!!
    }

    override fun getItem(position: Int): SnifferLog {
        return RequestStore.get(RequestStore.size() - position - 1)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return RequestStore.size()
    }
}