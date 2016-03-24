package com.kit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhao on 15/10/14.
 */
public class RegexUtils {


    /**
     * 正则表达式得到中间的部分
     *
     * @param content
     * @param startWithRegex 开始符号的正则
     * @param endWithRegex   结束符号的正则
     * @return
     */
    public static String getMidddle(String content, String startWithRegex, String endWithRegex) {
//        String regex = "\\[url\\](.*?)\\[/url\\]";

        String regex = startWithRegex + "(.*?)" + endWithRegex;

        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (matcher.find()) {
            String got = matcher.group(0);
            return got.substring(startWithRegex.length(),got.length()-endWithRegex.length());
        }
        return null;
    }
}