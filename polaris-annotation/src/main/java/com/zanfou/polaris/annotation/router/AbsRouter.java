package com.zanfou.polaris.annotation.router;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 路由信息存储的基类
 */
public abstract class AbsRouter {
    protected Map<String, RouterParams> routes;
    private boolean loaded = false;

    /**
     * 获取路由信息，通过路由
     */
    public RouterParams getRouterParams(String route) {
        if (routes == null) {
            routes = new LinkedHashMap<>(1, 0.75f, true);
            load();
            loaded = true;
        }
        return routes.get(route);
    }

    /**
     * 加载路由信息
     */
    public abstract void load();

    /**
     * 是否已经加载过路由
     *
     * @return
     */
    public boolean isLoaded() {
        return loaded;
    }
}
