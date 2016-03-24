package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.kit.config.AppConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class ImageBaseUtils {

    /**
     * 初始化 ImageLoader
     *
     * @param context
     */
    public static ImageLoader initImageLoader(Context context) {

        DisplayImageOptions options = getDefaultDisplayImageOptions();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context.getApplicationContext())
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
                .discCache(new UnlimitedDiskCache(getDefaultImageCacheDir()))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(
                        new BaseImageDownloader(context.getApplicationContext()))
                .defaultDisplayImageOptions(options)
//                .writeDebugLogs()
                .build();


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        return imageLoader;

    }

    public static File getDefaultImageCacheDir(){
        File cacheDir = new File(AppConfig.CACHE_IMAGE_DIR);
        return  cacheDir;
    }

    //
//    public static void display(ImageView view, String url, int defaultRes) {
//        Context context = view.getContext();
//        BitmapDisplayConfig config = new BitmapDisplayConfig();
//        Drawable drawable = context.getResources().getDrawable(defaultRes);
//        config.setLoadingDrawable(drawable);
//        config.setLoadFailedDrawable(drawable);
//        new BitmapUtils(context).display(view, url, config);
//    }
//
//    public static void display(ImageView view, String url, int defaultRes, final int radiusDp) {
//        final Context context = view.getContext();
//        BitmapDisplayConfig config = new BitmapDisplayConfig();
//        Drawable drawable = context.getResources().getDrawable(defaultRes);
//        config.setLoadingDrawable(drawable);
//        config.setLoadFailedDrawable(drawable);
//        new BitmapUtils(context).display(view, url, config, new BitmapLoadCallBack<ImageView>() {
//
//            @Override
//            public void onLoadCompleted(ImageView arg0, String arg1,
//                                        Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
//                int pixels = ScreenUtils.dip2px(context, radiusDp);
//                arg0.setImageBitmap(ImageHelper.getRoundedCornerBitmap(arg2, pixels));
//            }
//
//            @Override
//            public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
//
//            }
//        });
//    }
//
//

    public static DisplayImageOptions getDefaultDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false).delayBeforeLoading(100)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
                .cacheInMemory(true).cacheOnDisk(true)
//                .showImageForEmptyUri(R.drawable.default_pic)
//                .showImageOnFail(R.drawable.default_pic)
                .build();
        return options;

    }



//
//    public static DisplayImageOptions getAvatarDisplayImageOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(false).delayBeforeLoading(100)
//                .considerExifParams(false)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
//                .cacheInMemory(true).cacheOnDisk(true)
//                .showImageForEmptyUri(R.drawable.ic_protrait_solid)
//                .showImageOnFail(R.drawable.ic_protrait_solid)
//                .build();
//        return options;
//
//    }
//
//
//
//    /**
//     * 初始化 ImageLoader
//     *
//     * @param context
//     */
//    public static ImageLoader initNoCacheImageLoader(Context context) {
//
//
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(false).delayBeforeLoading(100)
//                .considerExifParams(false)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
//                .cacheInMemory(false).cacheOnDisk(false)
////                .showImageForEmptyUri(R.drawable.default_pic)
////                .showImageOnFail(R.drawable.default_pic)
//                .build();
//
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                context.getApplicationContext())
//                .threadPoolSize(10)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
//                .imageDownloader(
//                        new BaseImageDownloader(context.getApplicationContext()))
//                .defaultDisplayImageOptions(options)
////                .writeDebugLogs()
//                .build();
//
//
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);
//
//        return imageLoader;
//
//    }
//
//
}
