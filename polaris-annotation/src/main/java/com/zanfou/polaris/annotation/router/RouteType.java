package com.zanfou.polaris.annotation.router;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 支持的类型
 */
public enum RouteType {
    /**
     * Activity
     */
    ACTIVITY("android.app", "Activity", Class.class),
    /**
     * Fragment
     */
    FRAGMENT("android.app", "Fragment", Class.class),

    /**
     * Androidx - fragment
     */
    FRAGMENT_X("androidx.fragment.app", "Fragment", Class.class),
    /**
     * 服务
     */
    SERVICE(null, "Service", Class.class);

    String simpleName;
    String packageName;
    Class valueType;

    RouteType(String packageName, String simpleName, Class type) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.valueType = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getName() {
        return packageName + "." + simpleName;
    }

    public Class getValueType() {
        return valueType;
    }
}
