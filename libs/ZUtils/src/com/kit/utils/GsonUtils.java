package com.kit.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhao on 14/11/20.
 */
public class GsonUtils {


    /**
     * 强制转换对象
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T castObj(Object obj, Class<T> clazz) {
        return GsonUtils.getObj(GsonUtils.toJson(obj), clazz);
    }

    /**
     * 强制转换列表
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> List<T> castList(Object obj, Type typeOfT) {
        return GsonUtils.getList(GsonUtils.toJson(obj), typeOfT);
    }


    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * 解析成list
     *
     * @param jsonStr
     * @param type    要构建出type
     * @return
     */
    public static <T> List<T> getList(String jsonStr, Type type) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(jsonStr, type);
        return list;
    }


    /**
     * 解析成list
     *
     * @param jsonStr
     * @return
     */
    public static <T> ArrayList<T> getArrayList(String jsonStr, Type typeOfT) {
        Gson gson = new Gson();

        ArrayList<T> list = gson.fromJson(jsonStr, typeOfT);
        return list;
    }


    /**
     * 解析成对象
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static <T> T getObj(String jsonStr, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, clazz);
    }


}
