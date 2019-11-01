package com.zanfou.polaris.api.router.internel;

import com.zanfou.polaris.annotation.router.AbsRouter;
import com.zanfou.polaris.annotation.router.RouterPool;
import com.zanfou.polaris.annotation.utils.Constants;
import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.router.exception.RouterException;
import com.zanfou.polaris.api.router.token.RouterToken;
import com.zanfou.polaris.api.utils.PolarisLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 管理路由配置信息
 */
public class RouterManager {
    private static volatile RouterManager instance;
    /**
     * 注册的全局路由观察者
     */
    private List<Polaris.Observer> observers;
    /**
     * 路由表
     */
    private RouterPool routerPool;
    private long startTime;
    /**
     * 全局的路由观察者
     * 在这个观察者中对使用者注册的全局路由观察者进行回调
     */
    private final Polaris.Observer globalObserver = new Polaris.Observer() {
        @Override
        public void onStart(RouterToken routerToken) {
            startTime = System.nanoTime();
            PolarisLogger.logD("Polaris Router Start---> RouterToken:" + routerToken);
            if (observers != null) {
                for (Polaris.Observer observer : observers) {
                    observer.onStart(routerToken);
                }
            }
        }

        @Override
        public void onAccept(RouterToken routerToken) {
            if (observers != null) {
                for (Polaris.Observer observer : observers) {
                    observer.onAccept(routerToken);
                }
            }
            long consumeTime = (System.nanoTime() - startTime) / 1000000;
            PolarisLogger.logD("Polaris Router Success--->ConsumeTime:" + consumeTime + "ms; RouterToken:" + routerToken);
        }

        @Override
        public void onError(RouterException e, RouterToken routerToken) {
            PolarisLogger.logD("Polaris Router Failed---> RouterToken:" + routerToken);
            e.printStackTrace();
            if (observers != null) {
                for (Polaris.Observer observer : observers) {
                    observer.onError(e, routerToken);
                }
            }
        }
    };


    public static RouterManager getInstance() {
        if (instance == null) {
            synchronized (RouterManager.class) {
                if (instance == null) {
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    private RouterManager() {
        //加载路由索引
        try {
            Class<?> routerPoolClass = Class.forName(Constants.ROUTER_POOL_FILE_CANONICAL_NAME);
            Object routerPool = routerPoolClass.getConstructor().newInstance();
            if (routerPool instanceof RouterPool) {
                this.routerPool = (RouterPool) routerPool;
            } else {
                throw new RuntimeException(routerPoolClass.getName() + " must implements " + RouterPool.class.getName() + "!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            globalObserver.onError(new RouterException(RouterException.ERROR_ROUTER_POOL_NOT_FOUND, e.getMessage()),
                    null);
        }
    }


    AbsRouter findRouterByGroup(String group) throws Exception {
        if (routerPool == null) {
            throw new RouterException(RouterException.ERROR_ROUTER_POOL_NOT_FOUND,
                    Constants.ROUTER_POOL_FILE_CANONICAL_NAME + " generated failed!");
        }
        return routerPool.findRouterByGroup(group);
    }

    public void addObserver(Polaris.Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        this.observers.add(observer);
    }

    public void removeObserver(Polaris.Observer observer) {
        if (observers == null) {
            return;
        }
        this.observers.remove(observer);
    }

    public Polaris.Observer getGlobalObserver() {
        return globalObserver;
    }
}
