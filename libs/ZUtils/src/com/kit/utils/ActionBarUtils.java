package com.kit.utils;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Window;

import java.lang.reflect.Method;

/**
 * Created by Zhao on 14-10-7.
 */
public class ActionBarUtils {

    /**
     * 启用嵌入式（沉浸式）ActionBarTab
     *
     * @param actionBar
     */
    public static void enableEmbeddedTabs(Activity activity, Object actionBar) {
        try {
            Method setHasEmbeddedTabsMethod = actionBar.getClass().getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, true);
        } catch (Exception e) {
            ZogUtils.printLog(activity.getClass(), e.getMessage().toString());

        }
    }


    /**
     * 设置NavagationBar(ActionBar)返回按钮
     *
     * @param activity
     */
    public static void setHomeBack(Activity activity, int resDrawableBack, int resStringBack) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            activity.getActionBar().setHomeActionContentDescription(resStringBack);
            activity.getActionBar().setHomeAsUpIndicator(resDrawableBack);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            activity.getActionBar().setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.getActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getActionBar().setDisplayShowHomeEnabled(true);
            activity.getActionBar().setTitle(resStringBack);
            activity.getActionBar().setDisplayShowTitleEnabled(true);
        }


    }

    public static void setHomeActionBar(AppCompatActivity activity,int resBackId) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(resBackId);
    }

        /**
         * 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
         *
         * @param featureId
         * @param menu      onMenuOpened方法中调用
         */
    public static void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
    }
}
