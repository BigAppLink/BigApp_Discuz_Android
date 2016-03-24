package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

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
import com.youzu.clan.R;
import com.youzu.clan.app.config.AppConfig;

import java.io.File;

public class ImageUtils {




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
                .discCache(new UnlimitedDiskCache(getDefaultCacheDir()))
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



    public static File getDefaultCacheDir(){
        File cacheDir = new File(AppConfig.CACHE_IMAGE_DIR);
        return  cacheDir;
    }
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

    public static DisplayImageOptions getAvatarDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false).delayBeforeLoading(100)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.ic_protrait_solid)
                .showImageOnFail(R.drawable.ic_protrait_solid)
                .build();
        return options;

    }



    /**
     * 初始化 ImageLoader
     *
     * @param context
     */
    public static ImageLoader initNoCacheImageLoader(Context context) {



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false).delayBeforeLoading(100)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
                .cacheInMemory(false).cacheOnDisk(false)
//                .showImageForEmptyUri(R.drawable.default_pic)
//                .showImageOnFail(R.drawable.default_pic)
                .build();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context.getApplicationContext())
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
                .imageDownloader(
                        new BaseImageDownloader(context.getApplicationContext()))
                .defaultDisplayImageOptions(options)
//                .writeDebugLogs()
                .build();


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        return imageLoader;

    }


}
