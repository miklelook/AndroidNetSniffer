package com.zanfou.polaris.api.field;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-11-01
 * <p>
 *  IInject
 */
public interface Factory {
    IInject creator(Class<?> clazz);
}
