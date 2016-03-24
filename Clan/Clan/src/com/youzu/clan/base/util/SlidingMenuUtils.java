package com.youzu.clan.base.util;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.kit.utils.ArrayUtils;
import com.youzu.clan.R;
import com.youzu.clan.profile.MenuFragment;

/**
 * Created by Zhao on 15/8/19.
 */
public class SlidingMenuUtils {

    public static void initSilidingMenu(SlidingFragmentActivity slidingFragmentActivity, View[] ingnoreView, boolean isUseShadow, Drawable background, boolean isLikeQQ) {

        slidingFragmentActivity.setBehindContentView(R.layout.menu_frame);

        final SlidingMenu sm = slidingFragmentActivity.getSlidingMenu();
        sm.setSlidingEnabled(true);

        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        MenuFragment menuFragment = MenuFragment.getInstance();
//        去除头部区域滑动显示侧边栏
//        getSlidingMenu().addIgnoredView(mIndicator);

        slidingFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFragment).commit();
        sm.setMode(SlidingMenu.LEFT);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        sm.setTouchmodeMarginThreshold(250);
        sm.setBackgroundDrawable(background);

        if (!ArrayUtils.isNullOrContainEmpty(ingnoreView)) {
            for (View v : ingnoreView) {
                sm.addIgnoredView(v);
            }
        }

        if (isUseShadow) {
            sm.setShadowWidth(20);
            sm.setShadowDrawable(R.drawable.shape_sliding_left);
        }

        if (isLikeQQ) {
            sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                @Override
                public void transformCanvas(Canvas canvas, float percentOpen) {
                    float scale = (float) (percentOpen * 0.25 + 0.75);
                    canvas.scale(scale, scale, -canvas.getWidth() / 2, canvas.getHeight() / 2);
                }
            });

            sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                @Override
                public void transformCanvas(Canvas canvas, float percentOpen) {
                    float scale = (float) (1 - percentOpen * 0.25);
                    canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
                }
            });
//            menuFragment.getScrollView().setBackgroundColor(slidingFragmentActivity.getResources().getColor(R.color.transparent));
//            menuFragment.getScrollView().setBackgroundDrawable(slidingFragmentActivity.getResources().getDrawable(R.drawable.trans));
        }


    }
}
