package com.kit.utils.pinyin;

import java.util.Comparator;


/**
 * 拼音比较器
 *
 */
public class PinyinComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        String str1 = PingYinUtils.getPingYin((String) o1);
        String str2 = PingYinUtils.getPingYin((String) o2);
        return str1.compareTo(str2);
    }
}