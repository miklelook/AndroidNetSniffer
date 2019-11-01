package com.zanfou.polaris.api.router.internel.core.handler;

import androidx.fragment.app.Fragment;

import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 处理Fragment的路由
 */
public class FragmentXHandler implements RouterHandler {

    @Override
    public Object handleRouter(RouterToken routerToken) throws Exception {
        Fragment newInstance = (Fragment) routerToken.getTargetClass().getConstructor().newInstance();
        if (routerToken.getBundle() != null) {
            newInstance.setArguments(routerToken.getBundle());
        }
        return newInstance;
    }
}
