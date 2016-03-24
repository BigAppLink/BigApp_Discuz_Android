package com.kit.imagelib.uitls;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kit.imagelib.R;

/**
 * Created by Zhao on 15/5/25.
 */
public class PicassoUtils {


    public static void display(Context context, ImageView imageView, String url, Drawable drawablePlace, Drawable drawableError) {
        if (url == null || TextUtils.isEmpty(url))
            return;

        Glide.with(context)
                .load(url)
//				.networkPolicy(NetworkPolicy.NO_CACHE)
//				.memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(drawablePlace)
                .error(drawableError)
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void display(Context context, ImageView imageView, String url) {
        if (url == null || TextUtils.isEmpty(url))
            return;

        Glide.with(context)
                .load(url)
//				.networkPolicy(NetworkPolicy.NO_CACHE)
//				.memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.no_picture)
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }

}
