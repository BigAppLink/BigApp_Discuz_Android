package com.kit.utils.intentutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kit.app.ActivityManager;
import com.kit.utils.GsonUtils;
import com.kit.utils.ZogUtils;

public class IntentUtils extends IntentBaseUtils {
    private static final String KEY_DATA = "zhao_bundle_data";


    /**
     * 发送广播
     *
     * @param packageContext
     * @param data
     * @param action
     */
    public static void sendBroadcast(Context packageContext,
                                     BundleData data, String action) {
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        intent.setAction(action);
        packageContext.sendBroadcast(intent);
    }

    /**
     * 发送广播
     *
     * @param activity
     * @param data
     * @param resultCode
     */
    public static void setResult(Activity activity, int resultCode,
                                 BundleData data) {
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        activity.setResult(resultCode);
        activity.finish();
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Context packageContext, Class<?> cls,
                                              BundleData data) {
        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        packageContext.startActivity(intent);
    }

    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoNextActivity(Context packageContext, Class<?> cls,
                                        BundleData data) {
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        packageContext.startActivity(intent);
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     * @param isCloseThis    是否关闭当前界面
     */
    public static void gotoNextActivity(Context packageContext, Class<?> cls,
                                        BundleData data, boolean isCloseThis) {
        if (isCloseThis) {
            ((Activity) packageContext).finish();
        }
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        packageContext.startActivity(intent);
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoNextActivity(Context packageContext, Class<?> cls,
                                        BundleData data, int requestFlag) {
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivityForResult(intent, requestFlag);

    }


    /**
     * 跳转界面并传值
     *
     * @param fragment
     * @param cls
     * @param data
     */
    public static void gotoNextActivity(Fragment fragment, Class<?> cls,
                                        BundleData data, int requestFlag) {
        Intent intent = new Intent();//创建Intent对象
        pushData(intent, data);
        intent.setClass(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestFlag);

    }


    /**
     * 跳转界面
     *
     * @param packageContext
     * @param cls
     * @param isCloseThis
     */
    public static void gotoSingleNextActivity(Context packageContext, Class<?> cls, boolean isCloseThis) {
        if (isCloseThis) {
            ((Activity) packageContext).finish();
        }

        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivity(intent);
    }

    /**
     * 从Service到Activity
     *
     * @param packageContext
     * @param cls
     */
    public static void gotoSingleNextActivityFromReceiver(Context packageContext,
                                                          Class<?> cls, BundleData data, boolean isCloseThis) {
        if (isCloseThis && (packageContext instanceof Activity)) {
            ((Activity) packageContext).finish();
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        packageContext.startActivity(intent);
    }

    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Context packageContext, Class<?> cls,
                                              BundleData data, boolean isCloseThis) {
        if (isCloseThis && (packageContext instanceof Activity)) {
            ((Activity) packageContext).finish();
        }

        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivity(intent);
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Context packageContext, Class<?> cls,
                                              BundleData data, int requestFlag) {
        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivityForResult(intent, requestFlag);
    }


    /**
     * 跳转界面并传值
     *
     * @param fragment
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Fragment fragment, Class<?> cls,
                                              BundleData data, int requestFlag) {
        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestFlag);
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Context packageContext, Class<?> cls,
                                              BundleData data, boolean isCloseThis, int requestFlag) {

        if (isCloseThis) {
            ((Activity) packageContext).finish();
        }

        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivityForResult(intent, requestFlag);
    }


    /**
     * 跳转界面并传值
     *
     * @param fragment
     * @param cls
     * @param data
     */
    public static void gotoSingleNextActivity(Fragment fragment, Class<?> cls,
                                              BundleData data, int requestFlag, boolean isCloseThis) {

        if (isCloseThis) {
            fragment.getActivity().finish();
        }

        if (ActivityManager.getInstance().isExistActivity(cls)) {
            ActivityManager.getInstance().popActivity(cls);
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestFlag);
        ZogUtils.printError(IntentUtils.class,"gotoSingleNextActivity form Fragment");
    }


    /**
     * 跳转界面并传值
     *
     * @param packageContext
     * @param cls
     * @param data
     * @param isCloseThis    是否关闭当前界面
     */
    public static void gotoNextActivity(Context packageContext, Class<?> cls,
                                        BundleData data, boolean isCloseThis, int requestFlag) {
        if (isCloseThis) {
            ((Activity) packageContext).finish();
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(packageContext, cls);
        ((Activity) packageContext).startActivityForResult(intent, requestFlag);
    }

    /**
     * 跳转界面并传值
     *
     * @param fragment
     * @param cls
     * @param data
     * @param isCloseThis 是否关闭当前界面
     */
    public static void gotoNextActivity(Fragment fragment, Class<?> cls,
                                        BundleData data, boolean isCloseThis, int requestFlag) {
        if (isCloseThis) {
            ((Activity) fragment.getActivity()).finish();
        }

        Intent intent = new Intent();
        pushData(intent, data);
        intent.setClass(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestFlag);
    }


    /**
     * 压入数据
     *
     * @param intent
     * @param data
     */
    public static void pushData(Intent intent, BundleData data) {
        String value = GsonUtils.toJson(data);
        intent.putExtra(KEY_DATA, value);
    }


    /**
     * 获取传过去的值
     *
     * @return
     */
    public static BundleData getData(Intent intent) {
        BundleData bundleData = null;

        try {
            String bundleDataStr = intent.getExtras().getString(KEY_DATA);
            bundleData = GsonUtils.getObj(bundleDataStr, BundleData.class);
        } catch (Exception e) {
            ZogUtils.showException(e);
        }
        return bundleData;
    }


}
