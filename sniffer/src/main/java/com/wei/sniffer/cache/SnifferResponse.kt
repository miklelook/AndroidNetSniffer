package com.wei.sniffer.cache

import java.io.Serializable
import java.net.URL


/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求响应信息
 */
data class SnifferResponse(
        /**
         * 请求ID
         */
        @JvmField var id: String,

        /**
         * 请求地址
         */
        @JvmField var url: URL,

        /**
         * 请求类型
         */
        @JvmField var scheme: String = "",

        /**
         * 请求响应体
         */
        @JvmField var body: StringBuilder = StringBuilder(),

        /**
         * 请求体大小
         */
        @JvmField var contentLength: Long = -1L,

        /**
         * 请求花费的时间
         */
        @JvmField var duration: Long = -1L,

        /**
         * 请求状态码
         */
        @JvmField var code: Int = -1,

        /**
         * 请求状态信息
         */
        @JvmField var message: String = "",

        /**
         * 响应头
         */
        @JvmField var headers: Map<String, List<String>>? = null

) : Serializable {

    /**
     * 获取响应体大小描述文案，将[contentLength]格式化成字符串返回
     */
    fun getContentLengthDesc(): String {
        return if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
    }
}