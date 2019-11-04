package com.zanfou.polaris;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zanfou.app.router.inject.MainActivityInject;
import com.zanfou.polaris.annotation.field.FieldInject;
import com.zanfou.polaris.annotation.router.Route;
import com.zanfou.polaris.api.field.PolarisInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-10-31
 * <p>
 * 测试路由解析问题
 */
@Route("test/activity/testactivity")
public class MainActivity extends Activity {
    @FieldInject(value = {"aaa", "bbb", "ccc"}, defaultValue = true)
    public int anInt = 1;
    @FieldInject
    public float aFloat;
    @FieldInject
    public double aDouble;
    @FieldInject
    public byte aByte;
    @FieldInject
    public char aChar;
    @FieldInject
    public boolean aBoolean;
    @FieldInject
    public long aLong;
    @FieldInject
    public short aShort;

    @FieldInject(defaultValue = true)
    public String aString = "aaaa";
    @FieldInject
    public CharSequence charSequence;
    @FieldInject
    public Parcelable parcelable;
    @FieldInject
    public Serializable serializable;
    @FieldInject
    public Boolean bBoolean;
    @FieldInject
    public Integer bnInt;


    @FieldInject
    public String[] bString;
    @FieldInject
    public int[] bInt;

    @FieldInject
    public ArrayList<String> cString;
    @FieldInject
    public ArrayList<Integer> cInt;
    @FieldInject
    public ArrayList<Parcelable> cParcelable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("anInt", 2);
            bundle.putFloat("aFloat", 3.0f);
            bundle.putDouble("aDouble", 4.0);
            bundle.putByte("aByte", Byte.valueOf("5"));
            bundle.putChar("aChar", '6');
            bundle.putBoolean("aBoolean", true);
            bundle.putLong("aLong", 7L);
            bundle.putShort("aShort", Short.valueOf("8"));
            bundle.putString("aString", "9");
            bundle.putCharSequence("charSequence", "9");
            bundle.putParcelable("parcelable", new ParcelableObject());
            bundle.putSerializable("serializable", "10");
            bundle.putBoolean("bBoolean", true);
            bundle.putInt("bnInt", 11);
            bundle.putStringArray("bString", new String[]{"12", "13"});
            bundle.putIntArray("bInt", new int[]{14, 15});
            ArrayList<String> value = new ArrayList<>();
            value.add("18");
            value.add("19");
            bundle.putStringArrayList("cString", value);
            ArrayList<Integer> value1 = new ArrayList<>();
            value1.add(20);
            value1.add(21);
            bundle.putIntegerArrayList("cInt", value1);

            ArrayList<Parcelable> value2 = new ArrayList<>();
            value2.add(new ParcelableObject());
            value2.add(new ParcelableObject());
            bundle.putParcelableArrayList("cParcelable", value2);
            long currentTimeMillis = System.nanoTime();
            new MainActivityInject().inject(this, bundle);
            Log.i("shuxin.wei", "onCreate: " + (System.nanoTime() - currentTimeMillis));

            long currentTimeMillis1 = System.nanoTime();
            PolarisInject.inject(this, bundle);
            Log.i("shuxin.wei", "onCreate: " + (System.nanoTime() - currentTimeMillis1));
            long currentTimeMillis2 = System.nanoTime();
            PolarisInject.reflexInject(this, bundle);
            Log.i("shuxin.wei", "onCreate: " + (System.nanoTime() - currentTimeMillis2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "TestActivity{" +
                "\nanInt=" + anInt +
                ",\n aFloat=" + aFloat +
                ",\n aDouble=" + aDouble +
                ",\n aByte=" + aByte +
                ",\n aChar=" + aChar +
                ",\n aBoolean=" + aBoolean +
                ",\n aLong=" + aLong +
                ",\n aShort=" + aShort +
                ",\n aString='" + aString + '\'' +
                ",\n charSequence=" + charSequence +
                ",\n parcelable=" + parcelable.toString() +
                ",\n serializable=" + serializable +
                ",\n bBoolean=" + bBoolean +
                ",\n bnInt=" + bnInt +
                ",\n bString=" + Arrays.toString(bString) +
                ",\n bInt=" + Arrays.toString(bInt) +
                ",\n cString=" + cString.toString() +
                ",\n cInt=" + cInt.toString() +
                ",\n cParcelable=" + cParcelable.toString() +
                '}';
    }

    public static class ParcelableObject implements Parcelable {
        public String name = "1111";

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
        }

        public ParcelableObject() {
        }

        protected ParcelableObject(Parcel in) {
            this.name = in.readString();
        }

        public static final Creator<ParcelableObject> CREATOR = new Creator<ParcelableObject>() {
            @Override
            public ParcelableObject createFromParcel(Parcel source) {
                return new ParcelableObject(source);
            }

            @Override
            public ParcelableObject[] newArray(int size) {
                return new ParcelableObject[size];
            }
        };

        @Override
        public String toString() {
            return "ParcelableObject{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
