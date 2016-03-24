package com.youzu.clan.base.util.theme;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;

import com.kit.utils.DeviceUtils;
import com.kit.utils.ImmersiveModeUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.base.enums.ForumNavStyle;
import com.youzu.clan.base.enums.MainActivityStyle;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.friends.SearchFriendsActivity;
import com.youzu.clan.main.bottomtab.BottomTabMainActivity;
import com.youzu.clan.main.bottomtab.MenuJumpActivity;
import com.youzu.clan.main.sliding.SlidingStyleMainActivity;
import com.youzu.clan.profile.homepage.HomePageActivity;
import com.youzu.clan.search.SearchActivity;

/**
 * Created by Zhao on 15/10/16.
 */
public class ThemeUtils {
    private static int themeColor;


    public static Drawable getActionBarDrawable(Context context) {
        GradientDrawable myGrad = null;
        Drawable actionbarDrawable = context.getResources().getDrawable(R.drawable.bg_actionbar);
        myGrad = (GradientDrawable) actionbarDrawable;
        if (myGrad != null) {
            myGrad.setColor(getThemeColor(context));
            //            myGrad.setColor(ColorUtils.toAlpha(ThemeUtils.getThemeColor(this), 100));
        }
        return myGrad;
    }

    public static void setThemeColor(Context context, int color) {
        themeColor = color;
        AppUSPUtils.saveUThemeColor(context, color);
    }

    public static int getThemeColor(Context context) {
        themeColor = (AppUSPUtils.getUThemeColor(context) == 0
                ? context.getResources().getColor(R.color.bg_title) : AppUSPUtils.getUThemeColor(context));
        return themeColor;
    }


    public static void initTheme(AppCompatActivity appCompatActivity) {
        if (AppUSPUtils.isPureAndroid(appCompatActivity)) {
            appCompatActivity.setTheme(R.style.PureAndroidActionBarTheme);
        }

        initResource(appCompatActivity);

        boolean isShowNavigationBar = DeviceUtils.isShowNavigationBar(appCompatActivity);

        ZogUtils.printError(ThemeUtils.class, "getNavigationBarHeight:" + DeviceUtils.getNavigationBarHeight(appCompatActivity) +
                " isShowingNavigationBar:" + isShowNavigationBar);


        /**
         * 如果有actionbar 设置actionbar相关
         */
        if (appCompatActivity.getSupportActionBar() != null
                && ThemeUtils.getActionBarDrawable(appCompatActivity) != null) {


            if ((appCompatActivity instanceof BottomTabMainActivity)
                    || (appCompatActivity instanceof SearchActivity)
                    || (appCompatActivity instanceof SearchFriendsActivity)
                    || (appCompatActivity instanceof HomePageActivity)
                    || (appCompatActivity instanceof SlidingStyleMainActivity)
                    || (appCompatActivity instanceof MenuJumpActivity)
                    ) {
                appCompatActivity.getSupportActionBar().hide();
            }

            /**
             * 设置Actionbar背景色
             */
            if (appCompatActivity.getSupportActionBar() != null
                    && ThemeUtils.getActionBarDrawable(appCompatActivity) != null) {
                appCompatActivity.getSupportActionBar().setBackgroundDrawable(ThemeUtils.getActionBarDrawable(appCompatActivity));
            }


        }


        /**
         * 沉浸状态栏
         */
        if (AppUSPUtils.isOpenImmersiveMode(appCompatActivity)) {


            /**
             * 这几个没有使用actionbar 需要特殊处理
             */
            if ((appCompatActivity instanceof BottomTabMainActivity)
                    || (appCompatActivity instanceof SearchActivity)
                    || (appCompatActivity instanceof SearchFriendsActivity)
                    ) {
                if (isShowNavigationBar) {
                    //如果有虚拟按键
                    ImmersiveModeUtils.immersiveAboveAPI19(appCompatActivity, ThemeUtils.getThemeColor(appCompatActivity), true, true);
                } else
                    ImmersiveModeUtils.immersiveAboveAPI19(appCompatActivity, ThemeUtils.getThemeColor(appCompatActivity), true, false);

            } else {
                if (isShowNavigationBar) {
                    //如果有虚拟按键
                    ImmersiveModeUtils.immersiveAboveAPI19(appCompatActivity, ThemeUtils.getThemeColor(appCompatActivity), false, true);
                } else
                    ImmersiveModeUtils.immersiveAboveAPI19(appCompatActivity, ThemeUtils.getThemeColor(appCompatActivity), false, false);

            }

        }
    }


    /**
     * 初始化一些资源文件
     *
     * @param context
     */
    public static void initResource(Context context) {
        GradientDrawable myGrad = (GradientDrawable) context.getResources().getDrawable(R.drawable.bg_menu);
        if (myGrad != null)
            myGrad.setColor(ThemeUtils.getThemeColor(context));
    }


    /**
     * 初始化一些资源文件
     *
     * @param context
     */
    public static void printAppStyle(Context context) {
        ZogUtils.printError(ClanApplication.class,
                "\n███████████████████████████████████████████████Theme START███████████████████████████████████████████████"
                        + "\n███████████████████████████████████████████████AppStyle:" + AppSPUtils.getConfig(context).getAppStyle() + " -> " + getAppStyleReadable(AppSPUtils.getConfig(context).getAppStyle())
                        + "\n███████████████████████████████████████████████ForumNavStyle:" + AppSPUtils.getContentConfig(context).getDisplayStyle() + " -> " + getFroumNavStyleReadable(AppSPUtils.getContentConfig(context).getDisplayStyle())
                        + "\n███████████████████████████████████████████████isUseSignIn:" + AppSPUtils.getContentConfig(context).getCheckinEnabled() + " -> " + ClanUtils.isUseSignIn(context)
                        + "\n███████████████████████████████████████████████Theme   END███████████████████████████████████████████████"

        );
    }


    /**
     * 获取可读性较强的APP风格
     *
     * @param appStyle
     * @return
     */
    public static String getAppStyleReadable(int appStyle) {

        switch (appStyle) {
            case MainActivityStyle.DEFAULT:
            case MainActivityStyle.TAB_BOTTOM:
//                return WeChatStyleMainActivity.class;
                return "MainActivityStyle.TAB_BOTTOM";
            case MainActivityStyle.SLIDING:
                return "MainActivityStyle.SLIDING";
            case MainActivityStyle.QQ:
                return "MainActivityStyle.QQ";
            case MainActivityStyle.QZONE:
                return "MainActivityStyle.QZONE";
            case MainActivityStyle.ANDROID:
                return "MainActivityStyle.ANDROID";
            default:
                return "MainActivityStyle.TAB_BOTTOM";
        }

    }


    /**
     * 获取可读性较强的APP风格
     *
     * @param forumNavStyle
     * @return
     */
    public static String getFroumNavStyleReadable(String forumNavStyle) {

        switch (forumNavStyle) {
            case ForumNavStyle.NORMAL:
                return "ForumNavStyle.NORMAL";
            case ForumNavStyle.TOP:
                return "ForumNavStyle.TOP";
            case ForumNavStyle.SLIDE:
                return "ForumNavStyle.SLIDE";
            default:
                return "MainActivityStyle.NORMAL";
        }

    }


}
