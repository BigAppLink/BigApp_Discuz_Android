package com.kit.imagelib.banner.utils;

import android.content.Context;

/*
 * 基本功能：工具类
 * 创建：王杰
 *
 */
public class Convert {
    /**
     * 基本功能：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
