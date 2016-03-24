package com.youzu.clan.base.util;

import android.content.Context;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.AppUtils;
import com.kit.utils.FileUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.ForumnavJson;
import com.youzu.clan.base.json.config.ClanConfig;
import com.youzu.clan.base.json.config.ConfigJson;
import com.youzu.clan.base.json.config.content.ContentConfigJson;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.main.base.forumnav.DBForumNavUtils;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Zhao on 15/7/14.
 */
public class InitUtils {

    public static void initShareSDK(Context context) {

        ShareSDK.initSDK(context, true);

        HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
        hashMap1.put("Id", 1);
        hashMap1.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, SinaWeibo.NAME) + "");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, hashMap1);

//        HashMap<String, Object> hashMap3 = new HashMap<String, Object>();
//        hashMap3.put("Id", 6);
//        hashMap3.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, QQ.NAME) + "");
//        ShareSDK.setPlatformDevInfo("QZone", hashMap3);

        HashMap<String, Object> hashMap4 = new HashMap<String, Object>();
        hashMap4.put("Id", 4);
        hashMap4.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, Wechat.NAME) + "");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, hashMap4);

        HashMap<String, Object> hashMap5 = new HashMap<String, Object>();
        hashMap5.put("Id", 5);
        hashMap5.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, Wechat.NAME) + "");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, hashMap5);

//        HashMap<String, Object> hashMap6 = new HashMap<String, Object>();
//        hashMap6.put("Id", 6);
//        hashMap6.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, Wechat.NAME) + "");
//        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, hashMap6);

//        HashMap<String, Object> hashMap7 = new HashMap<String, Object>();
//        hashMap7.put("Id", 7);
//        hashMap7.put("Enable", ClanUtils.isUseShareSDKPlatformName(context, QQ.NAME) + "");
//        ShareSDK.setPlatformDevInfo(QQ.NAME, hashMap7);


    }

    /**
     * 初始化基本环境
     *
     * @param context
     */
    public static void initBasicEnvironment(Context context) {
        //切换语言环境
        AppUtils.changeAppLanguage(context.getResources(), "zh-cn");

        //初始化缓存、图片、数据等文件路径
        {
            AppConfig.CACHE_DIR = "mnt/sdcard/.bigapp/" + AppUtils.getAppPackage(context);
            AppConfig.CACHE_DATA_DIR = AppConfig.CACHE_DIR + "/data/";
            AppConfig.CACHE_IMAGE_DIR = AppConfig.CACHE_DIR + "/image/";

            FileUtils.mkDir(AppConfig.CACHE_DATA_DIR);
            FileUtils.mkDir(AppConfig.CACHE_IMAGE_DIR);
        }
    }

    /**
     * 切换环境
     *
     * @param context
     */
    public static void initEnvironment(Context context) {
        initBasicEnvironment(context);

        ClanUtils.setDebug(context, false, false);
    }

    public static void initConfigFromSharedPerference(Context context) {
        AppSPUtils.setConfig(context);
    }

    /**
     * 提前加载版块数据，用于首页发帖
     *
     * @param context
     * @param doSomeThing
     */

    public static void preLoadForumData(final Context context, final DoSomeThing doSomeThing) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "forumnav");
        BaseHttp.get(Url.DOMAIN, params, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                ZogUtils.printError(ClanUtils.class, "preLoadForumData  json:" + jsonStr);
                ForumnavJson forumnavJson = ClanUtils.parseObject(jsonStr, ForumnavJson.class);
                if (forumnavJson != null && forumnavJson.getVariables() != null) {
                    DBForumNavUtils.deleteAllForum(context);
                    DBForumNavUtils.saveOrUpdateAllForum(context, forumnavJson.getVariables().getForums(), doSomeThing);
                } else {
                    doSomeThing.execute();
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                ZogUtils.printError(ClanUtils.class, "preLoadForumData failed");
                doSomeThing.execute();
            }
        });
    }

    /**
     * 首页的配置
     *
     * @param context
     * @param doSomeThing
     */
    public static void initHomePageConfig(final Context context, final DoSomeThing doSomeThing) {

        ClanHttpParams params = new ClanHttpParams();

        params.addQueryStringParameter("module", "indexcfg");
        params.addQueryStringParameter("iyzmobile", "1");
        BaseHttp.get(Url.DOMAIN, params, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, final String str) {
                String json = str;
                if (!StringUtils.isEmptyOrNullOrNullStr(json)) {
                    ZogUtils.printError(ClanUtils.class, "initHomePageConfig json:" + json);
                    AppSPUtils.saveHomePageConfigJson(context, json);
                }
                doSomeThing.execute();
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                ZogUtils.printError(ClanUtils.class, "initHomePageConfig failed");
                doSomeThing.execute();
            }
        });
    }

    /**
     * 初始化配置项
     *
     * @param context
     * @param doSomeThing
     */
    public static void initContentConfig(final Context context, final DoSomeThing doSomeThing) {

        ClanHttpParams params = new ClanHttpParams();

        params.addQueryStringParameter("module", "plugcfg");
        params.addQueryStringParameter("iyzmobile", "1");

        BaseHttp.get(Url.DOMAIN, params, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, final String str) {
                String json = str;
                ContentConfigJson configJson = ClanUtils.parseObject(json, ContentConfigJson.class);
                if (configJson != null) {
//                    ZogUtils.printError(ClanUtils.class, "json:" + json.substring(json.length() - 100, json.length()));
                    ZogUtils.printError(ClanUtils.class, "json:" + json);
                    AppSPUtils.saveSmileyLastMD5(context, AppSPUtils.getContentConfig(context).getSmileyInfo().getMD5());

//                    ContentConfig clanConfig = configJson.getConfig();
//                    AppSPUtils.saveContentConfig(context, clanConfig);
                    AppSPUtils.saveContentConfigJson(context, json);

                }
                doSomeThing.execute();

            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                ZogUtils.printError(ClanUtils.class, "initContentConfig failed");

                doSomeThing.execute();
            }

        });


    }


    /**
     * 初始化配置项
     *
     * @param context
     * @param doSomeThing
     */
    public static void initConfig(final Context context, final DoSomeThing doSomeThing) {
        initConfig(context);
        doSomeThing.execute();
    }


    private static void initConfig(Context context) {
        String json = "{\n" +
                "\"request_id\": \"5242308408425228137\",\n" +
                "\"error_code\": 0,\n" +
                "\"error_msg\": \"SUCC\",\n" +
                "\"data\": {\n" +
                "\"api_url\": \""+ context.getResources().getString(R.string.api_url) +"\",\n" +
                "\"real_api_url\": \"\",\n" +
                "\"cfg\": 1,\n" +
                "\"api_url_path\": \"" + context.getResources().getString(R.string.api_url_path) +"\",\n" +
                "\"api_url_base\": \"" + context.getResources().getString(R.string.api_url_base) + "\",\n" +
                "\"theme\": \"" + context.getResources().getString(R.string.custom_theme) + "\"\n" +
                "}\n" +
                "}";

        if (StringUtils.isEmptyOrNullOrNullStr(json))
            return;

        ConfigJson configJson = JsonUtils.parseObject(json, ConfigJson.class);
        if (configJson != null) {
            ClanConfig clanConfig = configJson.getData();
            AppSPUtils.setConfig(clanConfig);
            AppSPUtils.saveConfig(context, clanConfig);
        }
    }

}
