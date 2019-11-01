package com.zanfou.polaris.annotation.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 字段的注入，一期使用运行时注解，后期进行优化，可采用异步或者编译期注解
 * <p>
 * 被注入的字段，必须是public修饰
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FieldInject {
    /**
     * 注入的字段名称
     *
     * @return
     */
    String[] value() default "";

    /**
     * 是否设置默认值
     * 例如：
     * <p>
     * {@code
     *
     * @FieldInject(defaultValue = true)
     * public int anInt = 100;
     * }
     * <p>
     * 如果为true，会进行空判断，那么在获取到的值为null时，不对字段赋值；
     * 例如：
     * {@code
     * Object anIntValue = getData(new String[]{"anInt","aaa","bbb","ccc"}, bundle, jsonObject, int.class);
     * if(anIntValue != null) {
     * object.anInt = (int)anIntValue;
     * }
     * }
     */
    boolean defaultValue() default false;
}
