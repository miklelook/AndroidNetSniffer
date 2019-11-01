package com.zanfou.polaris.api.field;

import android.app.Activity;
import android.os.Bundle;
import android.util.LruCache;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zanfou.polaris.annotation.field.FieldInject;
import com.zanfou.polaris.annotation.utils.Constants;
import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.utils.JsonConverter;
import com.zanfou.polaris.api.utils.ObjectHelper;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 注入参数
 */
public class PolarisInject {

    private static JsonConverter jsonConverter;
    private static final LruCache<Class, IInject> INJECT_LRU_CACHE = new LruCache<>(5);
    private static Factory injectFactory;

    static {
        try {
            injectFactory = (Factory) Class.forName(Constants.INJECT_PACKAGE_NAME + "." + Constants.INJECTFACTORY_CLASS_SIMPLE_NAME).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析Activity传递的参数
     *
     * @param activity
     */
    public static void inject(Activity activity) {
        Objects.requireNonNull(activity);
        inject(activity, activity.getIntent().getExtras());
    }

    /**
     * 解析Fragment传递的参数
     *
     * @param fragment
     */
    public static void inject(Fragment fragment) {
        Objects.requireNonNull(fragment);
        inject(fragment, fragment.getArguments());
    }

    public static String getRouteFromBundle(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        return bundle.getString(Constants.POLARIS_ROUTER_URI, "");
    }

    /**
     * 解析参数
     *
     * @param into   要解析的对象
     * @param bundle 参数
     * @param <T>
     */
    public static <T> void inject(T into, Bundle bundle) {
        try {
            IInject iInject = INJECT_LRU_CACHE.get(into.getClass());
            if (iInject == null) {
                Class<?> intoClass = into.getClass();
                iInject = injectFactory.creator(intoClass);
                INJECT_LRU_CACHE.put(intoClass, iInject);
            }
            iInject.inject(into, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射解析参数
     *
     * @param into
     * @param bundle
     * @param <T>
     * @see #inject(Object, Bundle)
     */
    public static <T> void reflexInject(T into, Bundle bundle) {
        if (bundle == null || bundle.isEmpty()) {
            return;
        }
        try {
            Field[] declaredFields = into.getClass().getFields();
            if (ObjectHelper.isEmpty(declaredFields)) {
                return;
            }
            //优先解析 POLARIS_JSON_QUERY,不存在 POLARIS_JSON_QUERY， 再解析Bundle
            JSONObject jsonObject = null;
            if (bundle.containsKey(Constants.POLARIS_JSON_QUERY)) {
                jsonObject = new JSONObject(bundle.getString(Constants.POLARIS_JSON_QUERY));
            }
            for (Field declaredField : declaredFields) {
                try {
                    FieldInject fieldInject = declaredField.getAnnotation(FieldInject.class);
                    if (fieldInject == null) {
                        continue;
                    }
                    Object object = getData(declaredField.getName(), fieldInject.value(), bundle, jsonObject, declaredField.getType());
                    if (object == null) {
                        continue;
                    }
                    boolean accessible = declaredField.isAccessible();
                    declaredField.setAccessible(true);
                    declaredField.set(into, object);
                    declaredField.setAccessible(accessible);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析参数值
     * 优先解析Bundle，Bundle的数据结构是ArrayMap，效率相对Json会高一些，并且路由跳转更多的场景是参数放在Bundle中
     * 如果从Bundle中解析不到再从Json中找，Json是由路由解析Uri之后放在Bundle中的
     *
     * @param keys       参数Key
     * @param bundle     优先从Bundle中解析
     * @param jsonObject Bundle中解析不到，再从Json中解析
     * @param type       数据类型，Json解析使用
     * @return
     */
    @Nullable
    public static Object getData(String firstKey, String[] keys, Bundle bundle, JSONObject jsonObject, Type type) {
        Object data = getData(firstKey, bundle, jsonObject, type);
        for (String key : keys) {
            if (data != null) {
                return data;
            }
            if (key == null || key.length() == 0) {
                continue;
            }
            data = getData(key, bundle, jsonObject, type);
        }
        return null;
    }

    public static Object getData(String key, Bundle bundle, JSONObject jsonObject, Type type) {
        try {
            if (bundle.containsKey(key)) {
                return bundle.get(key);
            } else if (jsonObject != null && jsonObject.has(key)) {
                return getJsonConverter().deserialization(jsonObject.getString(key), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Json转换器
     *
     * @return
     */
    public static JsonConverter getJsonConverter() {
        if (jsonConverter == null) {
            jsonConverter = Polaris.route(JsonConverter.PATH).subscribe(JsonConverter.class);
        }
        return jsonConverter;
    }
}
