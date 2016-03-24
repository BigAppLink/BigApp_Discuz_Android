package com.kit.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimUtils {


    /**
     * 伴随动画显示控件
     *
     * @param context
     * @param view
     * @param resAnimId
     */
    public static void show(Context context, View view, int resAnimId) {
        if (view.getVisibility() != View.VISIBLE) {
            view.startAnimation(loadAnimation(context, resAnimId));
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 伴随动画隐藏控件
     *
     * @param context
     * @param view
     * @param resAnimId
     */
    public static void hidden(Context context, View view, int resAnimId) {
        if (view.getVisibility() == View.VISIBLE) {
            view.startAnimation(loadAnimation(context, resAnimId));
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 开始动画
     *
     * @param context
     * @param resId
     * @param resAnimId
     */
    public static void startAnim(Context context, int resId, int resAnimId) {
//		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//		findViewById(R.id.xxx).startAnimation(shake);

        Animation shake = AnimationUtils.loadAnimation(context, resAnimId);
        ((Activity) context).findViewById(resId).startAnimation(shake);
    }


    /**
     * 加载动画
     *
     * @param context
     * @param id
     * @return
     */
    public static Animation loadAnimation(Context context, int id) {

        Animation myAnimation = AnimationUtils.loadAnimation(context, id);
        return myAnimation;
    }
}
