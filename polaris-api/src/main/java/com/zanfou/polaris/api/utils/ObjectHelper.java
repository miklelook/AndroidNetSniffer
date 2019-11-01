package com.zanfou.polaris.api.utils;

public class ObjectHelper {
    private ObjectHelper() {
        throw new AssertionError("No " + ObjectHelper.class.getName() + " instances for you!");
    }

    /**
     * Check Object is null
     */
    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    /**
     * 检查对象是否为空
     */
    public static boolean checkObjectNull(Object obj) {
        return obj == null;
    }

    /**
     * 检查数组是否为空
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }


    /**
     * 检查String是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
