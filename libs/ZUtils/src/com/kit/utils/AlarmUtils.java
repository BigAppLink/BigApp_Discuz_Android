package com.kit.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * 闹钟设置类
 */
public class AlarmUtils {

    public static final String SET_ALARM = "com.kit.set_alarm";


    /**
     * @param context
     * @param hour            24小时制
     * @param minute
     * @param second
     * @param millisecond
     * @param interval        时间间隔
     * @param intentBroadcast
     */
    public static void setRepeatingAlarm(Context context, Intent intentBroadcast,
                                         int hour, int minute,
                                         int second, int millisecond,
                                         long interval) {
        AlarmManager alarm = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();//Calendar是一类可以将时间转化成绝对时间毫秒数的一个类
        calendar.set(Calendar.HOUR_OF_DAY, hour);

        calendar.set(Calendar.MINUTE, minute);

        calendar.set(Calendar.SECOND, second);

        calendar.set(Calendar.MILLISECOND, millisecond);

        intentBroadcast.setAction(SET_ALARM);


        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, sender);//set the properties of alarm

    }


    // 设置闹铃时间(毫秒)
    public static void setAlarm(Context context, Intent intentBroadcast, long timeInMillis) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);


        // LogUtils.printLog(AlarmUtils.class, "requestCode:" + requestCode);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);

        // AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
        // 如果需要重复执行，使用setRepeating方法，倒数第二参数为间隔时间,单位为毫秒

        // /* 设置闹钟 */
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);

        // requestCode++;

    }


    // 设置闹铃时间(毫秒)
    public static void setAlarm(Context context, PendingIntent pendingIntent, long timeInMillis) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);


        // LogUtils.printLog(AlarmUtils.class, "requestCode:" + requestCode);


        // AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
        // 如果需要重复执行，使用setRepeating方法，倒数第二参数为间隔时间,单位为毫秒

        // /* 设置闹钟 */
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        //requestCode++;

    }


    // 设置闹铃时间(毫秒)
    public static void unsetAlarm(Context context, Intent intentBroadcast) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentBroadcast, 0);

        // AlarmManager.RTC_WAKEUP休眠时会运行，如果是AlarmManager.RTC,在休眠时不会运行
        // 如果需要重复执行，使用setRepeating方法，倒数第二参数为间隔时间,单位为毫秒

        // /* 设置闹钟 */
        am.cancel(sender);
    }


    // 设置闹铃时间(毫秒)
    public static void unsetAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);


        // /* 设置闹钟 */
        am.cancel(pendingIntent);
    }


    public static void setRepeatingAlarm(Context context, Intent intentBroadcast, long time,
                                         long interval) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intentBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);


        /* 设置周期闹钟 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, interval,
                pendingIntent);
    }


    public static void setRepeatingAlarm(Context context, PendingIntent pendingIntent, long time,
                                         long interval) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        /* 设置周期闹钟 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, interval,
                pendingIntent);
    }



}
