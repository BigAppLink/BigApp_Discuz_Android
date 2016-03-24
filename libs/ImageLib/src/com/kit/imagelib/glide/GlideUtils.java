package com.kit.imagelib.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;
import com.kit.config.AppConfig;
import com.kit.imagelib.R;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.StringUtils;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhao on 15/11/27.
 */
public class GlideUtils {

    public static void init(final Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
//        okHttpClient.setProtocols(Arrays.asList(Protocol.HTTP_1_1));

        GlideBuilder glideBuilder = new GlideBuilder(context)
                .setDiskCache(new DiskCache.Factory() {
                    @Override
                    public DiskCache build() {
                        // Careful: the external cache directory doesn't enforce permissions
                        File cacheLocation = new File(context.getExternalCacheDir(), AppConfig.CACHE_IMAGE_DIR);
                        cacheLocation.mkdirs();
                        return DiskLruCacheWrapper.get(cacheLocation, 100 * 1024 * 1024);
                    }
                });
        if (!Glide.isSetup()) {
            Glide.setup(glideBuilder);
        }

        Glide.get(context).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }


    public static void clearCache(final Context context) {
        Glide.with(context).onDestroy();

        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(context).clearDiskCache();
                Glide.get(context).clearMemory();

            }
        }.start();
    }


    public static void resetCache(Context context) {
        Glide.with(context).onDestroy();
        Glide.get(context).clearMemory();
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;


        if (ImageLibUitls.isGif(url)) {
            displayGif(context, imageView, url);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_picture)
                    .crossFade()
                    .centerCrop()
                    .into(imageView);
        }

    }


    public static void displayGif(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;
        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_picture_round)
                .crossFade()
                .fitCenter()
                .into(imageView);

    }


    public static void displayFile(Context context, ImageView imageView, File file) {
        if (StringUtils.isEmptyOrNullOrNullStr(file.getPath()))
            return;
        if (ImageLibUitls.isGif(file.getPath())) {
            displayGifFile(context, imageView, file);
        } else {
            Glide.with(context)
                    .load(file)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.no_picture)
                    .crossFade()
                    .centerCrop()
                    .into(imageView);
        }
    }


    public static void displayGifFile(Context context, ImageView imageView, File file) {
        if (StringUtils.isEmptyOrNullOrNullStr(file.getPath()))
            return;
        Glide.with(context)
                .load(file)
                .asGif()
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_picture_round)
                .crossFade()
                .fitCenter()
                .into(imageView);

    }

    public static void displayCropCirlce(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        Glide.with(context)
                .load(url)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.drawable.loading_round)
                .error(R.drawable.no_picture_round)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void displayInsideCircle(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        Glide.with(context)
                .load(url)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.drawable.loading_round)
                .error(R.drawable.no_picture_round)
                .crossFade()
                .fitCenter()
                .into(imageView);
    }


    public static void display(Context context, ImageView imageView, String url, Drawable drawablePlace, Drawable drawableError) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        Glide.with(context)
                .load(url)
                .placeholder(drawablePlace)
                .error(drawableError)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void displayNoHolder(Context context, ImageView imageView, String url, int drawableError) {


        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            Glide.with(context).load(drawableError)
                    .error(drawableError)
                    .crossFade()
                    .centerCrop()
                    .fitCenter()
                    .into(imageView);
        } else {

            Glide.with(context)
                    .load(url)
                    .error(drawableError)
                    .crossFade()
                    .centerCrop()
                    .fitCenter()
                    .into(imageView);
        }
    }
}
