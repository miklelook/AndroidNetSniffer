package com.zanfou.polaris.annotation.utils;

import com.zanfou.polaris.annotation.router.RouterPool;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 */
public class Constants {
    public static final String PATH_SPLIT = "/";
    /**
     * 路由Path正则匹配，Path必须是以/或者英文开头，中间的PathSegment必须是英文或者数字
     * <p>
     * zanfou://www.zanfou.com/product/detail ---> Scheme:zanfou Host:www.zanfou.com Path:/product/detail
     * zanfou:/www.zanfou.com/product/detail ---> Scheme:zanfou Host:null Path:/www.zanfou.com/product/detail
     * zanfou://product/detail ---> Scheme:zanfou Host:product Path:/detail
     * www.zanfou.com/product/detail ---> Scheme:null Host:null Path:www.zanfou.com/product/detail
     * /product/detail ---> Scheme:null Host:null Path:/product/detail
     */
    public static final String PATH_REGEX = "/*[a-zA-Z][a-zA-Z0-9-_]*+(/[a-zA-Z0-9-_]+/?)*";

    /**
     * 生成文件的包名
     */
    public static final String PACKAGE_NAME = "com.zanfou.app.router";
    /**
     * 参数注入的包名
     */
    public static final String INJECT_PACKAGE_NAME = PACKAGE_NAME + ".inject";

    /**
     * 生成的InjectFactory类名
     */
    public static final String INJECTFACTORY_CLASS_SIMPLE_NAME = "InjectFactory";

    /**
     * 参数注入生成的文件名格式
     */
    public static final String FILE_NAME_INJECT_FORMAT = "%sInject";
    /**
     * Polaris-api中的 IInject 类
     */
    public static final String IINJECT_CLASS_NAME = "com.zanfou.polaris.api.field.IInject";
    /**
     * Polaris-api中的 Factory 类
     */
    public static final String FACTORY_CLASS_NAME = "com.zanfou.polaris.api.field.Factory";
    /**
     * Bundle 类名
     */
    public static final String BUNDLE_CLASS_NAME = "android.os.Bundle";
    /**
     * 生成路由映射文件的文件名格式
     */
    public static final String FILE_NAME_ROUTER_FORMAT = "%sPolarisRouter";

    /**
     * 生成保存所有路由映射的文件名
     */
    public static final String ROUTER_POOL_FILE_NAME = "PolarisRouterPool";

    /**
     * 方法名要一致，生成的文件会重写这个方法
     * {@link RouterPool#findRouterByGroup(String)}
     */
    public static final String ROUTER_POOL_FIND_ROUTER_METHOD_NAME = "findRouterByGroup";

    /**
     * 生成保存所有路由映射的文件全路径名
     */
    public static final String ROUTER_POOL_FILE_CANONICAL_NAME = PACKAGE_NAME + "." + ROUTER_POOL_FILE_NAME;

    /**
     * 保存路由信息的字段名称
     */
    public static final String FIELD_ROUTES = "routes";
    /**
     * Json序列化后存储在Bundle中的Key，在Activity和Fragment创建时使用
     * <p>
     * Uri 后面拼接的参数，序列化成Json，存储在Bundle中，这样在反序列时可以直接将Json反序列化成对应的对象
     */
    public static final String POLARIS_JSON_QUERY = "polaris_json_query";
    /**
     * 将原始路由请求信息放在Intent中，以此来做为Key
     */
    public static final String POLARIS_ROUTER_URI = "polaris_router_uri";
}
