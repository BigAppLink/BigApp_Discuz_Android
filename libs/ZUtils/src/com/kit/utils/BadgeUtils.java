package com.kit.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;

//BadgeUtil provides static utility methods to set "badge count" on Launcher (by Samsung, LG). 
//Currently, it's working from Android 4.0. 
//But some devices, which are released from the manufacturers, are not working.

public class BadgeUtils {

    private static final String TAG = "BadgeUtil";

    private static final String ACTION_BADGE_COUNT_UPDATE = "android.intent.action.BADGE_COUNT_UPDATE";

    private static final String EXTRA_BADGE_COUNT = "badge_count";

    private static final String EXTRA_BADGE_COUNT_PACKAGE_NAME = "badge_count_package_name";

    private static final String EXTRA_BADGE_COUNT_CLASS_NAME = "badge_count_class_name";

    private static final Uri CONTENT_URI = Uri.parse("content://" + "com.android.badge" + "/" + "badge");


    /**
     * Set badge count<br/>
     * 针对 Samsung / xiaomi / sony 手机有效
     *
     * @param context The context of the application package.
     * @param count   Badge count to be set
     */
    public static void setBadgeCount(Context context, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        ZogUtils.printError(BadgeUtils.class, "Build.MANUFACTURER:" + Build.MANUFACTURER);

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            sendToXiaoMi(context, count);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("zuk")) {
            sendToZUK(context, count);
        } else {
            setBadgeCount4Default(context, count);
//            Toast.makeText(context, "Not Support", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * 向小米手机发送未读消息数广播
     *
     * 不一定起作用 待验证
     *
     * @param count
     */
    private static void sendToXiaoMi(Context context, int count) {
        try {
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);

            // 设置信息数-->这种发送必须是miui 6才行
            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));
        } catch (Exception e) {
            ZogUtils.showException(e);
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text"
                    , String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }


    /**
     * 向索尼手机发送未读消息数广播<br/>
     * 据说：需添加权限：
     * <uses-permission
     * android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
     *
     * @param count
     */
    private static void sendToSony(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();

        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");

        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE"
                , isShow);//是否显示

        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME"
                , launcherClassName);//启动页

        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE"
                , String.valueOf(count));//数字

        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME"
                , context.getPackageName());//包名

        context.sendBroadcast(localIntent);
    }


    /**
     * 向三星手机发送未读消息数广播
     *
     * @param count
     */
    private static void sendToSamsumg(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }


    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     * "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }

    /**
     * 向ZUK手机发送未读消息数广播
     *
     * @param count
     */
    @TargetApi(11)
    private static void sendToZUK(Context context, int count) {
        Bundle extra = new Bundle();
        ArrayList<String> ids = new ArrayList<String>();
// 以列表形式传递快捷方式id，可以添加多个快捷方式id
        ids.add("custom_id_1");
        ids.add("custom_id_2");
        extra.putStringArrayList("app_shortcut_custom_id", ids);
        extra.putInt("app_badge_count", count);
        Bundle b = null;
        b = context.getContentResolver().call(CONTENT_URI, "setAppBadgeCount", null, extra);
        boolean result = false;
        if (b != null) {
            result = true;
        } else {
            result = false;
        }
        return;

    }

    /**
     * Set badge count
     *
     * @param context The context of the application package.
     * @param count   Badge count to be set
     */
    private static void setBadgeCount4Default(Context context, int count) {
        Intent badgeIntent = new Intent(ACTION_BADGE_COUNT_UPDATE);
        badgeIntent.putExtra(EXTRA_BADGE_COUNT, count);
        badgeIntent.putExtra(EXTRA_BADGE_COUNT_PACKAGE_NAME, context.getPackageName());
        badgeIntent.putExtra(EXTRA_BADGE_COUNT_CLASS_NAME, getLauncherClassName(context));
        context.sendBroadcast(badgeIntent);
    }

    /**
     * 重置、清除Badge未读显示数<br/>
     *
     * @param context
     */
    public static void resetBadgeCount(Context context) {
        setBadgeCount(context, 0);
    }


}