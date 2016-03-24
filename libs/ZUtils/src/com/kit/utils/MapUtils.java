package com.kit.utils;

import com.kit.interfaces.IEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUtils {


    /**
     * 深copy
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Map deepCopy(Map src) throws IOException,
            ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(
                byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        Map dest = (Map) in.readObject();
        return dest;
    }


    /**
     * @param
     * @return Object 返回类型
     * @Title getOne
     * @Description 得到一个
     */
    public static Object getOne(Map objs) {

        int index = RandomUtils.getRandomIntNum(0, objs.size() - 1);

        return objs.get(index);

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定数组是否是空指针或里面的数据是null
     */
    public static boolean isNullOrContainEmpty(Map map) {

        if (isNullOrEmpty(map)) {
            return true;
        } else {
            Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> entry = it.next();
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                if (entry.getValue() != null)
                    return false;
            }
            return true;
        }

    }


    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定Map是否是空指针或里面没有数据
     */
    public static boolean isNullOrEmpty(Map objs) {

        if (objs == null || objs.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return boolean 返回类型
     * @Title isNullOrEmpty
     * @Description 判定Map里面没有数据
     */
    public static boolean isEmpty(Map objs) {

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
    public static Map removeDuplicateByKey(Map map, Comparator comparator) {
        Map tempMap = new HashMap();

        if (isNullOrEmpty(map)) {
            return null;
        } else {

            if (comparator != null) {


                Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<?, ?> entry = it.next();
//                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    if (!isContainKey(tempMap, entry.getKey(), comparator)) {
                        tempMap.put(entry.getKey(), entry.getValue());
                    }

                }


            } else {//comparator为null时，传统比较

                Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<?, ?> entry = it.next();
//                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    if (tempMap.containsKey(entry.getKey())) {
                        tempMap.put(entry.getKey(), entry.getValue());
                    }

                }
            }

        }


        if (isNullOrEmpty(map)) {
            return null;
        } else
            return tempMap;
    }


    /**
     * @param
     * @return boolean 返回类型
     * @Description 去重
     */
    public static Map removeDuplicateByValue(Map map, Comparator comparator) {
        Map tempMap = new HashMap();

        if (isNullOrEmpty(map)) {
            return null;
        } else {

            if (comparator != null) {
                Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<?, ?> entry = it.next();
                    if (!isContainKey(tempMap, entry.getValue(), comparator)) {
                        tempMap.put(entry.getValue(), entry.getValue());
                    }

                }


            } else {//comparator为null时，传统比较

                Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<?, ?> entry = it.next();
                    if (tempMap.containsKey(entry.getValue())) {
                        tempMap.put(entry.getValue(), entry.getValue());
                    }
                }
            }

        }


        if (isNullOrEmpty(map)) {
            return null;
        } else
            return tempMap;
    }



    /**
     * 根据comparator判定是否包含
     *
     * @param map
     * @param value
     * @param comparator
     * @return
     */
    public static boolean isContainValue(Map map, Object value, Comparator comparator) {

        if (comparator != null) {

            Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> entry = it.next();
//                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                int compare = comparator.compare(entry.getValue(), value);

                if (compare == 0) {
                    return true;
                }

            }
            return false;
        } else return map.containsValue(value);

    }


    /**
     * 根据comparator判定是否包含
     *
     * @param map
     * @param key
     * @param comparator
     * @return
     */
    public static boolean isContainKey(Map map, Object key, Comparator comparator) {

        if (comparator != null) {
            Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<?, ?> entry = it.next();
//                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                int compare = comparator.compare(entry.getKey(), key);

                if (compare == 0) {
                    return true;
                }

            }
            return false;
        } else return map.containsKey(key);

    }


    public static Map<?, ?> findByKey(Map map, IEqual iEqual) {

        Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<?, ?> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if (iEqual.equal(entry.getValue())) {

                Map<Object, Object> get = new HashMap<>();
                get.put(entry.getKey(), entry.getValue());

                return get;
            }

        }


        return null;
    }


    public static Map removeByKey(Map map, IEqual iEqual) {

        Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<?, ?> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if (iEqual.equal(entry.getKey())) {

                Map<Object, Object> get = new HashMap<>();
                get.put(entry.getKey(), entry.getValue());

                map.remove(entry.getKey());
                return get;
            }

        }
        return map;
    }



    public static Map removeByValue(Map map, IEqual iEqual) {

        Iterator<Map.Entry<?, ?>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<?, ?> entry = it.next();
//            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if (iEqual.equal(entry.getValue())) {

                Map<Object, Object> get = new HashMap<>();
                get.put(entry.getKey(), entry.getValue());

                map.remove(entry.getKey());
                return get;
            }

        }
        return map;
    }

}