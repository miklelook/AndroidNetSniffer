package com.wei.sniffer.console

import android.view.View
import com.wei.sniffer.cache.SnifferLog

/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求控制台展示适配器
 */
abstract class BaseConsoleAdapter(
        /**
         * 请求信息
         */
        var snifferLog: SnifferLog
) {

    private lateinit var contentView: View

    /**
     *请求ID
     */
    abstract fun getId(): String

    /**
     * 请求地址
     */
    abstract fun getUrl(): String

    /**
     * 请求HttpCode
     */
    abstract fun getHttpCode(): String

    /**
     * 请求状态
     */
    abstract fun getRequestState(): String

    /**
     * 请求体
     */
    abstract fun getRequestBody(): String

    /**
     * 请求大小
     */
    abstract fun getContentLength(): String

    /**
     * 创建展示View
     */
    abstract fun onCreateView(): View

    /**
     * 通知View刷新
     */
    fun notifyDataChanged() {

    }

    private fun createView(): View {
        contentView = onCreateView()
        return contentView
    }
}