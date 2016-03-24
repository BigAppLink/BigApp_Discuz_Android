package com.kit.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Zhao on 14-8-13.
 */
public class VibratorUtils {


    /**
     * 轻微振动(1次)
     *
     * @param mContext
     */
    public static void lessVibrate(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        long[] pattern = {500, 500}; // /OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);//-1不重复，非-1为从pattern的指定下标开始重复
    }


    /**
     * 模拟心跳振动(1次)
     *
     * @param mContext
     */
    public static void heartbeat(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        long[] pattern = {50, 50, 400, 30, 500, 500}; // /OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);//-1不重复，非-1为从pattern的指定下标开始重复
    }


    /**
     * 模拟心跳振动(持续)
     *
     * @param mContext
     */
    public static void heartbeatTwice(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        long[] pattern = {800, 50, 400, 30, 800, 50, 400, 30}; // OFF/ON/OFF/ON/...
        vibrator.vibrate(pattern, -1);//-1不重复，非-1为从pattern的指定下标开始重复
    }

    /**
     * 模拟心跳振动(持续)
     *
     * @param mContext
     */
    public static void heartbeating(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        long[] pattern = {800, 50, 400, 30}; // OFF/ON/OFF/ON/...
        vibrator.vibrate(pattern, 2);//-1不重复，非-1为从pattern的指定下标开始重复
    }

}
