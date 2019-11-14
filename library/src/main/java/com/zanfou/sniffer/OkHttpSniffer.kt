package com.zanfou.sniffer

import com.zanfou.sniffer.cache.RequestCache
import com.zanfou.sniffer.cache.SnifferRequest
import com.zanfou.sniffer.cache.SnifferRequestLog
import com.zanfou.sniffer.cache.SnifferResponse
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class OkHttpSniffer : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val snifferRequestLog = SnifferRequestLog()
        val snifferRequest = SnifferRequest()
        val snifferResponse = SnifferResponse()
        snifferRequestLog.request = snifferRequest
        snifferRequestLog.response = snifferResponse

        // 处理请求
        val request = chain.request()

        snifferRequestLog.id = request.hashCode().toString()
        snifferRequestLog.url = request.url.toUrl()
        snifferRequest.url = snifferRequestLog.url.toString()

        RequestCache.put(snifferRequestLog.id, snifferRequestLog)
        RequestCache.onSniffListener?.onSniff(snifferRequestLog)
        val startNs = System.nanoTime()
        //处理响应
        val response: Response

        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            snifferResponse.textBody.appendln("END HTTP Error:${e.message}")
            throw e
        }
        val responseBody = response.body!!
        val headers = response.headers
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        snifferResponse.textBody.appendln("${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)}ms${", $bodySize body"})")

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
            snifferResponse.textBody.appendln("END HTTP (binary ${buffer.size}-byte body omitted)")
            return response
        }

        if (contentLength != 0L) {
            snifferResponse.textBody.appendln(buffer.clone().readString(charset))
        }

        if (gzippedLength != null) {
            snifferResponse.textBody.appendln("END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
        } else {
            snifferResponse.textBody.appendln("END HTTP (${buffer.size}-byte body)")
        }
        RequestCache.onSniffListener?.onSniff(snifferRequestLog)
        return response
    }
}