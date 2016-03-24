package com.kit.utils;

import com.kit.interfaces.IEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class SetUtils {



    /**
     * 深copy
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<?> deepCopy(Set<?> src) throws IOException,
            ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(
                byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        Set<?> dest = (Set<?>) in.readObject();
        return dest;
    }


    /**
     * 转换为数组
     *
     * @param set
     * @return
     */
    public static Object[] toArray(Set set) {
        Object[] array = new Object[set.size()];
        set.toArray(array);
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
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面的数据是null
     */
    public static boolean isNullOrContainEmpty(Set objs) {

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
    public static boolean isNullOrEmpty(Set<?> objs) {

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
    public static boolean isEmpty(Set<?> objs) {

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
    public static Set removeDuplicate(Set<?> objs, Comparator comparator) {
        Set<Object> tempSet = new HashSet<Object>();

        if (objs == null || objs.isEmpty()) {
            return null;
        } else {

            if (comparator != null) {
                for (Object obj : objs) {
                    if (!isContain(tempSet, obj, comparator)) {
                        tempSet.add(obj);
                    }
                }


            } else {//comparator为null时，传统比较
                for (Object obj : objs) {
                    if (!tempSet.contains(obj)) {
                        tempSet.add(obj);
                    }
                }
            }

        }


        if (tempSet == null || tempSet.isEmpty()) {
            return null;
        } else
            return tempSet;
    }


    public static Set<Object> replace(Set<Object> objs, Object obj, Comparator comparator) {

        Object[] objects = SetUtils.toArray(objs);

        for (int i = 0; i < objs.size(); i++) {
            int compare = comparator.compare(i, obj);

            if (compare == 0) {
//                LogUtils.printLog(ListUtils.class, "compare true. value:" + compare);

                objects[i] = new Object();
            }

        }

        objs = ArrayUtils.toSet(objects);
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
    public static boolean isContain(Set<?> objs, Object obj, Comparator comparator) {

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


    public static Set addTop(Set Set, Object obj) {
        Set tempSet = new LinkedHashSet();
        tempSet.add(obj);
        tempSet.addAll(Set);

        return tempSet;
    }


    public static <T> T find(Set<T> Set, IEqual equal) {
        for (T i : Set) {
            if (equal.equal(i)) {
                return i;
            }

        }
        return null;
    }


    public static <T> Set<T> remove(Set<T> set, IEqual iEqual) {


        for (Iterator<T> it = set.iterator(); it.hasNext();) {
            T t = it.next();
            if (iEqual.equal(t)) {
                it.remove();  // ok
            }
        }

        Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (iEqual.equal(t)) {
                set.remove(t);
            }
        }
        return set;
    }

}