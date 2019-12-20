package com.planb.sniffer.cache

import java.io.Serializable
import java.net.URL

abstract class BaseSnifferDetail(
        /**
         * 请求ID
         */
        @JvmField open var id: String,

        /**
         * 请求地址
         */
        @JvmField open var url: URL
) : Serializable