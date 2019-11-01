package com.zanfou.polaris.api.router;

import android.app.Application;
import com.zanfou.polaris.api.router.exception.RouterException;
import com.zanfou.polaris.api.router.internel.RouterManager;
import com.zanfou.polaris.api.router.internel.RouterObservable;
import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * Polaris Router Core Code
 */
public class Polaris {
    private static volatile Polaris instance;
    private Application context;

    private Polaris() {
    }

    public static Polaris getInstance() {
        if (instance == null) {
            synchronized (Polaris.class) {
                if (instance == null) {
                    instance = new Polaris();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化RouterManager，加载路由索引
     *
     * @param application 用来无Result的Activity跳转
     * @return
     */
    public Polaris init(Application application) {
        this.context = application;
        RouterManager.getInstance();
        return this;
    }

    public Application getContext() {
        return context;
    }

    /**
     * 创建路由观察者
     *
     * @param router 路由地址
     * @return 路由的观察者
     */
    @Deprecated
    public static RouterObservable path(String router) {
        return route(router);
    }

    /**
     * 创建路由观察者
     *
     * @param route 路由地址
     * @return 路由的观察者
     */
    public static RouterObservable route(String route) {
        return RouterObservable.newInstance(route);
    }

    /**
     * 路由观察者，可以在订阅时进行设置观察者
     */
    public interface Observer {
        /**
         * 路由分发开始
         *
         * @param routerToken 所有的路由参数
         */
        void onStart(RouterToken routerToken);

        /**
         * 路由正常跳转回调
         *
         * @param routerToken 所有的路由参数
         */
        void onAccept(RouterToken routerToken);

        /**
         * 路由跳转异常
         *
         * @param e           异常信息
         * @param routerToken 所有的路由参数
         */
        void onError(RouterException e, RouterToken routerToken);
    }
}
