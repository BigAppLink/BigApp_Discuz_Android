package com.kit.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Joey.Zhao Email: laozhao1005@gmail.com
 * @date 2014年3月20日
 */
public class KeyboardUtils {

    /**
     * 一定要注意，隐藏键盘的时候，要保证view可见
     *
     * @param context
     * @param view
     */
    public static void hiddenKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

//        if (view.getVisibility() != View.VISIBLE) {
//            LogUtils.printLog(KeyboardUtils.class, "view must visible!!!");
//        }


        view.clearFocus();
        if (context != null && imm != null) {

            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                imm.hideSoftInputFromWindow(((Activity) context)
                                .getCurrentFocus().getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

//    @Deprecated
//    public static void hiddenKeyboard(Context context) {
//        InputMethodManager im = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        // 获取输入法是否打开
//        boolean isOpen = im.isActive();
//        if (context != null && im != null) {
//            if (isOpen) {
//                try {
//                    im.hideSoftInputFromWindow(((Activity) context)
//                                    .getCurrentFocus().getApplicationWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//                } catch (Exception e) {
//                    LogUtils.showException(e);
//                }
//            } else {
//                try {
//                    im.hideSoftInputFromWindow(((Activity) context)
//                                    .getCurrentFocus().getApplicationWindowToken(),
//                            InputMethodManager.SHOW_FORCED);
//                } catch (Exception e) {
//                    LogUtils.showException(e);
//                }
//            }
//        }
//    }


    /**
     * 强制显示软键盘
     *
     * @param context
     * @param view    为接受软键盘输入的视图
     */
    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        imm.showSoftInput(view,
                InputMethodManager.RESULT_UNCHANGED_SHOWN);

//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_NOT_ALWAYS);

    }


    /**
     * 键盘是否在打开状态
     *
     * @param context
     * @return
     */
    public static boolean isOpen(Context context) {

        InputMethodManager im = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // 获取输入法是否打开
        boolean isOpen = im.isActive();
        return isOpen;
    }
}
