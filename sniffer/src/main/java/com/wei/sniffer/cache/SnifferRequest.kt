package com.wei.sniffer.cache

import java.io.Serializable
import java.net.URL

/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求信息
 */
data class SnifferRequest(
        /**
         * 请求ID
         */
        @JvmField var id: String,

        /**
         * 请求地址
         */
        @JvmField var url: URL,

        /**
         * 请求状态
         */
        @JvmField var requestState: RequestState,

        /**
         * 响应头
         */
        @JvmField var headers: Map<String, List<String>>? = null

) : Serializable


/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求状态码
 */
enum class RequestState(var desc: String) {
    /**
     * 请求加载中
     */
    REQUEST_STATE_LOADING("Loading..."),
    /**
     * 请求完成
     */
    REQUEST_STATE_FINISHED("Finish..."),
}