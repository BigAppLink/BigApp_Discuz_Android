package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.kit.utils.DensityUtils;
import com.youzu.android.framework.bitmap.BitmapDisplayConfig;
import com.youzu.android.framework.bitmap.callback.BitmapLoadCallBack;
import com.youzu.android.framework.bitmap.callback.BitmapLoadFrom;
import com.youzu.clan.R;
import com.youzu.clan.base.widget.ImageHelper;

/**
 * Created by Zhao on 15/7/2.
 */
public class BitmapUtils {

    public static com.youzu.android.framework.BitmapUtils bu;

    public static com.youzu.android.framework.BitmapUtils getInstance(Context context) {
        if (bu == null) {
            bu = new com.youzu.android.framework.BitmapUtils(context, ImageUtils.getDefaultCacheDir().getPath());
        }
        return bu;
    }

    public static BitmapDisplayConfig getConfig(Drawable loadingDrawable, Drawable failedDrawable) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(failedDrawable);
        config.setLoadingDrawable(loadingDrawable);
        return config;
    }

    public static BitmapDisplayConfig getConfig(Context context, int loadingDrawable, int failedDrawable) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(context.getResources().getDrawable(failedDrawable));
        config.setLoadingDrawable(context.getResources().getDrawable(loadingDrawable));
        return config;
    }

    public static <T extends View> void display(Context context, T container, String uri, Drawable loadingDrawable, Drawable failedDrawable) {
        if (!TextUtils.isEmpty(uri)) {
            getInstance(context).display(container, uri, getConfig(loadingDrawable, failedDrawable));
        }
    }

    public static <T extends View> void display(Context context, T container, String uri, int loadingDrawable, int failedDrawable) {
        if (!TextUtils.isEmpty(uri)) {
            getInstance(context).display(container, uri, getConfig(context, loadingDrawable, failedDrawable));
        }
    }


    public static <T extends View> void display(Context context, T container, String uri) {
        if (!TextUtils.isEmpty(uri)) {
            getInstance(context).display(container, uri,
                    getConfig(context.getResources().getDrawable(R.drawable.no_picture)
                            , context.getResources().getDrawable(R.drawable.no_picture)));
        }
    }


    public static void display(ImageView view, String url, int defaultRes) {
        Context context = view.getContext();
        BitmapDisplayConfig config = getConfig(context, defaultRes, defaultRes);
        getInstance(context).display(view, url, config);
    }

    public static void display(ImageView view, String url, int defaultRes, final int radiusDp) {
        final Context context = view.getContext();
        BitmapDisplayConfig config = getConfig(context, defaultRes, defaultRes);

        getInstance(context).display(view, url, config, new BitmapLoadCallBack<ImageView>() {

            @Override
            public void onLoadCompleted(ImageView arg0, String arg1,
                                        Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
                int pixels = DensityUtils.dip2px(context, radiusDp);
                arg0.setImageBitmap(ImageHelper.getRoundedCornerBitmap(arg2, pixels));
            }

            @Override
            public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {

            }
        });
    }
}
