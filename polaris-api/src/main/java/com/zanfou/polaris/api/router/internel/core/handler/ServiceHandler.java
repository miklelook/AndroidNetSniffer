package com.zanfou.polaris.api.router.internel.core.handler;

import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 处理Service的路由
 */
public class ServiceHandler implements RouterHandler {
    @Override
    public Object handleRouter(RouterToken routerToken) throws Exception {
        return routerToken.getTargetClass().getConstructor().newInstance();
    }
}
