package com.wei.sniffer.cache

import java.io.Serializable
import java.net.URL

/**
 * v1.0 of the file created on 2019-11-27 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 保存请求的所有信息
 */
data class SnifferLog(
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
        @JvmField var method: String,

        /**
         * 请求状态
         */
        @JvmField var statusCode: String,

        /**
         * 请求信息
         */
        @JvmField var request: SnifferRequest,

        /**
         * 响应信息
         */
        @JvmField var response: SnifferResponse
) : Serializable


enum class BodyType {
    JSON,
    TEXT,
    FORM,
    IMAGE,
    FILE,
    UNKNOWN
}

fun getBodyTypeByContentType(contentType: String?): BodyType {
    if (contentType != null) {
        if (contentType.contains("application/json")) {
            return BodyType.JSON
        } else if (contentType.contains("text/")) {
            return BodyType.TEXT
        }
    }
    return BodyType.UNKNOWN
}