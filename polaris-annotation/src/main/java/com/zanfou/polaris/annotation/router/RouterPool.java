package com.zanfou.polaris.annotation.router;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 路由池，保存所有的路由映射
 */
public interface RouterPool {
    /**
     * 以路由组名称查找路由组信息
     *
     * @param group 路由组名称
     * @return 路由组
     */
    AbsRouter findRouterByGroup(String group);
}
