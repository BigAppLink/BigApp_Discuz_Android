package com.kit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

public class NetWorkUtils {

    public boolean isNetWorkOpen, isMobileDataEnable;
    public Context context;
    public ConnectivityManager mConnectivityManager;
    public WifiManager wm;

    public NetWorkUtils(Context context) {
        this.context = context;
        mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        isMobileDataEnable = isMobileNetWorkEnabled();
    }

    /**
     * WIFI网络开关
     */
    public void toggleWiFi(boolean enabled) {
        wm.setWifiEnabled(enabled);
    }

    public boolean isWiFiEnabled() {

        return wm.isWifiEnabled();
    }

    public boolean isNetWorkAvaliable() {

        if (mConnectivityManager == null) {
            return false;
        }

        NetworkInfo networkinfo = mConnectivityManager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    private boolean isMobileNetWorkEnabled() {

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method getMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(mConnectivityManager.getClass()
                    .getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(mConnectivityManager);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的getMobileDataEnabled(boolean)方法
            getMobileDataEnabledMethod = iConMgrClass
                    .getDeclaredMethod("getMobileDataEnabled");
            // 设置getMobileDataEnabled方法可访问
            getMobileDataEnabledMethod.setAccessible(true);
            // 调用getMobileDataEnabled方法
            return (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public Object invokeMethod(String methodName, Object[] arg)
            throws Exception {
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = null;
        if (arg != null) {
            argsClass = new Class[1];
            argsClass[0] = arg.getClass();
        }
        @SuppressWarnings("unchecked")
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, arg);
    }

    @SuppressWarnings("rawtypes")
    public Object invokeBooleanArgMethod(String methodName, boolean value)
            throws Exception {
        Class ownerClass = mConnectivityManager.getClass();
        Class[] argsClass = new Class[1];
        argsClass[0] = boolean.class;
        @SuppressWarnings("unchecked")
        Method method = ownerClass.getMethod(methodName, argsClass);
        return method.invoke(mConnectivityManager, value);
    }

    /**
     * 移动网络开关
     */
    public void toggleMobileData(boolean enabled) {
        // ConnectivityManager conMgr = (ConnectivityManager) context
        // .getSystemService(Context.CONNECTIVITY_SERVICE);

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(mConnectivityManager.getClass()
                    .getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(mConnectivityManager);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);

            isMobileDataEnable = enabled;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String getNetworkTypeReabable(Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = conMgr.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isAvailable() && activeNetInfo.isConnected()) {
            switch (activeNetInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return "wifi";
                case ConnectivityManager.TYPE_MOBILE:
                case ConnectivityManager.TYPE_MOBILE_DUN:
                case ConnectivityManager.TYPE_MOBILE_HIPRI:
                case ConnectivityManager.TYPE_MOBILE_MMS:
                case ConnectivityManager.TYPE_MOBILE_SUPL:
                case ConnectivityManager.TYPE_WIMAX:
                    return "mobile";
                default:
                    return "mobile";


            }
        } else
            return "none";
    }


    public static int getNetworkType(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectionManager.getActiveNetworkInfo().getType();
    }

}
