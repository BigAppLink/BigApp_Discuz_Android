package com.kit.utils;

public class WebUtils {

    public static String getStartWith(String url) {
        if(StringUtils.isEmptyOrNullOrNullStr(url))
            return null;

        int end = url.indexOf("://");

        ZogUtils.printError(WebUtils.class, "end:" + end);

        if (end == -1)
            end = url.length();

        return url.substring(0, end);
    }



}
