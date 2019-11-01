package com.zanfou.polaris.api.router.internel;

import android.os.Bundle;
import android.os.Parcelable;

import com.zanfou.polaris.api.router.internel.core.BaseObservable;
import com.zanfou.polaris.api.router.token.RouterToken;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 路由构建器
 */
public class RouterObservable extends BaseObservable {

    private RouterObservable(String path) {
        routerToken = RouterToken.newInstance();
        path(path);
    }

    public static RouterObservable newInstance(String path) {
        return new RouterObservable(path);
    }

    public RouterObservable path(String path) {
        routerToken.setRoute(path);
        return this;
    }

    public RouterObservable putExtras(Bundle bundle) {
        routerToken.getBundle().putAll(bundle);
        return this;
    }

    public RouterObservable putExtra(String name, boolean value) {
        routerToken.getBundle().putBoolean(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, byte value) {
        routerToken.getBundle().putByte(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, char value) {
        routerToken.getBundle().putChar(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, short value) {
        routerToken.getBundle().putShort(name, value);
        return this;
    }


    public RouterObservable putExtra(String name, int value) {
        routerToken.getBundle().putInt(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, long value) {
        routerToken.getBundle().putLong(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, float value) {
        routerToken.getBundle().putFloat(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, double value) {
        routerToken.getBundle().putDouble(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, String value) {
        routerToken.getBundle().putString(name, value);
        return this;
    }


    public RouterObservable putExtra(String name, CharSequence value) {
        routerToken.getBundle().putCharSequence(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, Parcelable value) {
        routerToken.getBundle().putParcelable(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, Parcelable[] value) {
        routerToken.getBundle().putParcelableArray(name, value);
        return this;
    }

    public RouterObservable putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        routerToken.getBundle().putParcelableArrayList(name, value);
        return this;
    }

    public RouterObservable putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        routerToken.getBundle().putIntegerArrayList(name, value);
        return this;
    }

    public RouterObservable putStringArrayListExtra(String name, ArrayList<String> value) {
        routerToken.getBundle().putStringArrayList(name, value);
        return this;
    }

    public RouterObservable putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        routerToken.getBundle().putCharSequenceArrayList(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, Serializable value) {

        routerToken.getBundle().putSerializable(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, boolean[] value) {
        routerToken.getBundle().putBooleanArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, byte[] value) {
        routerToken.getBundle().putByteArray(name, value);
        return this;
    }


    public RouterObservable putExtra(String name, short[] value) {
        routerToken.getBundle().putShortArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, char[] value) {
        routerToken.getBundle().putCharArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, int[] value) {
        routerToken.getBundle().putIntArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, long[] value) {
        routerToken.getBundle().putLongArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, float[] value) {
        routerToken.getBundle().putFloatArray(name, value);
        return this;
    }


    public RouterObservable putExtra(String name, double[] value) {
        routerToken.getBundle().putDoubleArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, String[] value) {
        routerToken.getBundle().putStringArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, CharSequence[] value) {
        routerToken.getBundle().putCharSequenceArray(name, value);
        return this;
    }

    public RouterObservable putExtra(String name, Bundle value) {
        routerToken.getBundle().putBundle(name, value);
        return this;
    }
}
