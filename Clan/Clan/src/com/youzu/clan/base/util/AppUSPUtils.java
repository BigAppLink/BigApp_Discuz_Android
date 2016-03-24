package com.youzu.clan.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.kit.sharedpreferences.SharedPreferencesUtils;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.common.Action;

/**
 * Created by Zhao on 15/6/24.
 */
public class AppUSPUtils {

    public static void setUSPListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_USER_PREFERENCES, context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);
    }

    public static void unsetUSPListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_USER_PREFERENCES, context.MODE_PRIVATE);
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);

        ServiceUtils.stopClanService(context, Action.ACTION_CHECK_NEW_MESSAGE);
    }

    public static void saveUMainStyle(Context context, int style) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_MAIN_STYLE, style);
    }


    public static int getUMainStyle(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadIntSharedPreference(Key.KEY_U_MAIN_STYLE, 0);
    }


    public static void saveUThemeColor(Context context, int color) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_THEME_COLOR, color);
    }

    public static int getUThemeColor(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadIntSharedPreference(Key.KEY_U_THEME_COLOR, 0);
    }


    public static void saveUShowDigetstInTitle(Context context, boolean isUJpush) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_SHOW_DIGETST_IN_TITLE, isUJpush);
    }


    public static boolean isUShowDigetstInTitle(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_SHOW_DIGETST_IN_TITLE, false);
    }



    public static void saveUShowHotInTitle(Context context, boolean isUJpush) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_SHOW_HOT_IN_TITLE, isUJpush);
    }


    public static boolean isUShowHotInTitle(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_SHOW_HOT_IN_TITLE, false);
    }



    public static void saveUShowRecommendInTitle(Context context, boolean isUJpush) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_SHOW_RECOMMEND_IN_TITLE, isUJpush);
    }


    public static boolean isUShowRecommendInTitle(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_SHOW_RECOMMEND_IN_TITLE, false);
    }



    public static void saveUUsePush(Context context, boolean isUJpush) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_JPUSH, isUJpush);
    }


    public static boolean isUUsePush(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_JPUSH, true);
    }



    public static void saveUShowGroupAndRegisterDate(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_SHOW_GROUP_AND_REGISTER_DATE, b);
    }


    public static boolean isUShowGroupAndRegisterDate(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_SHOW_GROUP_AND_REGISTER_DATE, false);
    }



    public static void saveUDebug(Context context, boolean isUDebug) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_DEBUG, isUDebug);
    }


    public static boolean isUDebug(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_DEBUG, false);
    }

    public static void saveUShowBadge(Context context, boolean isUJpush) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_U_SHOW_BADGE, isUJpush);
    }


    public static boolean isUShowBadge(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_U_SHOW_BADGE, true);
    }




    public static void saveTailStr(Context context, String str) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("TAIL_STR", str);
    }

    public static String getTailStr(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadStringSharedPreference("TAIL_STR", "");
    }

    public static void saveTail(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("IS_TAIL", b);
    }

    public static boolean isTail(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("IS_TAIL", false);
    }

    public static void saveMadPraiseStr(Context context, String str) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("MAD_PRAISE_STR", str);
    }

    public static String getMadPraiseStr(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadStringSharedPreference("MAD_PRAISE_STR", "");
    }

    public static void saveMadPraise(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("IS_MAD_PRAISE", b);
    }

    public static boolean isMadPraise(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("IS_MAD_PRAISE", false);
    }


    public static void savePicSizeStr(Context context, String str) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("PIC_SIZE_STR", str);
    }

    public static String getPicSizeStr(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadStringSharedPreference("PIC_SIZE_STR", "");
    }

    public static void savePicSize(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("IS_PIC_SIZE", b);
    }

    public static boolean isPicSize(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("IS_PIC_SIZE", false);
    }

    public static void saveLookPicSizeStr(Context context, String str) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("look_pic_size_str", str);
    }

    public static String getLookPicSizeStr(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadStringSharedPreference("look_pic_size_str", "");
    }


    public static void saveLookPicSize(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("IS_LOOK_PIC_SIZE", b);
    }

    public static boolean isLookPicSize(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("IS_LOOK_PIC_SIZE", false);
    }

    public static void saveOpenImmersiveMode(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("open_immersive_mode", b);
    }

    public static boolean isOpenImmersiveMode(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("open_immersive_mode", false);
    }


    public static void saveShowNewFriendsInChatList(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("is_show_new_friends_in_chat_list", b);
    }


    public static boolean isShowNewFriendsInChatList(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("is_show_new_friends_in_chat_list", false);
    }

    public static void saveEnableScrollViewPager(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("is_enable_scroll_viewpager", b);
    }


    public static boolean isEnableScrollViewPager(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("is_enable_scroll_viewpager", false);
    }

    public static void savePureAndroid(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("is_pure_android", b);
    }


    public static boolean isPureAndroid(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        boolean isPureAndroid = sharedPreferencesUtils.loadBooleanSharedPreference("is_pure_android", false);
        return isPureAndroid;
    }


    public static void saveZhaoMode(Context context, boolean b) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences("ZHAO_MODE", b);
    }

    public static boolean isZhaoMode(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference("ZHAO_MODE", false);
    }


    public static void clear(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_USER_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.clear();
    }


}
