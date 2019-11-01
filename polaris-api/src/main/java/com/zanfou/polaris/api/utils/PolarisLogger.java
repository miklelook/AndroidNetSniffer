package com.zanfou.polaris.api.utils;

import android.util.Log;

import com.zanfou.polaris.api.BuildConfig;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 */
public class PolarisLogger {
    public static boolean debug = BuildConfig.DEBUG;
    private static final String TAG = "Polaris";

    /**
     * 打印日志
     *
     * @param content
     */
    public static void logD(Object content) {
        if (debug && content != null) {
            Log.i(TAG, content.toString());
        }
    }
}
