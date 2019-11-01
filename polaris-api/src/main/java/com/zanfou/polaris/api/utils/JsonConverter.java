package com.zanfou.polaris.api.utils;

import java.lang.reflect.Type;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * Json序列化工具
 */
public interface JsonConverter {
    String PATH = "/json/converter";

    /**
     * 反序列化Json
     *
     * @param json 被解析的Json
     * @param type 反序列化的类型
     * @param <T>
     * @return 反序列化后的对象
     */
    <T> T deserialization(String json, Type type);

    /**
     * 反序列化Json到已存在的对象{@link T}中
     *
     * @param json 被解析的Json
     * @param type 反序列化的类型
     * @param into 被反序列化的对象
     * @param <T>
     */
    <T> void deserialization(String json, Class<? super T> type, T into);

    /**
     * 序列化对象为Json
     *
     * @param jsonObject 被序列化的对象
     * @return 生成的Json
     */
    String serialization(Object jsonObject);
}
