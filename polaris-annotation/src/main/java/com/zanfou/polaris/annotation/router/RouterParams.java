package com.zanfou.polaris.annotation.router;

import com.zanfou.polaris.annotation.utils.RouterUtils;

import java.net.URI;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 路由的配置信息
 */
public class RouterParams {
    private Route route;
    private URI[] paths;
    private String group;
    private Class target;
    private RouteType routeType;

    public RouterParams(Route route) {
        this.route = route;
        String[] value = route.value();
        paths = new URI[value.length];
        for (int i = 0; i < value.length; i++) {
            paths[i] = RouterUtils.checkPath(value[i]);
        }
    }

    public Route getRoute() {
        return route;
    }


    public RouterParams(RouteType routeType, String[] paths, String group, Class target) {
        this.routeType = routeType;
        this.paths = new URI[paths.length];
        for (int i = 0; i < paths.length; i++) {
            this.paths[i] = RouterUtils.checkPath(paths[i]);
        }
        this.group = group;
        this.target = target;
    }

    public Class getTarget() {
        return target;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public URI[] getPaths() {
        return paths;
    }

    /**
     * 生成RouteParams的伪代码
     */
    public static String fillRouteParamsString(RouterParams params, String currentPath, String targetClassName) {
        StringBuilder paths = new StringBuilder();
        for (URI path : params.getPaths()) {
            if (paths.length() > 0) {
                paths.append(",");
            }
            paths.append("\"").append(path.toString()).append("\"");
        }
        String routeFormat = "new $T($T.%s,new String[]{" + paths.toString() + "},\"%s\",%s.class)";
        return String.format(routeFormat, params.getRouteType(),
                RouterUtils.getGroupByPath(URI.create(currentPath).getPath()),
                targetClassName);
    }
}
