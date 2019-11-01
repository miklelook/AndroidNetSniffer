package com.zanfou.polaris.annotation.utils;


public class PolarisLog {
    private static final String TAG = "Polaris";

    /**
     * 打印日志
     *
     * @param content
     */
    public static void log(Object content) {
        System.out.println(TAG + ":   " + content.toString());
    }
}
