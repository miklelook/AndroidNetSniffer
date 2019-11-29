package com.wei.sniffer.console

import android.graphics.Color
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.wei.sniffer.R
import com.wei.sniffer.cache.RequestCache
import com.wei.sniffer.cache.SnifferLog

/**
 * v1.0 of the file created on 2019-11-28 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求列表Adapter
 */
class RequestListViewAdapter @JvmOverloads constructor(private var onItemClickListener: AdapterView.OnItemClickListener? = null) : BaseAdapter() {
    var temp: SnifferLog? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view === null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.sniffer_adapter_item_request, null, false)
        }
        val item = getItem(position)
        view?.findViewById<TextView>(R.id.tv_title)?.apply {
            text = "${position}-${item.id}-${item.url?.toString()}"
            setTextColor(view.resources.getColor(if (item === temp) R.color.sniffer_background else R.color.sniffer_console_text))
        }
        view?.setBackgroundColor(if (item === temp) view.resources.getColor(R.color.sniffer_list_divider) else Color.TRANSPARENT)


        val gestureDetector = GestureDetector(view?.context, object : GestureDetector.OnGestureListener {
            override fun onShowPress(e: MotionEvent?) {

            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                onItemClickListener?.onItemClick(view?.parent as AdapterView<*>?, view, position, getItemId(position))
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return false
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return false
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent?) {
            }

        })

        view?.setOnTouchListener { _, event ->
            return@setOnTouchListener gestureDetector.onTouchEvent(event)
        }
        return view!!
    }

    override fun getItem(position: Int): SnifferLog {
        return RequestCache.VALUES[RequestCache.VALUES.size - position - 1]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return RequestCache.requestKeys.size
    }
}