package com.kit.imagelib;

import android.os.Environment;

/**
 * @author join
 */
public class Config {

    public static int limit;
    static String takePhotoSavePath;

    static {
        limit = 4;
        takePhotoSavePath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera";
    }

    public static void setLimit(int limit) {
        Config.limit = limit;
    }

    public static int getLimit() {
        return limit;
    }

    public static String getTakePhotoSavePath() {
        return takePhotoSavePath;
    }

    public static void setTakePhotoSavePath(String takePhotoSavePath) {
        Config.takePhotoSavePath = takePhotoSavePath;
    }
}
