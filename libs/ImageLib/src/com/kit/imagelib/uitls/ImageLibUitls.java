package com.kit.imagelib.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.kit.imagelib.R;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.imagelib.imagelooker.ImageBrowserActivity;
import com.kit.imagelib.imagelooker.ImagesLookerActivity;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @author wanggang
 */
public class ImageLibUitls {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void setHomeActionBar(AppCompatActivity activity) {
//

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

    }

    public static boolean isEmptyOrNullOrNullStr(String str) {
        return str == null || TextUtils.isEmpty(str) || str.equals("null");
    }


    public static boolean isGif(String filedir) {
        if (isEmptyOrNullOrNullStr(filedir)) {
            return false;
        } else {
            String sub = getSuffix(filedir);
            boolean isGifPic = ("gif".equals(sub) || "GIF".equals(sub));
            return isGifPic;
        }
    }


    /**
     * 获取文件后缀，不带“.”
     *
     * @return
     */
    public static String getSuffix(File file) {
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return suffix;
    }


    /**
     * 获取文件后缀，不带“.”
     *
     * @param filedir
     * @return
     */
    public static String getSuffix(String filedir) {
        if (isEmptyOrNullOrNullStr(filedir)) {
            return null;
        } else {
            String suffix = filedir.substring(filedir.lastIndexOf(".") + 1);
//            ZogUtils.printError(ImageLibUitls.class, "suffix:" + suffix);
            return suffix;
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 文件copy
     *
     * @param oldFilePath
     * @param newFilePath
     * @param cover       新目录存在，是否覆盖
     */
    public static String copy(String oldFilePath, String newFilePath, boolean cover) {


        if (!oldFilePath.equals(newFilePath)) {
            File oldfile = new File(oldFilePath);
            File newfile = new File(newFilePath);

            beforeSave(newFilePath);

            if (newfile.exists()) {//若在待转移目录下，已经存在待转移文件
                if (cover)//覆盖
                    oldfile.renameTo(newfile);
                else
                    Log.e("APP", "在新目录下已经存在：" + newFilePath);
            } else {
                oldfile.renameTo(newfile);
            }

            return newFilePath;
        }
        return null;
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }


    public static File saveBitmap(Bitmap bmp, File file) throws IOException {
        if (bmp == null) {
            return null;
        } else {
            System.out.println("file.getPath():" + file.getPath());
            if ((new File(file.getParent())).mkdirs()) {
                System.out.println("建立目录成功");
            } else {
                System.out.println("建立目录失败");
            }

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return file;
        }
    }

    /**
     * 跳转到浏览图片界面的公用方法
     *
     * @param context
     * @param imageBeans
     * @param index
     */
    public static void gotoBrowserImage(Activity context, List<ImageBean> imageBeans, int index) {

        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra("images", (Serializable) imageBeans);
        intent.putExtra("position", index);
        intent.putExtra("isdel", false);
        context.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_LOOK_PIC);
    }


    /**
     * 跳转到浏览图片界面的公用方法
     *
     * @param context
     * @param imageBeans
     * @param index
     */
    public static void gotoLookImage(Activity context, Class imageLookerActivityClass, List<ImageBean> imageBeans, int index) {

        Intent intent = new Intent();
        if (imageLookerActivityClass == null)
            intent.setClass(context, ImagesLookerActivity.class);
        else intent.setClass(context, imageLookerActivityClass);


        intent.putExtra("images", (Serializable) imageBeans);
        intent.putExtra("position", index);
        intent.putExtra("isdel", false);
        context.startActivityForResult(intent, ImageLibRequestResultCode.REQUEST_LOOK_PIC);
    }

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

    public static DisplayImageOptions getDisplayImageOptions(Drawable drawableEmpty, Drawable drawableFail) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false).delayBeforeLoading(100)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).handler(new Handler())
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(drawableEmpty)
                .showImageOnFail(drawableFail)
                .build();
        return options;

    }

    /**
     * 初始化 ImageLoader
     *
     * @param context
     */
    public static ImageLoader initImageLoader(Context context, File cacheDir, Drawable drawableEmpty, Drawable drawableFail) {

        DisplayImageOptions options = getDisplayImageOptions(drawableEmpty, drawableFail);


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context.getApplicationContext())
                .threadPoolSize(10)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
                .discCache(new UnlimitedDiskCache(cacheDir))
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

//
//    /**
//     * 初始化 ImageLoader
//     *
//     * @param context
//     */
//    public static ImageLoader initNoCacheImageLoader(Context context) {
//
//
//        File cacheDir = new File("/sdcard/temp");
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


    private static void beforeSave(String fileName) {
        String dir = fileName.substring(0, fileName.lastIndexOf("/"));
        File directory = new File(dir);

        if (!directory.exists()) {
            directory.mkdir();//没有目录先创建目录
        }
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
