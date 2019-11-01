package com.zanfou.polaris.api.router.exception;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 路由异常
 */
public class RouterException extends RuntimeException {
    /**
     * 路由找不到
     */
    public static final int ERROR_ROUTER_NOT_FOUND = 1000;

    /**
     * 路由表创建失败
     */
    public static final int ERROR_ROUTER_POOL_NOT_FOUND = 1001;
    /**
     * 路由格式错误
     */
    public static final int ERROR_ROUTER_PATH_FORMAT = 1002;
    /**
     * startActivityForResult error
     */
    public static final int ERROR_START_ACTIVITY_FOR_RESULT = 1003;
    /**
     * Activity 未找到
     */
    public static final int ERROR_ACTIVITY_NOT_FOUND = 1004;

    /**
     * 系统未知异常
     */
    public static final int ERROR_CODE_UNKNOWN = -1;

    private String errorMessage;
    private int code;

    public RouterException(int code, String errorMessage) {
        super("RouterError! code: " + code + "; message: " + errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getCode() {
        return code;
    }
}
