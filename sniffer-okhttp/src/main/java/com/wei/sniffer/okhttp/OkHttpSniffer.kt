package com.wei.sniffer.okhttp

import com.wei.sniffer.cache.*
import com.wei.sniffer.utils.JsonUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class OkHttpSniffer : Interceptor {
    var startNs: Long? = null
    override fun intercept(chain: Interceptor.Chain): Response {


        // 处理请求
        val request = chain.request()
        startNs = System.nanoTime()
        val snifferLog = setRequestData(request)
        val response = chain.proceed(request)
        setResponseData(snifferLog, response)
        return response
    }

    private fun setResponseData(snifferLog: SnifferLog, response: Response?) {

        if (response != null) {
            val responseBody = response.body!!
            val headers = response.headers
            snifferLog.response.contentLength = responseBody.contentLength()
            snifferLog.response.code = response.code
            snifferLog.statusCode = response.code.toString()
            snifferLog.response.message = if (response.message.isEmpty()) "" else ' ' + response.message
            snifferLog.response.duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - (startNs
                    ?: 0L))
            snifferLog.response.scheme = snifferLog.url.protocol
            snifferLog.response.headers = HashMap(response.headers.toMultimap())
            snifferLog.response.bodyType = getBodyTypeByContentType(response.header("Content-Type", null))


            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            var gzippedLength: Long? = null
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8)
                    ?: StandardCharsets.UTF_8

            if (!buffer.isProbablyUtf8()) {
                //出现Error
                snifferLog.response.headers?.put("body-size", Collections.singletonList("${buffer.size}-byte body omitted"))
                return
            }

            if (snifferLog.response.contentLength != 0L) {
                snifferLog.response.body = buffer.clone().readString(charset)
            }

            if (gzippedLength != null) {
                snifferLog.response.headers?.put("body-size", Collections.singletonList("${buffer.size}-byte, $gzippedLength-gzipped-byte body"))
            } else {
                snifferLog.response.headers?.put("body-size", Collections.singletonList("${buffer.size}-byte"))
            }

            if (snifferLog.response.bodyType == BodyType.JSON) {
                snifferLog.response.formatBody = JsonUtils.formatJsonSafe(snifferLog.response.body, JsonUtils.JsonStringBuilder.Step.TWO_SPACES)
            } else {
                snifferLog.response.formatBody = snifferLog.response.body
            }

            //请求结束
            snifferLog.request.requestState = RequestState.REQUEST_STATE_FINISHED
            //通知数据更新
            RequestCache.onSniffListener?.onNotifyDataChanged(snifferLog)
        }
    }

    private fun setRequestData(request: Request): SnifferLog {
        val id = request.hashCode().toString()
        val url = request.url.toUrl()
        val snifferLog = SnifferLog(
                id,
                url,
                request.method,
                "-1",
                SnifferRequest(id, url, RequestState.REQUEST_STATE_LOADING),
                SnifferResponse(id, url))

        val snifferRequest = snifferLog.request
        snifferRequest.headers = request.headers.toMultimap()
        if (snifferLog.method.equals("GET", true)
                || snifferLog.method.equals("DELETE", true)) {
            snifferRequest.bodyType = BodyType.TEXT
            snifferRequest.body = url.query ?: ""
            snifferRequest.formatBody = url.query?.replace('&', '\n') ?: ""
        } else if (snifferLog.method.equals("POST", true)
                || snifferLog.method.equals("PUT", true)) {
            snifferRequest.bodyType = getBodyTypeByContentType(request.header("Content-Type"))
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            snifferRequest.body = buffer.readUtf8()
            if (snifferRequest.bodyType == BodyType.JSON) {
                snifferRequest.formatBody = JsonUtils.formatJsonSafe(snifferRequest.body, JsonUtils.JsonStringBuilder.Step.TWO_SPACES)
            } else {
                snifferRequest.formatBody = snifferRequest.body
            }
        }
        RequestCache.put(snifferLog.id, snifferLog)
        RequestCache.onSniffListener?.onNotifyDataChanged(snifferLog)
        return snifferLog
    }
}