package com.planb.sniffer.cache

import android.os.Environment
import android.util.Log
import com.planb.sniffer.SnifferConfig
import java.io.File
import java.util.*


/**
 * v1.0 of the file created on 2019-11-12 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 缓存请求
 */
object RequestStore {
    var onSniffListener: OnSniffListener? = null
    var totalCount = 0
    private val KEYS: Vector<String> = Vector()
    private val VALUES: Vector<SnifferLog> = Vector()

    fun put(key: String, value: SnifferLog) {
        if (KEYS.size >= SnifferConfig.CACHE_REQUEST_SIZE - 1) {
            //删除多余的文件
            val file = File(getPath(), "${KEYS[0]}.log")
            file.deleteOnExit()
            // TODO: 2019-11-28 创建新的缓存文件
            removeAt(0)
        }
        totalCount++
        KEYS.add(key)
        VALUES.add(value)
        onSniffListener?.onNotifyDataChanged(value, false)
    }

    fun size(): Int {
        return KEYS.size
    }

    fun get(index: Int): SnifferLog {
        return VALUES[index]
    }

    fun removeAt(index: Int) {
        KEYS.removeAt(index)
        onSniffListener?.onNotifyDataChanged(VALUES.removeAt(index), true)
    }

    fun clear() {
        totalCount = 0
        KEYS.clear()
        VALUES.clear()
        onSniffListener?.onNotifyDataChanged(null, true)
    }
}

/**
 * Sniffer监听器，包含大部分监听回调
 */
interface OnSniffListener {
    /**
     * 数据更新通知
     */
    fun onNotifyDataChanged(snifferLog: SnifferLog?, isRemove: Boolean)
}

/**
 * 缓存地址
 */
fun getPath(): String {
    val path = Environment.getDataDirectory().absolutePath + "/sniffer"
    Log.i("shuxin.wei", path)
    return path
}