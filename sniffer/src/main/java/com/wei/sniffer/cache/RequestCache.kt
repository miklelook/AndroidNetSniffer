package com.wei.sniffer.cache

import android.os.Environment
import android.util.Log
import java.io.File
import java.util.*
import java.util.logging.Logger


/**
 * v1.0 of the file created on 2019-11-12 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 缓存请求
 */
object RequestCache {
    var onSniffListener: OnSniffListener? = null
    val requestKeys: Vector<String> = Vector()
    val VALUES: Vector<SnifferLog> = Vector()

    fun put(key: String, value: SnifferLog) {
        if (requestKeys.size > 9) {
            //删除多余的文件
            val file = File(getPath(), "${requestKeys[0]}.log")
            file.deleteOnExit()
            // TODO: 2019-11-28 创建新的缓存文件
            requestKeys.removeAt(0)
            VALUES.removeAt(0)
        }
        requestKeys.add(key)
        VALUES.add(value)
        onSniffListener?.onNotifyDataChanged(value)
    }
}

/**
 * Sniffer监听器，包含大部分监听回调
 */
interface OnSniffListener {
    /**
     * 数据更新通知
     */
    fun onNotifyDataChanged(snifferLog: SnifferLog)
}

/**
 * 缓存地址
 */
fun getPath(): String {
    val path = Environment.getDataDirectory().absolutePath + "/sniffer"
    Log.i("shuxin.wei", path)
    return path
}