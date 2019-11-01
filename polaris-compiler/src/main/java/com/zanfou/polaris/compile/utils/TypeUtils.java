package com.zanfou.polaris.compile.utils;

import java.io.Serializable;
import java.util.ArrayList;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-10-14
 * <p>
 * 数据类型转换
 */
public class TypeUtils {
    private Types typeUtil;
    private Elements elementUtil;

    public TypeUtils(Types typeUtil, Elements elementUtil) {
        this.typeUtil = typeUtil;
        this.elementUtil = elementUtil;
    }

    /**
     * 根据{@link TypeMirror} 获取数据类型，目前主要用于拼接bundle.getXxx();
     */
    public String getTypeName(TypeMirror typeMirror) {
        TypeKind kind = typeMirror.getKind();
        if (kind == TypeKind.BOOLEAN || isSameType(typeMirror, Boolean.class)) {
            return "Boolean";
        } else if (kind == TypeKind.BYTE || isSameType(typeMirror, Byte.class)) {
            return "Byte";
        } else if (kind == TypeKind.SHORT || isSameType(typeMirror, Short.class)) {
            return "Short";
        } else if (kind == TypeKind.INT || isSameType(typeMirror, Integer.class)) {
            return "Int";
        } else if (kind == TypeKind.LONG || isSameType(typeMirror, Long.class)) {
            return "Long";
        } else if (kind == TypeKind.CHAR || isSameType(typeMirror, Character.class)) {
            return "Char";
        } else if (kind == TypeKind.FLOAT || isSameType(typeMirror, Float.class)) {
            return "Float";
        } else if (kind == TypeKind.DOUBLE || isSameType(typeMirror, Double.class)) {
            return "Double";
        } else if (isSameType(typeMirror, String.class)) {
            return "String";
        } else if (isSameType(typeMirror, CharSequence.class)) {
            return "CharSequence";
        } else if (kind == TypeKind.ARRAY) {
            //element.asType().toString() 示例
            //int[]
            TypeMirror componentType = ((ArrayType) typeMirror).getComponentType();
            if (componentType.getKind().isPrimitive()
                    || isSameType(typeMirror, String.class)
                    || isSameType(typeMirror, CharSequence.class)) {
                return getTypeName(componentType) + "Array";
            } else if (componentType.getKind() == TypeKind.ARRAY) {
                return getTypeName(componentType);
            } else {
                return "Serializable";
            }
        } else if (isSameType(typeMirror, "android.os.Bundle")) {
            return "Bundle";
        } else if (isSubtype(typeMirror, "java.util.ArrayList")) {
            return "";
        } else if (isSameType(typeMirror, Serializable.class)) {
            return "Serializable";
        } else if (isSameType(typeMirror, "android.os.Parcelable")) {
            return "Parcelable";
        }
        return null;
    }


    /**
     * 只有String,CharSequence和基本数据类型在getBundle时可以设置默认值
     *
     * @return 获取值时是否能设置默认值
     */
    public boolean isDefaultValueOfBundle(TypeMirror typeMirror) {
        return typeMirror.getKind().isPrimitive()
                //bundle.getCharSequence("", defaultValue);
                || isSameType(typeMirror, CharSequence.class)
                //bundle.getString("", defaultValue);
                || isSameType(typeMirror, String.class);

    }

    private boolean isSameType(TypeMirror typeMirror, String typeName) {
        return typeUtil.isSameType(typeMirror, elementUtil.getTypeElement(typeName).asType());
    }

    private boolean isSameType(TypeMirror typeMirror, Class typeClass) {
        return typeUtil.isSameType(typeMirror, elementUtil.getTypeElement(typeClass.getName()).asType());
    }

    private boolean isSubtype(TypeMirror typeMirror, String typeName) {
        System.out.println("aaa:"+typeMirror.toString()+":"+typeName);
        return typeUtil.isAssignable(typeMirror, elementUtil.getTypeElement(typeName).asType());
    }

    private boolean isSubtype(TypeMirror typeMirror, Class typeClass) {
        System.out.println("aaa:"+typeMirror.toString()+":"+elementUtil.getTypeElement(typeClass.getName()).asType().toString());
        return typeUtil.isAssignable(typeMirror, elementUtil.getTypeElement(typeClass.getName()).asType());
    }
}
