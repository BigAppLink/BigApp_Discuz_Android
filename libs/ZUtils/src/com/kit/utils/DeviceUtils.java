package com.kit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.util.TypedValue;
import android.view.Display;

import java.lang.reflect.Field;

public class DeviceUtils {
    /**
     * 判定是否有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean isShowNavigationBar(Context context) {
        if (getAPIVersion() >= 14) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
            if (resourceId > 0) {
                return resources.getBoolean(resourceId);
            }
        }
        return false;
    }



    /**
     * Get the screen height.
     *
     * @param context
     * @return the screen height
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenHeight(Context context) {

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
        return display.getHeight();
    }

    /**
     * Get the screen width.
     *
     * @param context
     * @return the screen width
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenWidth(Context context) {

        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }


    /**
     * 获取虚拟按键高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取虚拟按键宽度
     * @param context
     * @return
     */
    public static int getNavigationBarWidth(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_width", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    /**
     * 获取虚拟按键横向时候的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeithtLandscape(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


    /**
     * 获取ActionBar的高度
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * 获取设备的APILevel
     *
     * @return
     */
    public static int getAPIVersion() {
        int APIVersion;
        try {
            APIVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            APIVersion = 0;
        }
        return APIVersion;
    }


    /**
     * 判定屏幕是否点亮
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();

        return screen;
    }


    /**
     * 是否解锁
     *
     * @param context
     * @return
     */
    public static boolean isKeyguard(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        return flag;
    }


}
