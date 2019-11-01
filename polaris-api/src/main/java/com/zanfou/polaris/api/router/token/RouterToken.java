package com.zanfou.polaris.api.router.token;

import android.net.Uri;
import android.os.Bundle;

import static com.zanfou.polaris.api.router.internel.core.handler.ActivityHandler.REQUEST_CODE_DEFAULT;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 保存路由请求的内容
 */
public class RouterToken {
    private RouterToken() {
    }

    public static RouterToken newInstance() {
        return new RouterToken();
    }

    /**
     * 原始的路由地址，调用者传递进来的；
     */
    private String route;
    /**
     * 分组信息
     */
    private String group;
    /**
     * 由router 构造的URI
     */
    private Uri uri;
    /**
     * 携带的参数
     */
    private Bundle bundle;

    /**
     * startActivityForResult requestCode
     */
    private int requestCode = REQUEST_CODE_DEFAULT;

    private Class<?> targetClass;

    public String getRoute() {
        return route;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Bundle getBundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public String toString() {
        return "RouterToken{" +
                "route='" + route + '\'' +
                ", group='" + group + '\'' +
                ", uri=" + uri +
                ", bundle=" + bundle +
                ", requestCode=" + requestCode +
                ", targetClass=" + targetClass +
                '}';
    }
}
