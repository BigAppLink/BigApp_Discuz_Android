package com.youzu.clan.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kit.sharedpreferences.SharedPreferencesUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.IDClan;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.enums.ForumNavStyle;
import com.youzu.clan.base.enums.IndexPagePicShow;
import com.youzu.clan.base.json.config.AdUrl;
import com.youzu.clan.base.json.config.ClanConfig;
import com.youzu.clan.base.json.config.LoginInfo;
import com.youzu.clan.base.json.config.content.ContentConfig;
import com.youzu.clan.base.json.config.content.ContentConfigJson;
import com.youzu.clan.base.json.config.content.PlatformLogin;
import com.youzu.clan.base.json.config.content.SearchSettings;
import com.youzu.clan.base.json.config.content.ThreadConfigItem;
import com.youzu.clan.base.json.config.content.smiley.SmileyInfo;
import com.youzu.clan.base.json.config.content.smiley.ZipInfo;
import com.youzu.clan.base.json.homepageconfig.HomePageJson;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/6/24.
 */
public class AppSPUtils {


    public static void setSPListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_PREFERENCES, context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);
    }

    public static void unsetSPListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_PREFERENCES, context.MODE_PRIVATE);
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);

        ServiceUtils.stopClanService(context, Action.ACTION_CHECK_NEW_MESSAGE);
    }


    public static void saveShowGoToTop(Context context, int show_go_to_top) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_SHOW_GO_TO_TOP, show_go_to_top);
    }

    public static int getShowGoToTop(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadIntSharedPreference(Key.KEY_SHOW_GO_TO_TOP, IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_RIGHT);
//        return  IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_HIDDEN;

    }




    public static void saveNewMessage(Context context, int count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(Key.KEY_NEW_MESSAGE, count)
                .commit();
    }

    public static int getNewMessage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Key.KEY_NEW_MESSAGE, 0);
    }



    public static void saveIndexPagePicMode(Context context, String count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Key.KEY_INDEX_PAGE_PIC_MODE, count)
                .commit();
    }

    public static String getIndexPagePicMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key.KEY_INDEX_PAGE_PIC_MODE, IndexPagePicShow.PIC_SHOW_IMAGE);
    }


    public static void saveAvatarUrl(Context context, String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Key.KEY_AVATAR, url).commit();
        sharedPreferences
                .edit()
                .putString(
                        Key.KEY_SAVE_AVATAR_DATE,
                        DateUtils
                                .getCurrDateFormat(DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC))
                .commit();
    }

    public static String getAvatartUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key.KEY_AVATAR, "");
    }


    public static long getAvatarSaveDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String dateStr = sharedPreferences.getString(
                Key.KEY_SAVE_AVATAR_DATE, "");

        ZogUtils.printLog(ClanUtils.class, "getAvatarSaveDate:" + dateStr);
        return DateUtils.getDate2Long(dateStr,
                DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }


    public static void saveAvatarReplacedDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(
                        Key.KEY_REPLACE_AVATAR_DATE,
                        DateUtils
                                .getCurrDateFormat(DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC))
                .commit();
    }


    public static boolean isWechatScroll(Context context) {
//        if (ForumNavStyle.TOP.equals(AppSPUtils.getContentConfig(context).getDisplayStyle())) {
//            return false;
//        }
        return AppUSPUtils.isEnableScrollViewPager(context);
    }


    public static void setLoginInfo(Context context, boolean isLogined, String uid, String username) {
        if (!isLogined) {
            uid = "0";
            CookieUtils.cleanCookie(context);
            AppSPUtils.saveNewMessage(context, 0);
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(Key.KEY_UID, uid)
                .putString(Key.KEY_USERNAME, username)
                .putBoolean(Key.KEY_LOGINED, isLogined)
                .commit();

    }

    public static boolean isLogined(Context context) {
        if ("0".equals(AppSPUtils.getUid(context))) {
            return  false;
        }
        return true;
    }



    public static String getUid(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Key.KEY_UID, "");
    }


    public static void saveExitTime(Context context, long time) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_EXIT_TIME, time);
    }

    public static long getExitTime(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadLongSharedPreference(Key.KEY_EXIT_TIME, DateUtils.getCurrDateLong());
    }


    public static void saveUsername(Context context, String username) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_USERNAME, username);
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Key.KEY_USERNAME, "");
    }


    public static void saveProfile(Context context, String profile) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_PROFILE, profile);
    }

    public static String getProfile(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Key.KEY_PROFILE, "");
    }



    public static long getAvatarReplaceDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String dateStr = sharedPreferences.getString(
                Key.KEY_REPLACE_AVATAR_DATE, "");


        ZogUtils.printLog(ClanUtils.class, "getAvatarReplaceDate:" + dateStr);
        return DateUtils.getDate2Long(dateStr,
                DateUtils.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }


    //    public static void setContentConfig(ContentConfig clanConfig) {
