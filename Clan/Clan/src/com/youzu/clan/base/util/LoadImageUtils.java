package com.youzu.clan.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kit.imagelib.glide.GlideCircleTransform;
import com.kit.imagelib.glide.GlideUtils;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.widget.LoadingDialogFragment;

/**
 * Created by Zhao on 15/5/21.
 */
public class LoadImageUtils extends GlideUtils {



    public static void display(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;


        if (ImageLibUitls.isGif(url)) {
            displayGif(context, imageView, url);
        } else {
            String resize = AppUSPUtils.getPicSizeStr(context);
            url = ClanUtils.resizePicSize(context, url, resize);
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

        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);
        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_picture)
                .crossFade()
                .fitCenter()
                .into(imageView);

    }


    public static void displayCropCirlce(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

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

        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

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

        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

        Glide.with(context)
                .load(url)
                .placeholder(drawablePlace)
                .error(drawableError)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void displayNoHolder(Context context, ImageView imageView, String url, Drawable drawableError) {
        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            return;
        }

        Glide.with(context)
                .load(url)
                .error(drawableError)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void displayNoHolder(Context context, ImageView imageView, String url, int drawableError) {
        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

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


    public static void displayNoHolderNoError(Context context, ImageView imageView, String url) {
        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .centerCrop()
                    .fitCenter()
                    .into(imageView);
        }
    }



    public static void displayMineAvatar(Context context, ImageView imageView, String url) {
        String resize = AppUSPUtils.getPicSizeStr(context);
        url = ClanUtils.resizePicSize(context, url, resize);

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            return;
        }

        boolean isDoRefreshAvatar = false;
        if (AppSPUtils.getAvatarReplaceDate(context) > AppSPUtils.getAvatarSaveDate(context)) {
            isDoRefreshAvatar = true;
        }
        ZogUtils.printLog(LoadImageUtils.class, "isDoRefreshAvatar：" + isDoRefreshAvatar);

        url = dealAvatarUrl(url);


        if (isDoRefreshAvatar) {
            displayAvatarRefresh(context, imageView, url);
        } else {
            displayAvatar(context, imageView, url);
        }

    }

    public static void displayAvatar(Context context, ImageView imageView, String url) {

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            return;
        }

        url = dealAvatarUrl(url);


//        String resize = AppUSPUtils.getPicSizeStr(context);
//        url = ClanUtils.resizePicSize(context, url, resize);

        Glide.with(context)
                .load(url)
//                .placeholder(R.drawable.ic_protrait_solid)
                .error(R.drawable.ic_protrait_solid)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);

    }


    /**
     * 刷新我的头像
     *
     * @param context
     * @param imageView
     * @param url
     */
    private static void displayAvatarRefresh(final Context context, final ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        final String avatarUrl = dealAvatarUrl(url);

        ZogUtils.printLog(LoadImageUtils.class, "old avatar：" + AppSPUtils.getAvatartUrl(context));
        ZogUtils.printLog(LoadImageUtils.class, "new avatar：" + avatarUrl);

        resetCache(context);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

        displayAvatarNoCache(context, imageView, url);

        AppSPUtils.saveAvatarUrl(context, avatarUrl);
//            }
//        }, 5000);
    }


    public static void displayAvatarNoCache(Context context, ImageView imageView, String url) {
        if (StringUtils.isEmptyOrNullOrNullStr(url))
            return;

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_protrait_solid)
                .crossFade()
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }


    public static void displayAvatarShowLoading(final FragmentActivity context, final ImageView imageView, String url) {

        if (StringUtils.isEmptyOrNullOrNullStr(url)) {
            return;
        }
        url = dealAvatarUrl(url);

        Glide.with(context)
                .load(AppSPUtils.getAvatartUrl(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.ic_protrait_solid)
                .error(R.drawable.ic_protrait_solid)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        LoadingDialogFragment.getInstance(context).dismissAllowingStateLoss();
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        LoadingDialogFragment.getInstance(context).dismissAllowingStateLoss();
                    }
                });
    }


    private static String dealAvatarUrl(String url) {
        url = url.replace("size=small", "size=big");

//        String resize = AppUSPUtils.getPicSizeStr(context);
//        url = ClanUtils.resizePicSize(context, url, resize);

        return url;

    }


}
