package com.kit.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayUtils {

    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面没有数据
     */
    public static boolean isNullOrEmpty(Object[] objs) {

        if (objs == null || objs.length < 1) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面没有数据
     */
    public static boolean isEmpty(Object[] objs) {

        if (objs.length < 1) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return Object 返回类型
     * @Title getOne
     * @Description 得到一个
     */
    public static Object getOne(Object[] objs) {

        int index = RandomUtils.getRandomIntNum(0, objs.length - 1);

        return objs[index];

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面的数据是null
     */
    public static boolean isNullOrContainEmpty(Object[] objs) {

        if (objs == null || objs.length < 1) {
            return true;
        } else {
            for (Object o : objs) {
                if (o != null)
                    return false;

            }
            return true;
        }

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Description 去重
     */
    public static Object[] removeDuplicate(Object[] objs) {

        ArrayList<Object> objectArrayList = new ArrayList<Object>();
        if (objs == null || objs.length < 1) {
            return null;
        } else {
            for (int i = 0; i < objs.length; i++) {
                for (int j = objs.length; j > i; j--) {
                    if (!objs[j].equals(objs[i])) {
                        objectArrayList.add(objs[j]);
                    }
                }
            }

            Object[] objects = new Object[objectArrayList.size()];
            objects = objectArrayList.toArray(objects);
            return objects;
        }

    }


    public static List toList(Object[] arr) {
        List list = Arrays.asList(arr);
        return list;
    }

    public static <T extends Object> Set<T> toSet(T[] arr) {
        Set<T> tSet = new HashSet<T>(Arrays.asList(arr));
        return tSet;

    }

    public static <T> ArrayList<T> toArrayList(Object[] arr) {
        List list = toList(arr);
        ArrayList<T> arrayList = new ArrayList<T>();
        arrayList.addAll(list);
        return arrayList;
    }
}