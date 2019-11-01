package com.zanfou.polaris.api.router.internel.core;

import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.router.exception.RouterException;
import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 */
public class SimpleObserver implements Polaris.Observer {
    @Override
    public void onStart(RouterToken routerToken) {

    }

    @Override
    public void onAccept(RouterToken routerToken) {

    }

    @Override
    public void onError(RouterException e, RouterToken routerToken) {

    }
}
