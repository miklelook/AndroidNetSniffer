package com.wei.sniffer.cache

import java.net.URL

/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求信息
 */
data class SnifferRequest(

        @JvmField override var id: String,

        @JvmField override var url: URL,

        /**
         * 请求状态
         */
        @JvmField var requestState: RequestState,

        /**
         * 响应头
         */
        @JvmField var headers: Map<String, List<String>>? = null,

        /**
         * 请求体类型
         */
        @JvmField var bodyType: BodyType? = null,

        /**
         * 请求响应体
         */
        @JvmField var body: String = "",

        /**
         * 格式化后的响应体
         */
        @JvmField var formatBody: String = "",

        /**
         * 请求体大小
         */
        @JvmField var contentLength: Long = -1L

) : BaseSnifferDetail(id, url)


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