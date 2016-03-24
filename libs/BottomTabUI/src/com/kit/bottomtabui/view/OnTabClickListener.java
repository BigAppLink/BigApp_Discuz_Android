package com.kit.bottomtabui.view;

import android.support.v4.view.ViewPager;
import android.view.View;

public class OnTabClickListener implements View.OnClickListener {
    public MainBottomTabLayout mainBottomTabLayout;
    public ViewPager mViewPager;
    public OnTabItemSelectedClickListener onTabItemSelectedClickListener;
    public OnItemClickListener onItemClickListener;


    public OnTabClickListener(MainBottomTabLayout mainBottomTabLayout, ViewPager mViewPager) {
        this.mainBottomTabLayout = mainBottomTabLayout;
        this.mViewPager = mViewPager;
        this.onTabItemSelectedClickListener = mainBottomTabLayout.getOnTabItemSelectedClickListener();

    }


    public OnTabItemSelectedClickListener getOnTabItemSelectedClickListener() {
        return onTabItemSelectedClickListener;
    }

    public void setOnTabItemSelectedClickListener(OnTabItemSelectedClickListener onTabItemSelectedClickListener) {
        this.onTabItemSelectedClickListener = onTabItemSelectedClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {

//        Log.e("APP", "OnTabClickListener v:" + v);

        for (int i = 0; i < mainBottomTabLayout.getChildCount(); i++) {
//            Log.e("APP", "mainBottomTabLayout.getChildCount():" + mainBottomTabLayout.getChildCount() + " i:" + i);
            if (v == mainBottomTabLayout.getChildAt(i)) {
                boolean isGoOn = onItemClickListener.onItemClick(v, i);

                int realInViewPager = mainBottomTabLayout.getRealPositionInViewPager(i);
                if (mViewPager.getCurrentItem() == realInViewPager && onTabItemSelectedClickListener != null) {
                    //当viewpager在当前位置，并且又被点击的时候调用
                    onTabItemSelectedClickListener.onItemClick(v, i);
                    return;
                }
                if (isGoOn)
                    mViewPager.setCurrentItem(mainBottomTabLayout.getRealPositionInViewPager(i), false);

                return;
            }
        }
//        for (int i = 0; i < mainBottomTabLayout.getChildCount(); i++) {
//            Log.e("APP", "mainBottomTabLayout.getChildCount():" + mainBottomTabLayout.getChildCount() + " i:" + i);
//
//            if (v == mainBottomTabLayout.getChildAt(i)) {
//                if (mViewPager.getCurrentItem() == i && onTabSelectedClickListener != null) {
//                    //当viewpager在当前位置，并且又被点击的时候调用
//                    onTabSelectedClickListener.onClick(v);
//                    return;
//                }
//                mViewPager.setCurrentItem(i, false);
//
//                return;
//            }
//        }
    }


    /**
     * 重写该方法来定位到ViewPager的位置
     *
     * @param position
     * @return
     */
    public int getPositionInViewPager(int position) {
        return position;
    }


    public interface OnItemClickListener {

        //boolean 是否继续执行,如果返回false证明是button型的，viewpager不滚动
        public boolean onItemClick(View v, int position);
    }

}
