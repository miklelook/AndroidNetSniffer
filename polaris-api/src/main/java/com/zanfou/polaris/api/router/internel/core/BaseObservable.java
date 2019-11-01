package com.zanfou.polaris.api.router.internel.core;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zanfou.polaris.api.router.internel.RouterDispatcher;
import com.zanfou.polaris.api.router.token.RouterToken;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-12
 * <p>
 */
public class BaseObservable {
    protected RouterToken routerToken;

    @Nullable
    public Object subscribe() {
        return build().navigate();
    }

    @Nullable
    public <T> T subscribe(Class<T> clazz) {
        return build().navigate(clazz);
    }

    public void subscribe(Activity activity, int requestCode) {
        build().navigate(activity, requestCode);
    }

    public void subscribe(Fragment fragment, int requestCode) {
        build().navigate(fragment, requestCode);
    }

    private RouterDispatcher build() {
        return new RouterDispatcher(routerToken);
    }
}
