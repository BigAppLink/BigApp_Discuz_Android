package com.kit.utils;

import com.kit.interfaces.IEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ListUtils {

    /**
     * @param list  被截取list
     * @param start 起始位置
     * @param size  截取多少个
     * @param <T>
     * @return
     */
    public static <T> List<T> subList(List<T> list, int start,
                                      int size) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int endIndex = start + size;
        if (start > endIndex || start > list.size()) {
            return null;
        }
        if (endIndex > list.size()) {
            endIndex = list.size();
        }
        return list.subList(start, endIndex);
    }


    /**
     * 深copy
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> List<T> deepCopy(List<T> src) {
        List<T> dest = null;
        try {

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(
                    byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
             dest = (List<T>) in.readObject();
        }catch (Exception e){
            ZogUtils.showException(e);
        }
        return dest;
    }


    /**
     * 转换为数组
     *
     * @param list
     * @return
     */
    public static Object[] toArray(List list) {
        Object[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }


//    /**
//     * 转换为ArrayList
//     *
//     * @param list
//     * @return
//     */
//    public static ArrayList toArrayList(List list) {
//        if (list == null || list.isEmpty()) {
//            return null;
//        } else {
//            Set set = new HashSet();
//            ArrayList newList = new ArrayList();
//            for (Iterator iter = list.iterator(); iter.hasNext(); ) {
//                Object element = iter.next();
//                newList.add(element);
//            }
//            return newList;
//        }
//    }

    /**
     * @param
     * @return Object 返回类型
     * @Title getOne
     * @Description 得到一个
     */
    public static <T> T getLast(List<T> objs) {

        if (ListUtils.isNullOrEmpty(objs))
            return null;

        return objs.get(objs.size() - 1);

    }

    /**
     * @param
     * @return Object 返回类型
     * @Title getOne
     * @Description 得到一个
     */
    public static Object getOne(List objs) {

        int index = RandomUtils.getRandomIntNum(0, objs.size() - 1);

        return objs.get(index);

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面的数据是null
     */
    public static boolean isNullOrContainEmpty(List objs) {

        if (objs == null || objs.size() < 1) {
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
     * @Title isNullOrEmpty
     * @Description 判定List是否是空指针或里面没有数据
     */
    public static boolean isNullOrEmpty(List<?> objs) {

        if (objs == null || objs.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定List里面没有数据
     */
    public static boolean isEmpty(List<?> objs) {

        if (objs.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * @param
     * @return boolean 返回类型
     * @Description 去重
     */
    public static List removeDuplicate(List<?> objs, Comparator comparator) {
        List<Object> tempList = new ArrayList<Object>();

        if (objs == null || objs.isEmpty()) {
            return null;
        } else {

            if (comparator != null) {
                for (Object obj : objs) {
                    if (!isContain(tempList, obj, comparator)) {
                        tempList.add(obj);
                    }
                }


            } else {//comparator为null时，传统比较
                for (Object obj : objs) {
                    if (!tempList.contains(obj)) {
                        tempList.add(obj);
                    }
                }
            }

        }


        if (tempList == null || tempList.isEmpty()) {
            return null;
        } else
            return tempList;
    }


    public static List<Object> replace(List<Object> objs, Object obj, Comparator comparator) {

        Object[] objects = ListUtils.toArray(objs);

        for (int i = 0; i < objs.size(); i++) {
            int compare = comparator.compare(i, obj);

            if (compare == 0) {
//                LogUtils.printLog(ListUtils.class, "compare true. value:" + compare);

                objects[i] = new Object();
            }

        }

        objs = ArrayUtils.toList(objects);
        return objs;
    }


    /**
     * 根据comparator判定是否包含
     *
     * @param objs
     * @param obj
     * @param comparator
     * @return
     */
    public static boolean isContain(List<?> objs, Object obj, Comparator comparator) {

        for (Object i : objs) {
            int compare = comparator.compare(i, obj);

            if (compare == 0) {
//                LogUtils.printLog(ListUtils.class, "compare true. value:" + compare);
                return true;
            }

        }
//        LogUtils.printLog(ListUtils.class, "compare false");
        return false;
    }


    public static List addTop(List list, Object obj) {
        List tempList = new ArrayList();
        tempList.add(obj);
        tempList.addAll(list);

        return tempList;
    }


    public static <T> T find(List<T> list, IEqual equal) {
        for (T i : list) {
            if (equal.equal(i)) {
                return i;
            }

        }
        return null;
    }


    public static <T> List<T> remove(List<T> list, IEqual iEqual) {


        for (Iterator<T> it = list.iterator(); it.hasNext(); ) {
            T t = it.next();
            if (iEqual.equal(t)) {
                it.remove();  // ok
            }
        }

        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (iEqual.equal(t)) {
                list.remove(t);
            }
        }
        return list;
    }

}