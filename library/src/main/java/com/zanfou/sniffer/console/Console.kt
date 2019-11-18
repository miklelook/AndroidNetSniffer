package com.zanfou.sniffer.console


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.zanfou.sniffer.R
import com.zanfou.sniffer.cache.OnSniffListener
import com.zanfou.sniffer.cache.RequestCache
import com.zanfou.sniffer.cache.SnifferRequestLog
import kotlin.math.abs


/**
 * v1.0 of the file created on 2019-11-18 by shuxin.wei, email: weishuxin@maoyan.com
 * description: todo
 */
@SuppressLint("StaticFieldLeak", "ClickableViewAccessibility", "SetTextI18n", "InflateParams")
object Console {
    private var isShowing = false
    private var contentView: View? = null
    private var windowManager: WindowManager? = null
    private val handler = Handler(Looper.getMainLooper())
    private val layoutParams = WindowManager.LayoutParams()
    fun showConsole(context: Application) {
        if (isShowing) {
            return
        }
        RequestCache.onSniffListener = object : OnSniffListener {
            override fun onSniff(snifferRequestLog: SnifferRequestLog) {
                handler.post {
                    updateConsole()
                }
            }
        }

        // 获取WindowManager服务
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // 设置LayoutParam
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, context.resources.displayMetrics).toInt()
        layoutParams.format = PixelFormat.TRANSLUCENT// 支持透明
        // mParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = layoutParams.flags.or(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { /*android7.0不能用TYPE_TOAST*/
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
            if (PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", context.packageName)) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
            }
        }
        layoutParams.gravity = Gravity.TOP
        layoutParams.x = 0
        layoutParams.y = 0
        contentView = getContentView(context)
        contentView?.setOnTouchListener(object : View.OnTouchListener {
            private var x: Int = 0
            private var y: Int = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = event.rawX.toInt()
                        y = event.rawY.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val nowX = event.rawX.toInt()
                        val nowY = event.rawY.toInt()
                        val movedY = nowY - y
                        x = nowX
                        y = nowY
                        layoutParams.y = layoutParams.y + movedY

                        // 更新悬浮窗控件布局
                        windowManager?.updateViewLayout(view, layoutParams)
                    }
                    else -> {
                    }
                }
                return false
            }
        })
        // 将悬浮窗控件添加到WindowManager
        windowManager?.addView(contentView, layoutParams)
        isShowing = true
    }

    private var listView: ListView? = null
    private fun getContentView(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val contentView = inflater.inflate(R.layout.layout_console, null)
        val detailView = contentView.findViewById<FrameLayout>(R.id.fl_detail)

        listView = contentView.findViewById(R.id.list_view)
        contentView.findViewById<TextView>(R.id.tv_close).setOnClickListener {
            hideConsole()
        }

        listView?.isStackFromBottom = true
        listView?.adapter = RequestListViewAdapter(AdapterView.OnItemClickListener { parent, view, position, id ->
            val snifferRequestLog = RequestCache.requestValues[position]
            detailView.removeAllViews()
            detailView.addView(inflater.inflate(R.layout.adapter_text_view, null), ViewGroup.LayoutParams(-1, -1))
            detailView.findViewById<TextView>(R.id.tv_detail).text = "RequestId:${snifferRequestLog.id}\n\nUrl:${snifferRequestLog.url.toString()}\n\n${snifferRequestLog.response?.textBody.toString()}"
        })
        return contentView
    }

    private fun updateConsole() {
        (listView?.adapter as BaseAdapter).notifyDataSetChanged()
    }

    private fun hideConsole() {
        handler.removeCallbacksAndMessages(null)
        contentView?.let {
            windowManager?.removeView(it)
            contentView = null
        }
        isShowing = false
    }
}

@SuppressLint("SetTextI18n", "InflateParams")
class RequestListViewAdapter @JvmOverloads constructor(var onItemClickListener: AdapterView.OnItemClickListener? = null) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view === null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_request, null, false)
        }
        val item = getItem(position)
        view?.findViewById<TextView>(R.id.tv_title)?.text = "${position}-${item.id}-${item.url?.toString()}"

        var upX = 0f
        var downX = 0f
        view?.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                downX = event.x
                upX = 0f
            } else if (event.actionMasked == MotionEvent.ACTION_UP && abs(upX - downX) < 5f) {
                onItemClickListener?.onItemClick(v.parent as AdapterView<*>?, v, position, getItemId(position))
                return@setOnTouchListener true
            } else if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                upX = event.x
            }
            return@setOnTouchListener false
        }
        return view!!
    }

    override fun getItem(position: Int): SnifferRequestLog {
        return RequestCache.requestValues[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return RequestCache.requestKeys.size
    }
}