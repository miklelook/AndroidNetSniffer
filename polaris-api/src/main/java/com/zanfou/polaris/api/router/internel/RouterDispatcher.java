package com.zanfou.polaris.api.router.internel;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zanfou.polaris.annotation.router.AbsRouter;
import com.zanfou.polaris.annotation.router.RouteType;
import com.zanfou.polaris.annotation.router.RouterParams;
import com.zanfou.polaris.annotation.utils.Constants;
import com.zanfou.polaris.annotation.utils.RouterUtils;
import com.zanfou.polaris.api.router.exception.ExceptionHandler;
import com.zanfou.polaris.api.router.exception.RouterException;
import com.zanfou.polaris.api.router.internel.core.handler.ActivityHandler;
import com.zanfou.polaris.api.router.internel.core.handler.FragmentXHandler;
import com.zanfou.polaris.api.router.internel.core.handler.RouterHandler;
import com.zanfou.polaris.api.router.internel.core.handler.ServiceHandler;
import com.zanfou.polaris.api.router.token.RouterToken;
import com.zanfou.polaris.api.utils.PolarisLogger;

import org.json.JSONObject;

import java.util.Set;
import java.util.regex.Pattern;


/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 分发处理路由跳转请求
 */
public final class RouterDispatcher {
    private RouterToken routerToken;
    private RouteType routerType;
    private Activity activity;
    private Fragment fragment;

    public RouterDispatcher(RouterToken routerToken) {
        this.routerToken = routerToken;
    }

    @Nullable
    public Object navigate() {
        try {
            return dispatchRoute();
        } catch (Exception e) {
            ExceptionHandler.handleException(e, routerToken);
        }
        return null;
    }

    @Nullable
    public <T> T navigate(@NonNull Class<T> clazz) {
        try {
            return clazz.cast(dispatchRoute());
        } catch (Exception e) {
            ExceptionHandler.handleException(e, routerToken);
        }
        return null;
    }

    public void navigate(Activity activity, int requestCode) {
        this.activity = activity;
        routerToken.setRequestCode(requestCode);
        try {
            dispatchRoute();
        } catch (Exception e) {
            ExceptionHandler.handleException(e, routerToken);
        }
    }

    public void navigate(Fragment fragment, int requestCode) {
        this.fragment = fragment;
        routerToken.setRequestCode(requestCode);
        try {
            dispatchRoute();
        } catch (Exception e) {
            ExceptionHandler.handleException(e, routerToken);
        }
    }

    @Nullable
    private Object dispatchRoute() throws Exception {
        String originPath = routerToken.getRoute();

        Uri uri;
        try {
            uri = Uri.parse(originPath);
            if (!Pattern.matches(Constants.PATH_REGEX, uri.getPath())) {
                throw new RuntimeException("The format of the Path is incorrect. ");
            }
        } catch (Exception e) {
            throw new RouterException(RouterException.ERROR_ROUTER_PATH_FORMAT, e.getMessage());
        }

        loadRouterParams(uri);

        RouterManager.getInstance().getGlobalObserver().onStart(routerToken);

        RouterHandler routerHandler;
        switch (routerType) {
            case ACTIVITY:
                routerHandler = new ActivityHandler(activity, fragment);
                break;
            case FRAGMENT_X:
                routerHandler = new FragmentXHandler();
                break;
            default:
                routerHandler = new ServiceHandler();
                break;
        }
        Object result = routerHandler.handleRouter(routerToken);
        clear();
        RouterManager.getInstance().getGlobalObserver().onAccept(routerToken);
        return result;
    }

    /**
     * 解析路由有效参数
     *
     * @throws Exception
     */
    private void loadRouterParams(Uri uri) throws Exception {
        long nanoTime = System.nanoTime();

        routerToken.setUri(uri);
        routerToken.setGroup(RouterUtils.getGroupByPath(uri.getPath()));
        AbsRouter router = RouterManager.getInstance().findRouterByGroup(routerToken.getGroup());
        if (router == null) {
            throw new RouterException(RouterException.ERROR_ROUTER_NOT_FOUND,
                    "Not found route group:" + routerToken.getGroup() + ", please check it again!");
        }
        //优先使用Path查找，大部分Route没有Scheme和Host；
        RouterParams routerParams = router.getRouterParams(uri.getPath());
        //如果使用Path没有查到，再使用去除了Fragment、Query等字段的路径进行查找；
        //因为有可能Route使用特定的Scheme和Host
        if (routerParams == null) {
            Uri cleanUri = new Uri.Builder()
                    .scheme(uri.getScheme())
                    .authority(uri.getHost())
                    .path(uri.getPath())
                    .build();
            routerParams = router.getRouterParams(cleanUri.toString());
        }
        if (routerParams == null) {
            throw new RouterException(RouterException.ERROR_ROUTER_NOT_FOUND,
                    "No route matched! The route is  " + routerToken.getRoute() + ", please check it again!");
        }
        long consumeTime = (System.nanoTime() - nanoTime) / 1000000;
        PolarisLogger.logD("Route search consume time:" + consumeTime + "ms");
        /*
            如果Intent设置setData，那么解析时所有参数类型只能是String，PolarisInject就解析不到数据；
            非基本数据类型序列化需要序列化方案，比如Json；
            解决方案：
            1.在生成路由时保存注入信息，在这进行一次解析然后设置Bundle；
            2.所有参数使用String接收，不现实；
            3.先用特定类型解析，找不到使用String解析，默认值会有问题；
            4.使用拦截器，进行个例处理，不大现实；
       */
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (queryParameterNames != null && !queryParameterNames.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            for (String key : queryParameterNames) {
                jsonObject.put(key, uri.getQueryParameter(key));
            }
            routerToken.getBundle().putString(Constants.POLARIS_JSON_QUERY, jsonObject.toString());
        }
        routerToken.getBundle().putString(Constants.POLARIS_ROUTER_URI, uri.toString());
        routerType = routerParams.getRouteType();
        routerToken.setTargetClass(routerParams.getTarget());
    }

    private void clear() {
        activity = null;
        fragment = null;
    }
}
