package com.planb.sniffer.okhttp

import java.io.IOException

import okhttp3.Call
import okhttp3.Connection
import okhttp3.EventListener

class OnHttpEventListener : EventListener() {
    override fun callStart(call: Call) {
        super.callStart(call)
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)

    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
    }
}
