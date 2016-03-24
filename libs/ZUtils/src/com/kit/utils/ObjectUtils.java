package com.kit.utils;

/**
 * Created by Zhao on 15/9/10.
 */
public class ObjectUtils {
    public static <T> T deepCopy(T t, Class<T> clazz) {
        String s = GsonUtils.toJson(t);
        return GsonUtils.getObj(s, clazz);
    }
}
