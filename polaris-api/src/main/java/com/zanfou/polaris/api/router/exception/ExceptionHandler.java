package com.zanfou.polaris.api.router.exception;

import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.router.internel.RouterManager;
import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 异常处理
 */
public class ExceptionHandler {
    public static void handleException(Exception e, RouterToken routerToken) {
        if (e == null) {
            return;
        }
        Polaris.Observer globalObserver = RouterManager.getInstance().getGlobalObserver();
        if (e instanceof RouterException) {
            RouterException routerException = (RouterException) e;
            globalObserver.onError(new RouterException(routerException.getCode(), routerException.getErrorMessage()),
                    routerToken);
        } else {
            globalObserver.onError(new RouterException(RouterException.ERROR_CODE_UNKNOWN, e.getMessage()),
                    routerToken);
        }
    }
}
