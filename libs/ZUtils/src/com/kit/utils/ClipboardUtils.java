package com.kit.utils;

import android.content.Context;
import android.text.ClipboardManager;

/**
 * Created by Zhao on 14/11/17.
 */
public class ClipboardUtils {
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(Context context, String content) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!StringUtils.isEmptyOrNullOrNullStr(content)) {
            cmb.setText(content.trim());
        }
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
