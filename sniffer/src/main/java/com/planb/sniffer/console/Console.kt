package com.planb.sniffer.console


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.*
import com.planb.sniffer.R
import com.planb.sniffer.cache.*
import com.planb.sniffer.console.adapter.JsonBodyAdapter
import com.planb.sniffer.console.adapter.TextBodyAdapter
import com.planb.sniffer.console.adapter.UnknownBodyAdapter
import com.planb.sniffer.console.view.BaseBodyView
import com.planb.sniffer.console.view.HeadersView
import com.planb.sniffer.console.view.RequestView
import com.planb.sniffer.console.view.ResponseView


/**
 * v1.0 of the file created on 2019-11-18 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 网络请求Sniffer控制台展示
 */
@SuppressLint("StaticFieldLeak", "ClickableViewAccessibility", "SetTextI18n", "InflateParams")
class Console {
    companion object {
        val instance: Console by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Console()
        }
    }

    private var isOpened = false
    private var isShowing = false
    private var rootView: View? = null
    private lateinit var windowManager: WindowManager
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var listViewAdapter: RequestListViewAdapter
    private lateinit var layoutParams: WindowManager.LayoutParams
    private var currentTabIndex = -1
    private var aliveActivityCount = 0
    private var context: Application? = null
    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
            aliveActivityCount++
            if (aliveActivityCount >= 0 && isOpened && !isShowing) {
                showConsole()
                isShowing = true
            }
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            aliveActivityCount--
            if (aliveActivityCount <= 0 && isOpened && isShowing) {
                hideConsole()
                isShowing = false
            }
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }

    /**
     * 开启Console
     */
    fun openConsole(context: Application) {
        if (isOpened) {
            return
        }
        this.context = context
        context.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        RequestStore.onSniffListener = object : OnSniffListener {
            override fun onNotifyDataChanged(snifferLog: SnifferLog?, isRemove: Boolean) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    updateConsole(snifferLog, isRemove)
                }, 200)
            }
        }
        rootView = createConsoleView(context)

        rootView?.let {
            selectTab(it, currentTabIndex, null)
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            layoutParams = windowManager.initDragWindow(it, it.findViewById(R.id.rl_header))
        }
        isOpened = true
        showConsole()
    }

    private fun createConsoleView(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val rootView = inflater.inflate(R.layout.sniffer_layout_console, null)
        val listView = rootView.findViewById<ListView>(R.id.list_view)
        rootView.findViewById<View>(R.id.tv_close).setOnClickListener {
            closeConsole()
        }
        rootView.findViewById<View>(R.id.iv_clear).setOnClickListener {
            RequestStore.clear()
        }
        listView?.isStackFromBottom = true
        listViewAdapter = RequestListViewAdapter(AdapterView.OnItemClickListener { _, _, position, _ ->
            val snifferLog = listViewAdapter.getItem(position)
            if (snifferLog !== listViewAdapter.temp) {
                listViewAdapter.temp = snifferLog
                resetConsoleDetailView()
                rootView?.let { selectTab(it, 0, snifferLog) }
                listViewAdapter.notifyDataSetChanged()
            }
        })

        listView?.adapter = listViewAdapter
        return rootView
    }

    /**
     * 重置控制台详情展示区
     */
    private fun resetConsoleDetailView() {
        rootView?.let {
            val contentView = it.findViewById<FrameLayout>(R.id.fl_content)
            contentView.removeAllViews()
            val context = contentView.context
            //add Headers View
            contentView.addView(HeadersView(context), ViewGroup.LayoutParams(-1, -1))

            //add Request View
            contentView.addView(RequestView(context), ViewGroup.LayoutParams(-1, -1))

            //add Response View
            contentView.addView(ResponseView(context), ViewGroup.LayoutParams(-1, -1))
        }
    }

    /**
     * 更新Console
     */
    private fun updateConsole(snifferLog: SnifferLog?, remove: Boolean) {
        listViewAdapter.notifyDataSetChanged()
        //已有选中，则更新详细内容
        rootView?.let { selectTab(it, if (remove) -1 else currentTabIndex, snifferLog) }
    }

    /**
     * 关闭Console
     */
    private fun closeConsole() {
        handler.removeCallbacksAndMessages(null)
        context?.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)
        hideConsole()
        clear()
        isOpened = false
    }

    /**
     * 隐藏Console
     */
    private fun hideConsole() {
        rootView?.let {
            windowManager.hideView(it)
        }
        isShowing = false
    }

    /**
     * 显示Console
     */
    private fun showConsole() {
        windowManager.showView(rootView, layoutParams)
        isShowing = true
    }

    /**
     * Tab选择
     */
    private fun selectTab(rootView: View, selectIndex: Int, snifferLog: SnifferLog?) {
        val contentView = rootView.findViewById<FrameLayout>(R.id.fl_content)
        val headersView = contentView.findViewById<HeadersView>(R.id.sniffer_id_headers)
        val requestView = contentView.findViewById<RequestView>(R.id.sniffer_id_request)
        val responseView = contentView.findViewById<View>(R.id.sniffer_id_response) as? ResponseView

        headersView?.notifyDataChanged(snifferLog)

        when (selectIndex) {
            1 -> {
                requestView?.setBodyAdapter(snifferLog?.request?.bodyType, snifferLog?.request)
            }
            2 -> {
                responseView?.setBodyAdapter(snifferLog?.response?.bodyType, snifferLog?.response)
            }
        }

        headersView?.visibility = if (selectIndex == 0) View.VISIBLE else View.GONE
        requestView?.visibility = if (selectIndex == 1) View.VISIBLE else View.GONE
        responseView?.visibility = if (selectIndex == 2) View.VISIBLE else View.GONE
        currentTabIndex = selectIndex
        val tabLayout = rootView.findViewById<LinearLayout>(R.id.ll_tab)
        for (index in 0 until tabLayout.childCount) {
            val childAt = tabLayout.getChildAt(index) as TextView
            if (index == selectIndex) {
                childAt.setBackgroundColor(tabLayout.context.resources.getColor(R.color.sniffer_list_divider))
                childAt.setTextColor(tabLayout.context.resources.getColor(R.color.sniffer_background))
            } else {
                childAt.background = null
                childAt.setTextColor(tabLayout.context.resources.getColor(R.color.sniffer_console_text))
            }

            childAt.setOnClickListener(object : View.OnClickListener {
                var lastTime = 0L
                override fun onClick(v: View?) {
                    if (index == currentTabIndex) {
                        if (index == 1 && System.currentTimeMillis() - lastTime < ViewConfiguration.getDoubleTapTimeout()) {
                            requestView?.baseConsoleAdapter?.isFormatBody = !(requestView?.baseConsoleAdapter?.isFormatBody
                                    ?: false)
                            requestView?.baseConsoleAdapter?.notifyDataChanged()
                            lastTime = 0
                        } else if (index == 2 && System.currentTimeMillis() - lastTime < ViewConfiguration.getDoubleTapTimeout()) {
                            responseView?.baseConsoleAdapter?.isFormatBody = !(responseView?.baseConsoleAdapter?.isFormatBody
                                    ?: false)
                            responseView?.baseConsoleAdapter?.notifyDataChanged()
                            lastTime = 0
                        } else {
                            lastTime = System.currentTimeMillis()
                        }
                    } else {
                        selectTab(rootView, index, snifferLog)
                    }
                }
            })
        }
    }

    private fun BaseBodyView.setBodyAdapter(bodyType: BodyType?, snifferLog: BaseSnifferDetail?) {
        baseConsoleAdapter = when (bodyType) {
            BodyType.JSON -> JsonBodyAdapter(snifferLog)
            BodyType.TEXT -> TextBodyAdapter(snifferLog)
            else -> UnknownBodyAdapter(snifferLog)
        }
    }

    /**
     * 清除数据
     */
    private fun clear() {
        rootView = null
        context = null
    }

    /**
     * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
     * description: 请求控制台展示适配器
     */
    abstract class BaseConsoleAdapter(private var snifferDetail: BaseSnifferDetail? = null) {

        private lateinit var contentView: View
        var isFormatBody = true

        /**
         * 创建展示View
         */
        abstract fun onCreateView(parent: View): View

        /**
         * 数据绑定
         */
        abstract fun onBindView(view: View, baseSnifferDetail: BaseSnifferDetail?)

        /**
         * 通知View刷新
         */
        fun notifyDataChanged() {
            onBindView(contentView, snifferDetail)
        }

        fun createView(parent: View): View {
            contentView = onCreateView(parent)
            return contentView
        }

        protected fun setViewScroll(view: View, parentView: View) {
            if (isFormatBody) {
                if (view.parent == parentView) {
                    (parentView as ViewGroup).removeView(view)
                    val scrollView = HorizontalScrollView(parentView.context)
                    scrollView.addView(view)
                    parentView.addView(scrollView, -1, -1)
                }
            } else {
                if (view.parent != parentView) {
                    (view.parent as ViewGroup).removeView(view)
                    (parentView as ViewGroup).addView(view, -1, -1)
                }
            }
        }
    }
}