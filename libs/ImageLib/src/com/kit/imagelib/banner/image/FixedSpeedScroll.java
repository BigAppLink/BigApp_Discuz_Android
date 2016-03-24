package com.kit.imagelib.banner.image;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 基本功能：设置图片自动滚动时间
 * 创建：王杰
 */
public class FixedSpeedScroll extends Scroller {
    Context context;
    private int mDuration = 500;

    public FixedSpeedScroll(Context context) {
        super(context);
        this.context = context;
    }

    public FixedSpeedScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
        this.context = context;
    }


    public void setDuration(ViewPager vp, int time) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            this.setmDuration(time);
            field.set(vp, this);
        } catch (Exception e) {

        }
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setmDuration(int time) {
        mDuration = time;
    }

    public int getmDuration() {
        return mDuration;
    }
} 