package com.planb.sniffer

import okhttp3.*
import okio.*
import java.io.IOException

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


class Progress {
    @Throws(Exception::class)
    fun run() {
        val request = Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build()
        val progressListener: ProgressListener = object : ProgressListener {
            var firstUpdate = true
            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                if (done) {
                    println("completed")
                } else {
                    if (firstUpdate) {
                        firstUpdate = false
                        if (contentLength == -1L) {
                            println("content-length: unknown")
                        } else {
                            System.out.format("content-length: %d\n", contentLength)
                        }
                    }
                    println(bytesRead)
                    if (contentLength != -1L) {
                        System.out.format("%d%% done\n", 100 * bytesRead / contentLength)
                    }
                }
            }
        }
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(Interceptor {
                    val originalResponse = it.proceed(it.request())
                    originalResponse.newBuilder()
                            .body(ProgressResponseBody(originalResponse.body, progressListener))
                            .build()
                })
                .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            println(response.body!!.string())
        }
    }

    private class ProgressResponseBody internal constructor(private val responseBody: ResponseBody?, private val progressListener: ProgressListener) : ResponseBody() {
        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody!!.contentType()
        }

        override fun contentLength(): Long {
            return responseBody!!.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody!!.source()).buffer()
            }
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L
                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    progressListener.update(totalBytesRead, responseBody!!.contentLength(), bytesRead == -1L)
                    return bytesRead
                }
            }
        }

    }

    internal interface ProgressListener {
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Progress().run()
        }
    }
}
