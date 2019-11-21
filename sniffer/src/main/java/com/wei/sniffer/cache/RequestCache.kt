package com.wei.sniffer.cache

import android.os.Environment
import java.io.File
import java.io.Serializable
import java.net.URL
import java.util.*

/**
 * v1.0 of the file created on 2019-11-12 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 缓存请求
 */
object RequestCache {
    var onSniffListener: OnSniffListener? = null
    val requestKeys: Vector<String> = Vector()
    val requestValues: Vector<SnifferRequestLog> = Vector()

    fun put(key: String, value: SnifferRequestLog) {
        if (requestKeys.size > 9) {
            val file = File(getPath(), "${requestKeys[0]}.log")
            file.deleteOnExit()
            requestKeys.removeAt(0)
            requestValues.removeAt(0)
        }
        requestKeys.add(key)
        requestValues.add(value)
        onSniffListener?.onSniff(value)
    }
}

class SnifferRequestLog : Serializable {
    var id: String = ""
    var url: URL? = null

    var request: SnifferRequest? = null
    var response: SnifferResponse? = null
}

class SnifferRequest : Serializable {
    var url: String? = null
    override fun toString(): String {
        return "SnifferRequest(url=$url)"
    }
}

class SnifferResponse : Serializable {
    var textBody = StringBuilder()
    override fun toString(): String {
        return "SnifferResponse(textBody=$textBody)"
    }
}

interface OnSniffListener {
    fun onSniff(snifferRequestLog: SnifferRequestLog)
}

fun getPath(): String {
    return Environment.getDataDirectory().absolutePath + "/sniffer"
}