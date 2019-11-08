package com.zanfou.vpn

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-10-31
 *
 * 请求拦截
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btn_request.setOnClickListener {
            et_result.text = "正在请求..."
            OkHttpClient.Builder()
                    .addNetworkInterceptor(OkHttpLogInterceptor().apply {
                        level = OkHttpLogInterceptor.Level.BODY
                    })
                    .build()
                    .newCall(Request.Builder().url("http://172.19.167.91:7300/mock/5d78a6a96460dbf9f53e2290/api/dynamic/list/productDynamic.json").build())
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            runOnUiThread {
                                et_result.text = e.message
                            }
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val readText = response.body?.charStream()?.readText()
                            runOnUiThread {
                                et_result.text = readText
                            }
                        }
                    })
        }
        btn_clear.setOnClickListener {
            et_result.text = ""
        }
    }
}
