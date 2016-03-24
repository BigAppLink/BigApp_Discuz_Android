package com.kit.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.kit.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZogUtils {

    public static int COUNT = 0;

    public static String LOGUTILS_TAG = "APP";
    public static String LOGUTILS_IDENTIFY = "@";


    /**
     * 保存错误信息到文件中
     *
     * @param zogStr
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public static String saveLog2File(Context context, String tag, String zogStr) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        StringBuffer sb = new StringBuffer();

        sb.append(zogStr);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());


            String fileName = AppUtils.getAppName(context) + "-" + tag + time + "-" + timestamp + ".zog";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = AppConfig.CACHE_DATA_DIR + "zogs/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            ZogUtils.printLog(ZogUtils.class,
                    "an error occured while writing file..." + e);
        }
        return null;
    }


    /**
     * @param clazz Class
     * @param msg   String 消息
     * @return void 返回类型
     * @Title printLog
     * @Description 打印Log
     */
    public static void printLog(Class<?> clazz, String msg) {

        if (AppConfig.LOG) {
            if (clazz == null) {

                Log.i(LOGUTILS_TAG, msg);

            } else {
                Log.i(LOGUTILS_TAG, "【" + LOGUTILS_IDENTIFY + clazz.getName()
                        + "】 " + msg);
            }
        }
    }

    /**
     * @param clazz Class
     * @param msg   String 消息
     * @return void 返回类型
     * @Title printError
     * @Description 打印Log
     */
    public static void printError(Class<?> clazz, String msg) {

        if (AppConfig.LOG) {
            if (clazz == null) {

                Log.e(LOGUTILS_TAG, msg);

            } else {
                Log.e(LOGUTILS_TAG, "【" + LOGUTILS_IDENTIFY + clazz.getName()
                        + "】 " + msg);
            }
        }
    }


    /**
     * @param clazz  Class
     * @param object
     * @return void 返回类型
     * @Title printError
     * @Description 打印Object
     */
    public static void printObj(Class<?> clazz, Object object) {

        String msg = GsonUtils.toJson(object);

        if (AppConfig.LOG) {
            if (clazz == null) {

                Log.e(LOGUTILS_TAG, msg);

            } else {
                Log.e(LOGUTILS_TAG, "【" + LOGUTILS_IDENTIFY + clazz.getName()
                        + "】 " + msg);
            }
        }
    }

    /**
     * @param clazz  Class
     * @param object
     * @return void 返回类型
     * @Title printError
     * @Description 打印Object
     */
    public static void printObj(Class<?> clazz, Object object, String tag) {

        String msg = GsonUtils.toJson(object);

        if (AppConfig.LOG) {
            if (clazz == null) {

                Log.e(LOGUTILS_TAG, "tag: " + tag + " " + msg);

            } else {
                Log.e(LOGUTILS_TAG, "【" + LOGUTILS_IDENTIFY + clazz.getName()
                        + "】 " + "tag:" + tag +" "+ msg);
            }
        }
    }


    /**
     * @param clazz Class
     * @param msg   String 消息
     * @return void 返回类型
     * @Title printLog
     * @Description 打印Log
     */
    public static void printLog(Class<?> clazz, String msg, int count) {
        if (AppConfig.LOG) {
            if (COUNT < count) {

                if (clazz == null) {

                    Log.i(LOGUTILS_TAG, msg);

                } else {
                    Log.i(LOGUTILS_TAG, "【" + LOGUTILS_IDENTIFY + clazz.getName()
                            + "】 " + msg);
                }
            }
            COUNT++;
        }
    }


    /**
     * @param clazz Class
     * @param tag   String 标志
     * @param msg   String 消息
     * @return void 返回类型
     * @Title printLog
     * @Description 打印Log
     */
    public static void printLog(Class<?> clazz, String tag, String msg) {

        if (AppConfig.LOG) {
            if (clazz == null) {

                Log.i(tag, msg);

            } else {
                Log.i(tag, "【" + LOGUTILS_IDENTIFY + clazz.getName() + "】 "
                        + msg);
            }
        }
    }


    /**
     * @param
     * @return void 返回类型
     * @Title showException
     * @Description 显示Exception
     */
    public static void showException(Exception e) {
        if (AppConfig.SHOW_EXCEPTION) {
            ZogUtils.printError(ZogUtils.class, GsonUtils.toJson(e));
        }
    }

}
