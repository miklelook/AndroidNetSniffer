package com.wei.sniffer

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.wei.sniffer.console.Console
import com.wei.sniffer.okhttp.OkHttpLogInterceptor
import com.wei.sniffer.okhttp.OkHttpSniffer
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * v1.0 of the file created on 2019-11-28 by shuxin.wei, email: weishuxin@maoyan.com
 * description: 请求拦截
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        et_result.apply {
            isHorizontalScrollBarEnabled = true
            movementMethod = ScrollingMovementMethod.getInstance()
            scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
            setSingleLine()
        }
        btn_request.setOnClickListener {
            et_result.text = "正在请求..."
            OkHttpClient.Builder()
                    .addNetworkInterceptor(OkHttpLogInterceptor().apply {
                        level = OkHttpLogInterceptor.Level.BODY
                    })
                    .addNetworkInterceptor(OkHttpSniffer())
                    .build()
                    .newCall(Request.Builder().url("http://172.19.167.91:7300/mock/5d78a6a96460dbf9f53e2290/api/dynamic/list/productDynamic.json?userId=1111&userName=2222").build())
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                et_result.text = "正在失败..."
                            }
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            runOnUiThread {
                                et_result.text = "正在完成..." + String(response.body?.bytes()!!)
                            }
                        }
                    })
            OkHttpClient.Builder()
                    .addNetworkInterceptor(OkHttpLogInterceptor().apply {
                        level = OkHttpLogInterceptor.Level.BODY
                    })
                    .addNetworkInterceptor(OkHttpSniffer())
                    .build()
                    .newCall(Request.Builder().url("http://www.baidu.com/img/pc_ede120f393776516980bdaa3dca88493.png").build())
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                et_result.text = "正在失败..."
                            }
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            runOnUiThread {
                                et_result.text = "正在完成..."
                            }
                        }
                    })

            OkHttpClient.Builder()
                    .addNetworkInterceptor(OkHttpLogInterceptor().apply {
                        level = OkHttpLogInterceptor.Level.BODY
                    })
                    .addNetworkInterceptor(OkHttpSniffer())
                    .build()
                    .newCall(Request.Builder().method("POST", "{\"userName\":\"weishuxin\"}".toRequestBody("application/json".toMediaType()))
                            .url("http://172.19.167.91:7300/mock/5d78a6a96460dbf9f53e2290/api/product/wish.json").build())
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                et_result.text = "正在失败..."
                            }
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            runOnUiThread {
                                et_result.text = "正在完成..."
                            }
                        }
                    })
        }


        btn_clear.setOnClickListener {
            Console.instance.openConsole(application)
        }
    }
}
