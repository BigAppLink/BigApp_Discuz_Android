package com.youzu.clan.base.util;

import android.text.TextUtils;

public final class StringUtils extends com.kit.utils.StringUtils {
    public static String get(String str) {
        if (str != null && !TextUtils.isEmpty(str.trim())) {
//			return HtmlUtils.delHTMLTag(str);

            return str;

        }
        return "--";
    }



}
