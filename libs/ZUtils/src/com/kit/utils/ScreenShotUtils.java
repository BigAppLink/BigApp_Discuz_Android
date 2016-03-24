package com.kit.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.kit.utils.bitmap.BitmapUtils;

import java.io.File;
import java.io.IOException;

public class ScreenShotUtils {

    public static Bitmap takeScreenShot(Activity activity) {

        //构建Bitmap
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap b1 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        b1 = view.getDrawingCache();

        ZogUtils.printLog(ScreenShotUtils.class, b1 + "");

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();

        Bitmap b = null;

        try {
            // 去掉标题栏
            b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                    - statusBarHeight);
        } catch (Exception e) {
            b = b1;
        }

      //  view.destroyDrawingCache();
        return b;
    }



    public static void shoot(Activity a, File file) throws IOException{
        if (file == null) {
            return;
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        BitmapUtils.saveBitmap(takeScreenShot(a), file);
    }
}