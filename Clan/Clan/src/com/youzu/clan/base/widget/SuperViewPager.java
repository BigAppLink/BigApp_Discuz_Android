package com.youzu.clan.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.kit.widget.listview.HorizontalListView;

public class SuperViewPager extends ViewPager {
    private boolean scrollable = true;

    public SuperViewPager(Context context) {
        super(context);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof HorizontalScrollView) {
            return true;
        }

        if (v instanceof HorizontalListView) {
            return true;
        }

        return super.canScroll(v, checkV, dx, x, y);
    }


//    @Override
//    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
//    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return false;
        } else
            return super.onInterceptTouchEvent(ev);
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (!scrollable) {
//            return false;
//        } else
//            return super.onTouchEvent(ev);
//    }


    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

}
