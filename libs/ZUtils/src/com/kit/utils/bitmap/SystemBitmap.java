package com.kit.utils.bitmap;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class SystemBitmap {
    public static BitmapDrawable getBackground(Context context) {

        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        float step = 0;
        // 计算出屏幕的偏移量
        step = (bm.getWidth() - 480) / (7 - 1);
        // 截取相应屏幕的Bitmap
        DisplayMetrics dm = new DisplayMetrics();
        Bitmap pbm = Bitmap.createBitmap(bm, (int) (5 * step), 0,
                dm.widthPixels, dm.heightPixels);

        return new BitmapDrawable(pbm);
    }

    public static Bitmap getWallpaper(Context context) {

        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

        return bm;
        // return new BitmapDrawable(bm);
    }

    public static Drawable getWallpaperDrawable(Context context) {

        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        Drawable d = BitmapUtils.Bitmap2Drawable(bm);
        if (bm != null && bm.isRecycled()) {
            bm.recycle();
        }
        return d;

    }


    public static Bitmap getWallpaperDrawableWithSizeBitmap(Context context,
                                                            int width, int height, int alpha) {

        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        bm = BitmapUtils.getAdpterBitmap(bm, width, height);
        bm = BitmapUtils.setAlpha(bm, alpha);

        return bm;
    }

    public static Drawable getWallpaperDrawableWithSizeDrawable(Context context,
                                                                int width, int height, int alpha) {

        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        bm = BitmapUtils.getAdpterBitmap(bm, width, height);
        Drawable d = BitmapUtils.Bitmap2Drawable(bm);
        bm = BitmapUtils.setAlpha(bm, alpha);
        if (bm != null && bm.isRecycled()) {
            bm.recycle();
        }
        return d;
    }
}
