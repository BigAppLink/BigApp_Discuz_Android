package com.kit.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kit.utils.bitmap.BitmapUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Zhao on 14-9-30.
 */
public class ImmersiveModeUtils {

    /**
     * 仅对使用了Actionbar的起作用
     * xml中需使用父控件如LinearLayout框住，并设置为 android:fitsSystemWindows="true"
     *
     * 对getActibar设置了hide()的，设置为 android:fitsSystemWindows="false" 并需要手动paddingTop
     *
     * @param baseActivity
     * @param color         状态栏的颜色
     * @param paddingTop    是否padding上部分(高度为状态栏高度)
     * @param paddingBottom 是否padding下部分
     */
    public static void immersiveAboveAPI19(AppCompatActivity baseActivity, int color, boolean paddingTop, boolean paddingBottom) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            Window window = baseActivity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
            ViewGroup view = (ViewGroup) baseActivity.getWindow().getDecorView();
            view.setBackgroundColor(color);

            if (paddingTop) {
                view.setPadding(0, (paddingTop ? DeviceUtils.getStatusBarHeight(baseActivity) : 0), 0,
                        ((baseActivity.getSupportActionBar() != null && paddingBottom)
                                ? DeviceUtils.getActionBarHeight(baseActivity) : 0));

                ZogUtils.printError(ImmersiveModeUtils.class, "immersive no actionbar");

            }
        }
    }


//    private void initSystemBar(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(color);
//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//        }
//    }

    /**
     * 三大金刚透明
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    public static void setTranslucentNavigation(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 状态栏透明
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * @param activity
     * @param container          容器
     * @param statusBarColor     顶部沉浸颜色
     * @param navigationBarColor 下面三大金刚的颜色
     * @param isTopMargin        是否margin头部
     * @param isBottomMargin     是否margin底部
     */
    public static void immersiveModeSystemBar(Activity activity, ViewGroup container,
                                              Integer statusBarColor, Integer navigationBarColor,
                                              boolean isTopMargin, boolean isBottomMargin) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
            setTranslucentNavigation(activity, true);

            container.setBackgroundColor(statusBarColor);

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

            //设置顶部颜色
            if (statusBarColor != null) {
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(statusBarColor);

                int actionBarHeight = DeviceUtils.getActionBarHeight(activity);
                Bitmap bitmap = BitmapUtils.getSingleColorBitmap(30, actionBarHeight, statusBarColor);

//                activity.getActionBar().setBackgroundDrawable(activity.getResources().getDrawable(R.color.transparent));
                if (activity.getActionBar() != null) {
                    activity.getActionBar().setBackgroundDrawable(new BitmapDrawable(bitmap));
                    activity.getActionBar().setStackedBackgroundDrawable(new BitmapDrawable(bitmap));
                    activity.getActionBar().setSplitBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            }

            /**
             * 设置底部三大金刚背景色
             */
            if (navigationBarColor != null) {
                //LogUtils.printLog(ImmersiveModeUtils.class, "set navigationBarColor");
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarTintColor(navigationBarColor);

//                int actionBarHeight = DeviceUtils.getActionBarHeight(activity);
//                Bitmap bitmap = BitmapUtils.getSingleColorBitmap(30, actionBarHeight, navigationBarColor);
//                activity.getActionBar().setSplitBackgroundDrawable(new BitmapDrawable(bitmap));
//                tintManager.setNavigationBarTintDrawable(new BitmapDrawable(bitmap));
            }


            //LogUtils.printLog(ImmersiveModeUtils.class, "isTopMargin:" + isTopMargin
//                    + " isBottomMargin:" + isBottomMargin +
//                    " config.getNavigationBarHeight():" + config.getNavigationBarHeight() +
//                    " navigationBarColor:" + navigationBarColor);

            if (isTopMargin) {
                if (isBottomMargin) {
                    //LogUtils.printLog(ImmersiveModeUtils.class, "isBottomMargin:" + isBottomMargin);
                    //是否下部margin

                    int marginBottomHeight = DensityUtils.dip2px(activity, DeviceUtils.getStatusBarHeight(activity));

                    if (container instanceof LinearLayout) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, marginBottomHeight);
                        container.setLayoutParams(lp);
                    } else if (container instanceof FrameLayout) {
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, marginBottomHeight);
                        container.setLayoutParams(lp);
                    } else if (container instanceof RelativeLayout) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, marginBottomHeight);
                        container.setLayoutParams(lp);
                    } else {
                        //LogUtils.printLog(ImmersiveModeUtils.class, "Parent Layout LayoutParams can not get");
                    }
                } else {
                    if (container.getLayoutParams().getClass().equals(LinearLayout.LayoutParams.class)) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
                        container.setLayoutParams(lp);
                    } else if (container.getLayoutParams().getClass().equals(FrameLayout.LayoutParams.class)) {
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
                        container.setLayoutParams(lp);
                    } else if (container.getLayoutParams().getClass().equals(RelativeLayout.LayoutParams.class)) {
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
                        container.setLayoutParams(lp);
                    } else {
                        //LogUtils.printLog(ImmersiveModeUtils.class, "Parent Layout LayoutParams can not get");
                    }
                }
            }

        }
    }

}
