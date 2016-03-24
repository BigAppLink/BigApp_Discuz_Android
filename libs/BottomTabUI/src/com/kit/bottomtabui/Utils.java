package com.kit.bottomtabui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Zhao on 15/8/24.
 */
public class Utils {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static Bitmap getAdpterBitmap(Bitmap bmp, int width,
                                         int height) {

        Bitmap outBitmap = null;

        Bitmap resizeBmp = bmp;
        try {
            if (bmp.getHeight() > height) {// 为了节省内存，如果高度过高，剪切一个高度等于所需高度的，宽度则再做变化后到最后再做剪切
                resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                        height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        float scaleWidth = (float) MathExtend.divide(width,
                resizeBmp.getWidth());
        float scaleHeight = (float) MathExtend.divide(height,
                resizeBmp.getHeight());

        float scale = 1;

        if (scaleWidth > scaleHeight) {
            scale = scaleWidth;
        } else {
            scale = scaleHeight;
        }

        if (scale != 0) {
            Matrix matrix = new Matrix();

            matrix.postScale(scale, scale);

            int w = resizeBmp.getWidth();
            int h = resizeBmp.getHeight();
            try {
                try {
                    resizeBmp = Bitmap.createBitmap(resizeBmp, 0, 0, w, h,
                            matrix, true);

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("我靠，scale居然等于0");
        }

        try {

            outBitmap = Bitmap.createBitmap(resizeBmp,
                    (resizeBmp.getWidth() - width) / 2,
                    (resizeBmp.getHeight() - height) / 2, width, height);
            // outBitmap = Bitmap.createBitmap(resizeBmp, 0, 0, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outBitmap == null)
            outBitmap = bmp;
        return outBitmap;
    }

}
