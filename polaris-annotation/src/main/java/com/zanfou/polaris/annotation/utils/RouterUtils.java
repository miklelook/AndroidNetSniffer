package com.zanfou.polaris.annotation.utils;

import java.awt.geom.IllegalPathStateException;
import java.net.URI;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 路由工具类
 */
public class RouterUtils {

    /**
     * 校验Path是否符合规则
     * 只用于polaris-annotation和polaris-compiler两个库中
     *
     * @param path
     * @return
     */
    public static URI checkPath(String path) {
        URI uri = null;
        try {
            if (path == null || path.length() == 0) {
                throw new NullPointerException("The router path require non null and non empty!");
            }
            uri = URI.create(path);
            if ((uri.getHost() == null) != (uri.getScheme() == null) || !uri.getPath().matches(Constants.PATH_REGEX)) {
                throw new IllegalPathStateException("The router path format error!");
            }
        } catch (Exception e) {
            RuntimeException runtimeException = new RuntimeException(path + " " + e.getMessage());
            runtimeException.printStackTrace();
            throw runtimeException;
        }
        return uri;
    }

    public static String buildUri(String scheme, String host, String path) {
        return String.format("%s:%s%s", scheme, host, path);
    }

    /**
     * 根据Path获取分组名称
     *
     * @param path
     * @return
     */
    public static String getGroupByPath(String path) {
        if (path == null) {
            return null;
        }
        String[] split = path.replace(".", "").split(Constants.PATH_SPLIT);
        if (split[0] == null || split[0].length() == 0) {
            return split[1];
        } else {
            return split[0];
        }
    }

    public static String getGroupUpper(String group) {
        if (group != null) {
            group = group.substring(0, 1).toUpperCase() + group.substring(1);
        }
        return group;
    }
}
