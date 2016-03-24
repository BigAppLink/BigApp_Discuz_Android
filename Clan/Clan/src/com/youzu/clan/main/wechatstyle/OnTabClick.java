package com.youzu.clan.main.wechatstyle;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.kit.bottomtabui.view.MainBottomTabLayout;
import com.kit.bottomtabui.view.OnTabClickListener;
import com.youzu.clan.base.enums.BottomButtonType;
import com.youzu.clan.base.json.homepageconfig.ButtonConfig;
import com.youzu.clan.base.util.ClanUtils;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/7/10.
 */
public class OnTabClick extends OnTabClickListener {

    private ArrayList<ButtonConfig> buttonConfig;

    public OnTabClick(MainBottomTabLayout mbtl, ViewPager viewPager, ArrayList<ButtonConfig> bc) {
        super(mbtl, viewPager);

        this.buttonConfig = bc;


        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public boolean onItemClick(View v, int position) {


                if (BottomButtonType.MESSAGE.equals(buttonConfig.get(position).getButtonType()) && ClanUtils.isToLogin((Activity) mainBottomTabLayout.getContext(), null, Activity.RESULT_OK, false)) {
                    return false;
                }

                if (BottomButtonType.THREAD_PUBLISH.equals(buttonConfig.get(position).getButtonType()) && mainBottomTabLayout.getTabItems().get(position).isJustButton()) {
                    mainBottomTabLayout.getTabItems().get(position).getJustButtonClickListener().onClick(v);
                    return false;
                }


                return true;
            }
        };

        setOnItemClickListener(onItemClickListener);

    }


    @Override
    public int getPositionInViewPager(int position) {
        int realPositionInViewPager = mainBottomTabLayout.getRealPositionInViewPager(position);


        Log.e("APP", "realPositionInViewPager:" + realPositionInViewPager);
        Log.e("APP", "click i:" + position + " mViewPager.getCurrentItem():" + mViewPager.getCurrentItem());
        return realPositionInViewPager;
    }

//    @Override
//    public void onClick(View v) {
//
//        ZogUtils.printError(OnTabClick.class, "mainBottomTabLayout.getCount():" + mainBottomTabLayout.getCount() + " mainBottomTabLayout.getChildCount():" + mainBottomTabLayout.getChildCount());
//
//        //TODO
//        for (int i = 0; i < mainBottomTabLayout.getCount(); i++) {
////            Log.e("APP", "mainBottomTabLayout.getCount():" + mainBottomTabLayout.getCount() + " i:" + i);
//
//            int realPositionInViewPager = mainBottomTabLayout.getRealPositionInViewPager(i);
//
//            Log.e("APP", "v:" + v + " mainBottomTabLayout.getChildAt(i):" + mainBottomTabLayout.getChildAt(i));
//
//
//            if (v == mainBottomTabLayout.getChildAt(i)) {
//
//
//                if (BottomButtonType.MESSAGE.equals(buttonConfig.get(i).getButtonType()) && ClanUtils.isToLogin((Activity) mainBottomTabLayout.getContext(), null, Activity.RESULT_OK, false)) {
//                    return;
//                }
//
//                if (BottomButtonType.THREAD_PUBLISH.equals(buttonConfig.get(i).getButtonType()) && mainBottomTabLayout.getTabItems().get(i).isJustButton()) {
//                    mainBottomTabLayout.getTabItems().get(i).getJustButtonClickListener().onClick(v);
//                    return;
//                }
//
//                Log.e("APP", "realPositionInViewPager:" + realPositionInViewPager);
//                Log.e("APP", "click i:" + i + " mViewPager.getCurrentItem():" + mViewPager.getCurrentItem());
//
//                if (mViewPager.getCurrentItem() == realPositionInViewPager && onTabSelectedClickListener != null) {
//                    //当viewpager在当前位置，并且又被点击的时候调用
//                    onTabSelectedClickListener.onClick(v);
//                    return;
//                }
//
//
//                mViewPager.setCurrentItem(realPositionInViewPager, false);
//                Log.e("APP", "mViewPager.setCurrentItem:" + realPositionInViewPager + " mViewPager.getChildCount():" + mViewPager.getChildCount());
//
//                return;
//            }
//        }
//    }
}
