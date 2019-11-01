package com.zanfou.polaris.api.field;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.utils.JsonConverter;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-10-12
 * <p>
 * 解析注入参数
 */
public abstract class IInject {
    private static JsonConverter jsonConverter;

    /**
     * 解析参数
     *
     * @param targetInjectObject 被注入的对象
     * @param bundle             参数
     */
    public abstract void inject(Object targetInjectObject, Bundle bundle) throws Exception;

    @Nullable
    protected Object getData(String[] keys, Bundle bundle, JSONObject jsonObject, Type type) {
        for (String key : keys) {
            if (key == null || key.length() == 0) {
                continue;
            }
            try {
                if (bundle.containsKey(key)) {
                    return bundle.get(key);
                } else if (jsonObject != null && jsonObject.has(key)) {
                    return getJsonConverter().deserialization(jsonObject.getString(key), type);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JsonConverter getJsonConverter() {
        if (jsonConverter == null) {
            jsonConverter = Polaris.route(JsonConverter.PATH).subscribe(JsonConverter.class);
        }
        return jsonConverter;
    }


}