//        if (clanConfig == null)
//            return;
//        if (!StringUtils.isEmptyOrNullOrNullStr(clanConfig.getDisplayStyle())) {
//            ZogUtils.printError(AppSPUtils.class, "clanConfig.getDisplayStyle():" + clanConfig.getDisplayStyle());
//            AppConfig.FORUM_NAV_STYLE = ClanUtils.parseInt(clanConfig.getDisplayStyle());
//        }
//    }

    public static void saveHomePageConfigJson(Context context, String clanHomePagerConfigJson) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_HOME_PAGE_CONFIG, clanHomePagerConfigJson);
    }

    public static HomePageJson getHomePageConfigJson(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String clanHomePagerJson = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_HOME_PAGE_CONFIG);
        HomePageJson contentConfigJson = null;
        if (!StringUtils.isEmptyOrNullOrNullStr(clanHomePagerJson)) {
            contentConfigJson = ClanUtils.parseObject(clanHomePagerJson, HomePageJson.class);
        }
        return contentConfigJson;
    }

    public static void saveContentConfigJson(Context context, String clanConfigJson) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG, clanConfigJson);
    }

    private static ContentConfigJson getContentConfigJson(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String clanConfigJson = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG);
        ContentConfigJson contentConfigJson = null;
        if (!StringUtils.isEmptyOrNullOrNullStr(clanConfigJson)) {
            contentConfigJson = ClanUtils.parseObject(clanConfigJson, ContentConfigJson.class);
        }

        return contentConfigJson;
    }

    public static void saveContentConfig(Context context, ContentConfig clanConfig) {

        if (clanConfig == null)
            return;
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_FORUM_DISPLAY_STYLE, clanConfig.getDisplayStyle());

        if (clanConfig.getPlatformLogin() == null) {
            clanConfig.setPlatformLogin(new PlatformLogin());
        }

        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_QQ_LOGIN, clanConfig.getPlatformLogin().getQQLogin());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_QQ_LOGIN_END, clanConfig.getPlatformLogin().getQQloginEnd());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_WECHAT_LOGIN, clanConfig.getPlatformLogin().getWechatLogin());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_WEIBO_LOGIN, clanConfig.getPlatformLogin().getWeiboLogin());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_CHECKINENABLED, clanConfig.getCheckinEnabled());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_CONFIG_IYZVERSOIN, clanConfig.getIyzversion());
        if (clanConfig.getSmileyInfo() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_LAST_MD5, getContentConfig(context).getSmileyInfo().getMD5());
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_MD5, clanConfig.getSmileyInfo().getMD5());
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_URL, clanConfig.getSmileyInfo().getZipUrl());
        } else {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_LAST_MD5, "");
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_MD5, "");
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_URL, "");
        }
        if (clanConfig.getSearchSetting() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SEARCH_SETTING, JsonUtils.toJSONString(clanConfig.getSearchSetting()));
        }
        if (clanConfig.getThreadConfig() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_THREAD_SETTING, JsonUtils.toJSONString(clanConfig.getThreadConfig()));
        }
        if (clanConfig.getPortalconfig() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_PORTAIL_SETTING, JsonUtils.toJSONString(clanConfig.getPortalconfig()));
        }
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_APP_DESC, clanConfig.getAppDesc());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_PUSH_ENABLED, clanConfig.getPushEnabled());

    }


    public static ContentConfig getContentConfig(Context context) {
        ContentConfigJson contentConfigJson = getContentConfigJson(context);

        ContentConfig clanConfig = new ContentConfig();
        clanConfig.setDisplayStyle(ForumNavStyle.NORMAL);
        clanConfig.setSmileyInfo(new SmileyInfo());

//        ZogUtils.printError(AppSPUtils.class, "contentConfigJson.getConfig().getPushEnabled():" + contentConfigJson.getConfig().getPushEnabled());

        if (contentConfigJson != null) {
            clanConfig = contentConfigJson.getConfig();
        }

//        clanConfig.getSmileyInfo().setLastMD5(getSmileyLastMD5(context));

//        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
//
//
//        ContentConfig clanConfig = new ContentConfig();
//        clanConfig.setDisplayStyle(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_FORUM_DISPLAY_STYLE, ForumNavStyle.NORMAL));
//
//        clanConfig.setCheckinEnabled(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_CHECKINENABLED, "0"));
//        clanConfig.setIyzversion(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_IYZVERSOIN, ""));
//
//        String zipLastMD5 = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_LAST_MD5, "");
//        String zipMD5 = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_MD5, "");
//        String zipUrl = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_URL, "");
//
//        SmileyInfo smileyInfo = new SmileyInfo();
//        smileyInfo.setLastMD5(zipLastMD5);
//        smileyInfo.setMD5(zipMD5);
//        smileyInfo.setZipUrl(zipUrl);
//        clanConfig.setSmileyInfo(smileyInfo);
//
//        PlatformLogin platformLogin = new PlatformLogin();
//        platformLogin.setQQlogin(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_QQ_LOGIN));
//        platformLogin.setQQloginEnd(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_QQ_LOGIN_END));
//        platformLogin.setWechatLogin(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_WECHAT_LOGIN));
//        platformLogin.setWeiboLogin(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_CONFIG_WEIBO_LOGIN));
//        clanConfig.setPlatformLogin(platformLogin);
//
//
//        clanConfig.setSearchSetting(getSearchConfig(context));
//        clanConfig.setThreadConfig(getThreadConfig(context));
//        clanConfig.setPortalconfig(getPortailConfig(context));
//
//        clanConfig.setAppDesc(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_APP_DESC, ""));
//
//        clanConfig.setPushEnabled(sharedPreferencesUtils.loadIntSharedPreference(Key.CLAN_CONTENT_PUSH_ENABLED));

        return clanConfig;
    }

    public static void saveSmileyLastMD5(Context context, String lastMD5) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_LAST_MD5, lastMD5);
    }


    public static String getSmileyLastMD5(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String zipLastMD5 = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_SIMILY_INFO_ZIP_LAST_MD5, "");
        return zipLastMD5;
    }


    public static SearchSettings getSearchConfig(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String searchSetting = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_SEARCH_SETTING, "");
        if (!StringUtils.isEmptyOrNullOrNullStr(searchSetting)) {
            return ClanUtils.parseObject(searchSetting, SearchSettings.class);
        }
        return null;
    }

    private static ArrayList<ThreadConfigItem> getThreadConfig(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String threadSetting = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_THREAD_SETTING, "");

        if (!StringUtils.isEmptyOrNullOrNullStr(threadSetting)) {
            ZogUtils.printError(AppSPUtils.class, "threadSetting: " + threadSetting);
            return (ArrayList) ClanUtils.parseArray(threadSetting, ThreadConfigItem.class);
        }
        return null;
    }

    private static ArrayList<ThreadConfigItem> getPortailConfig(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String portailSetting = sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONTENT_PORTAIL_SETTING, "");

        if (!StringUtils.isEmptyOrNullOrNullStr(portailSetting)) {
            ZogUtils.printError(AppSPUtils.class, "portailSetting: " + portailSetting);
            return (ArrayList) ClanUtils.parseArray(portailSetting, ThreadConfigItem.class);
        }
        return null;
    }

    public static ArrayList<ZipInfo> getSimleyInfo(Context context) {
        ContentConfig contentConfig = getContentConfig(context);
        if (contentConfig != null && contentConfig.getSmileyInfo() != null) {
            return contentConfig.getSmileyInfo().getZipInfo();
        }
        return null;
    }


    public static void setConfig(ClanConfig clanConfig) {
        if (clanConfig == null)
            return;
        ZogUtils.printLog(ClanUtils.class, "adConfig got!!! adConfig.getApiUrl():" + clanConfig.getApiUrl());

        if (!StringUtils.isEmptyOrNullOrNullStr(clanConfig.getApiUrl())) {

            Url.DOMAIN = clanConfig.getApiUrl();
            Url.BASE = clanConfig.getApiUrlBase();

//            Account account = getLoginInfo(context);
//            if (account != null) {
//                AppConfig.LOGIN_MODE = account.getLoginMod();
//                AppConfig.LOGIN_URL = account.getLoginUrl();

//                AppConfig.REG_MODE = clanConfig.getAccount().getRegMod();
//                AppConfig.REG_URL = clanConfig.getAccount().getRegUrl();
//                AppConfig.REG_SWITCH = clanConfig.getAccount().getRegSwitch();
//            }

        }
    }

    public static void setConfig(Context context) {
        ClanConfig clanConfig = getConfig(context);
        setConfig(clanConfig);
    }


    /**
     * 得到登录的配置项
     * @param context
     * @return
     */
    public static LoginInfo getLoginInfo(Context context) {
        LoginInfo loginInfo = null;
        ClanConfig clanConfig = getConfig(context);
        if (clanConfig.getLoginInfo() != null) {
            loginInfo = clanConfig.getLoginInfo();
        }

        ContentConfig contentConfig = getContentConfig(context);
        if (contentConfig.getLoginInfo() != null) {
            loginInfo = contentConfig.getLoginInfo();
        }
        return loginInfo;
    }

    public static void saveConfig(Context context, ClanConfig clanConfig) {
        if (clanConfig == null)
            return;
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_DOMAIN, clanConfig.getApiUrl());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_BASE, clanConfig.getApiUrlBase());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_REAL_BASE, clanConfig.getApiUrlRealBase());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_APP_ID, clanConfig.getAppId());
        sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_APP_KEY, clanConfig.getAppKey());

        if (clanConfig.getAd() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_AD_SPLASH, clanConfig.getAd().getUrlSplashAd());
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_AD_LIST, clanConfig.getAd().getUrlListAd());
            sharedPreferencesUtils.saveSharedPreferences(Key.CLAN_CONFIG_AD_LOG, clanConfig.getAd().getUrlLogAd());
        }

        if (clanConfig.getLoginInfo() != null) {
            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_LOGIN_MODE, clanConfig.getLoginInfo().getLoginMod());
            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_LOGIN_URL, clanConfig.getLoginInfo().getLoginUrl());

            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_REG_MODE, clanConfig.getLoginInfo().getRegMod());
            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_REG_URL, clanConfig.getLoginInfo().getRegUrl());
            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_REG_SWITCH, clanConfig.getLoginInfo().getRegSwitch());
            sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_ALLOW_AVATAR_CHANGE, clanConfig.getLoginInfo().getAllowAvatarChange());
        }

        sharedPreferencesUtils.saveSharedPreferences(Key.APP_CONFIG_APP_STYLE, clanConfig.getAppStyle());


    }


    /**
     * 保存基本配置
     * @param context
     * @return
     */
    public static ClanConfig getConfig(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);


        ClanConfig clanConfig = new ClanConfig();
        clanConfig.setApiUrl(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_DOMAIN));
        clanConfig.setApiUrlBase(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_BASE));
        clanConfig.setApiUrlRealBase(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_REAL_BASE));

        clanConfig.setAppId(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_APP_ID));
        clanConfig.setAppKey(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_APP_KEY));

        AdUrl adUrl = new AdUrl();
        adUrl.setUrlSplashAd(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_AD_SPLASH));
        adUrl.setUrlListAd(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_AD_LIST));
        adUrl.setUrlLogAd(sharedPreferencesUtils.loadStringSharedPreference(Key.CLAN_CONFIG_AD_LOG));
        clanConfig.setAd(adUrl);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setLoginMod(sharedPreferencesUtils.loadIntSharedPreference(Key.APP_CONFIG_LOGIN_MODE));
        loginInfo.setLoginUrl(sharedPreferencesUtils.loadStringSharedPreference(Key.APP_CONFIG_LOGIN_URL));

        loginInfo.setRegMod(sharedPreferencesUtils.loadIntSharedPreference(Key.APP_CONFIG_REG_MODE));
        loginInfo.setRegUrl(sharedPreferencesUtils.loadStringSharedPreference(Key.APP_CONFIG_REG_URL));
        loginInfo.setRegSwitch(sharedPreferencesUtils.loadIntSharedPreference(Key.APP_CONFIG_REG_SWITCH));
        loginInfo.setAllowAvatarChange(sharedPreferencesUtils.loadStringSharedPreference(Key.APP_CONFIG_ALLOW_AVATAR_CHANGE));


        clanConfig.setLoginInfo(loginInfo);

        clanConfig.setAppStyle(sharedPreferencesUtils.loadIntSharedPreference(Key.APP_CONFIG_APP_STYLE));

        return clanConfig;
    }



    public static ArrayList<String> getSearchHistoryRecord(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        String history = sharedPreferences.getString(Key.KEY_SEARCH_HISTORY_RECORD, "[]");
        ArrayList<String> historys = (ArrayList<String>) JsonUtils.parseArray(history, String.class);
        if (historys == null) {
            historys = new ArrayList<String>();
        }
        return historys;
    }

    public static void setSearchHistoryRecord(Context context, ArrayList<String> historys) {
        if (historys == null) {
            historys = new ArrayList<String>();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(Key.KEY_SEARCH_HISTORY_RECORD, JsonUtils.toJSON(historys).toString());
        e.commit();
    }

    public static void savePlugInIsOk(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferencesUtils.saveSharedPreferences(Key.KEY_PLUG_IN, true);
    }

    public static boolean isPlugInIsOk(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, Key.FILE_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferencesUtils.loadBooleanSharedPreference(Key.KEY_PLUG_IN, false);
    }
}
