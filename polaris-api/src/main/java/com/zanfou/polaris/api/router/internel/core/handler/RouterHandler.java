package com.zanfou.polaris.api.router.internel.core.handler;

import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 路由处理器
 */
public interface RouterHandler {
    /**
     * 处理路由
     *
     * @param routerToken
     * @return
     * @throws Exception
     */
    Object handleRouter(RouterToken routerToken) throws Exception;
}
