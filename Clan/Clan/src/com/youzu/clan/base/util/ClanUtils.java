package com.youzu.clan.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.utils.GsonUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.HttpUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.json.parser.Feature;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.config.AppConfig;
import com.youzu.clan.app.receiver.MessageUrl;
import com.youzu.clan.app.service.ClanService;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.common.ErrorTag;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.enums.ClanEnvironment;
import com.youzu.clan.base.enums.ForumNavStyle;
import com.youzu.clan.base.enums.HomeTabType;
import com.youzu.clan.base.enums.MainActivityStyle;
import com.youzu.clan.base.enums.RegisterMode;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.BaseResponse;
import com.youzu.clan.base.json.ForumAdJson;
import com.youzu.clan.base.json.NewMessageJson;
import com.youzu.clan.base.json.config.AdInfo;
import com.youzu.clan.base.json.config.ClanConfig;
import com.youzu.clan.base.json.config.LoginInfo;
import com.youzu.clan.base.json.config.content.ContentConfig;
import com.youzu.clan.base.json.config.content.SettingRewrite;
import com.youzu.clan.base.json.homepageconfig.NavSetting;
import com.youzu.clan.base.json.homepageconfig.ViewTabConfig;
import com.youzu.clan.base.json.profile.Extcredit;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.net.DoLogin;
import com.youzu.clan.base.net.MyCooike;
import com.youzu.clan.base.util.jump.JumpWebUtils;
import com.youzu.clan.common.WebFragment;
import com.youzu.clan.login.CallBackWebActivity;
import com.youzu.clan.login.LoginActivity;
import com.youzu.clan.login.RegisterActivity;
import com.youzu.clan.login.WebLoginActivity;
import com.youzu.clan.main.android.PureAndroidMainActivity;
import com.youzu.clan.main.base.ChangeableIndexPageFragment;
import com.youzu.clan.main.base.ChangeableNavPageFragment;
import com.youzu.clan.main.base.IndexPageFragment;
import com.youzu.clan.main.base.PortalPageFragment;
import com.youzu.clan.main.base.forumnav.ForumParentFragment;
import com.youzu.clan.main.base.forumnav.ForumnavFragment;
import com.youzu.clan.main.bottomtab.BottomTabMainActivity;
import com.youzu.clan.main.qqstyle.QQStyleMainActivity;
import com.youzu.clan.main.sliding.SlidingStyleMainActivity;
import com.youzu.clan.main.wechatstyle.WeChatStyleMainActivity;
import com.youzu.clan.myfav.MyFavUtils;

import org.apache.http.client.CookieStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Zhao on 15/5/11.
 */
public class ClanUtils {


    public static void setDebug(Context context, boolean debugAppConfig, boolean debugAppBaseConfig) {
    }


    public static void checkNewMessage(Context context) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "checknewpm");
        params.addQueryStringParameter("iyzmobile", "1");
        NewMessageJson json = BaseHttp.postSync(Url.DOMAIN, params, NewMessageJson.class);
        if (json != null) {
            int count = 0;
            try {
                count = Integer.valueOf(json.getNewpm());
            } catch (Exception e) {
            }
            AppSPUtils.saveNewMessage(context, count);

//            Intent intent = new Intent(this, GuideActivity.class);
//            if (count > 0) {
//                NotifyUtils.mkNotity(context, ClanApplication.getNotificationManager()
//                        , null, null, "新消息", "你有" + count + "个好友发来新消息", R.drawable.ic_launcher, R.drawable.ic_notify, NotifyUtils.NOTIFY, R.string.app_name);
//            } else {
//                NotifyUtils.cancel(ClanApplication.getNotificationManager(), NotifyUtils.NOTIFY);
//            }
        }
    }


    public static void pushCookie(Context context, String cookieStr) {
        if (StringUtils.isEmptyOrNullOrNullStr(cookieStr))
            return;

        com.youzu.clan.base.net.CookieManager cm = com.youzu.clan.base.net.CookieManager.getInstance();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        CookieStore cookieStore = cm.getCookieStore(context);

//        ClanBaseUtils.printCookieStore(cookieStore);

        String[] kvs = cookieStr.split("; ");

        for (String s : kvs) {
            String[] kv = s.split("=");
            String key = "";
            String value = "";
            if (kv.length >= 1) {
                key = kv[0];
            }
            if (kv.length >= 1) {
                value = kv[1];
            }
            Log.e("APP", "key:" + key + " value:" + value);
            MyCooike cookie = new MyCooike(key, value);
//            cm.getCookieStore(context).addCookie(cookie);
            cookie.setDomain(ClanUtils.getDomainName(Url.DOMAIN));
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }

        HttpUtils httpUtils = new HttpUtils(60 * 1000);
        httpUtils.configCookieStore(cookieStore);

    }


    /**
     * 加载收藏
     *
     * @param context
     */
    public static void loadMyFav(Context context) {
        Intent intent = new Intent(context, ClanService.class);
        context.startService(intent.setAction(Action.ACTION_MY_FAV));
    }


    public static boolean isAvatarChange(Context context) {
        ClanConfig clanConfig = AppSPUtils.getConfig(context);
        if (clanConfig == null)
            return true;

        LoginInfo loginInfo = clanConfig.getLoginInfo();
        if (loginInfo == null)
            return true;

        String allow = loginInfo.getAllowAvatarChange();
        if (StringUtils.isEmptyOrNullOrNullStr(allow)) {
            return true;
        }
        return "1".equals(allow);
    }


    /**
     * 获得启动进入的界面
     *
     * @param context
     * @return
     */
    public static Class getMain(Context context) {

        int appStyle = AppSPUtils.getConfig(context).getAppStyle();

        ZogUtils.printError(ClanUtils.class, "AppUSPUtils.getUMainStyle(context):" + AppUSPUtils.getUMainStyle(context));

        if (AppUSPUtils.getUMainStyle(context) == 0) {
            appStyle = (appStyle <= 0 ? MainActivityStyle.DEFAULT : appStyle);
        } else {
            appStyle = (AppUSPUtils.getUMainStyle(context) <= 0
                    ? MainActivityStyle.DEFAULT : AppUSPUtils.getUMainStyle(context));
        }

        ZogUtils.printError(ClanUtils.class, ":::::::appStyle:::::::" + appStyle);

        switch (appStyle) {
            case MainActivityStyle.DEFAULT:
            case MainActivityStyle.TAB_BOTTOM:
//                return WeChatStyleMainActivity.class;
                return BottomTabMainActivity.class;
            case MainActivityStyle.SLIDING:
                return SlidingStyleMainActivity.class;
            case MainActivityStyle.QQ:
                return QQStyleMainActivity.class;
            case MainActivityStyle.QZONE:
                return WeChatStyleMainActivity.class;
            case MainActivityStyle.ANDROID:
                return PureAndroidMainActivity.class;
            default:
                return BottomTabMainActivity.class;
        }

    }


    /**
     * 初始化签到按钮
     */
    public static void initSignIn(Context context, ImageButton signIn) {
        signIn.setVisibility((isUseSignIn(context) && AppSPUtils.isLogined(context)) ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否有签到的功能
     */
    public static boolean isUseSignIn(Context context) {
        return ("1").equals(AppSPUtils.getContentConfig(context).getCheckinEnabled());
    }

    /**
     * 获取Jpush极光推送的别名s
     *
     * @return
     */
    public static String getJPushAlias(Context context) {
        String appid = AppSPUtils.getConfig(context).getAppId();
        appid = StringUtils.isEmptyOrNullOrNullStr(appid) ? "" : appid;

        String uid = AppSPUtils.getUid(context);

        String temp = "00000000000000000000";
        int length = temp.length();
        ZogUtils.printError(ClanUtils.class, "temp.length():" + length);

        appid = temp.substring(0, length - appid.length()) + appid;
        ZogUtils.printError(ClanUtils.class, "appid.length():" + appid.length());

        uid = temp.substring(0, length - uid.length()) + uid;
        ZogUtils.printError(ClanUtils.class, "uid.length():" + uid.length());


        String alias = appid + uid;

        ZogUtils.printError(ClanUtils.class, "alias.length():" + alias.length());
        ZogUtils.printError(ClanUtils.class, "alias:" + alias);


        return alias;
    }


    public static void initCustomActionBar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.pure_android_action_bar);
    }


    public static String resizePicSize(Context context, String str, String resize) {

        String regex = "(size=[^&\"]*)";

        if (AppUSPUtils.isPicSize(context)
                && !StringUtils.isEmptyOrNullOrNullStr(resize)
                && !StringUtils.isEmptyOrNullOrNullStr(str)) {

            str = str.replaceAll(regex, "size=" + resize);

//            ZogUtils.printError(ClanUtils.class, "str::::" + str);
        }

//        Pattern pat = Pattern.compile(regex);
//        Matcher matcher = pat.matcher(str);
//        while (matcher.find()) {
//            String temp = str.substring(matcher.start(), matcher.end());
//            str = str.replaceAll(temp, temp.substring(0, temp.lastIndexOf("条")) + "行");
//        }

        return str;
    }

    public static void dealImg(String htmlStr) {
        String img = "";
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
//            while(m.find()){
//                pics.add(m.group(1));
//            }
        }
    }

    /**
     * 添加小尾巴
     *
     * @param context
     * @param content
     * @return
     */
    public static String appendContent(Context context, String content) {

        String tail = AppUSPUtils.getTailStr(context);
        //        tail = " &lt;p class='zhao_little_tail'&gt;这是我的小尾巴~&lt;/p&gt;";
        if (AppUSPUtils.isTail(context) && !StringUtils.isEmptyOrNullOrNullStr(tail)) {
            tail = "[/p][/p]" + tail;
            content = content + " " + tail;
        }
        return content;
    }


    public static BaseFragment getForumNav(Context context) {
        switch (AppSPUtils.getContentConfig(context).getDisplayStyle()) {
            case ForumNavStyle.NORMAL:
                return new ForumnavFragment();
            case ForumNavStyle.TOP:
            case ForumNavStyle.SLIDE:
                return new ForumParentFragment();
            default:
                return new ForumnavFragment();
        }
    }

    public static BaseFragment getIndexFragment(Context context) {
        if (AppSPUtils.getContentConfig(context) != null
                && AppSPUtils.getContentConfig(context).getPortalconfig() != null
                && AppSPUtils.getContentConfig(context).getPortalconfig().size() > 1) {
            return PortalPageFragment.getInstance();
        } else {
            return IndexPageFragment.getInstance(null);
        }
    }


    /**
     * 获取首页的Fragment，此Fragment与之前不同，可自定义
     *
     * @param
     * @return
     */
    public static Fragment getChangeableHomeFragment(ViewTabConfig viewTabConfig) {

        BaseFragment fragment = null;
        switch (viewTabConfig.getTabType()) {
            case HomeTabType.TAB_SINGLE_PAGE_TYPE:
                fragment = new ChangeableIndexPageFragment();
                ((ChangeableIndexPageFragment) fragment).setHomePager(viewTabConfig.getHomePage());
                return fragment;
            case HomeTabType.TAB_NAVIGATION_TYPE:
                fragment = new ChangeableNavPageFragment();
                ((ChangeableNavPageFragment) fragment).setViewTabConfig(viewTabConfig);
                return fragment;
            case HomeTabType.TAB_WAP_TYPE:
                WebFragment webFragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("content", viewTabConfig.getWapPage());
                webFragment.setArguments(bundle);
                return webFragment;
            default:
                fragment = new BaseFragment();
                return fragment;
        }

    }


    /**
     * 获取导航形式的Fragment
     *
     * @param navSetting
     * @return
     */
    public static Fragment getNavPageFragment(NavSetting navSetting) {

        ZogUtils.printError(ClanUtils.class, "navSetting.getTabType():" + navSetting.getTabType() + " @  navSetting:" + JsonUtils.toJSONString(navSetting));


        switch (navSetting.getTabType()) {

            case HomeTabType.TAB_SINGLE_PAGE_TYPE:
            case HomeTabType.TAB_NAVIGATION_TYPE:
                ChangeableIndexPageFragment changeableIndexPageFragment = new ChangeableIndexPageFragment();
                changeableIndexPageFragment.setHomePager(navSetting.getHomePage());
                return changeableIndexPageFragment;

            case HomeTabType.TAB_WAP_TYPE:
                WebFragment webFragment = new WebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("content", navSetting.getWapPage());
                webFragment.setArguments(bundle);
                return webFragment;

            default:
                BaseFragment fragment = new BaseFragment();
                return fragment;

        }

//        if (HomeTabType.TAB_SINGLE_PAGE_TYPE.equals(navSetting.getTabType())) {
//            ChangeableIndexPageFragment fragment = new ChangeableIndexPageFragment();
//            fragment.setHomePager(navSetting.getHomePage());
//            return fragment;
//        } else if (HomeTabType.TAB_WAP_TYPE.equals(navSetting.getTabType())) {
////        } else {
//            WebFragment webFragment = new WebFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("content", navSetting.getWapPage());
//            webFragment.setArguments(bundle);
//            return webFragment;
//        } else {
//            BaseFragment fragment = new BaseFragment();
//            return fragment;
//        }
    }

    /**
     * 判断是否启用了分享
     *
     * @param context
     * @param platformName
     * @return
     */
    public static boolean isUseShareSDKPlatformName(Context context, String platformName) {
        switch (platformName) {
            case "Wechat":
                if (context.getString(R.string.use_wechat).equals("1")
                        || context.getString(R.string.use_wechat).equals("use_wechat_ok")) {
                    return true;
                }
                break;


            case "QQ":
                if (context.getString(R.string.use_qq).equals("1")
                        || context.getString(R.string.use_qq).equals("use_qq_ok")) {
                    return true;
                }
                break;

            case "SinaWeibo":
                if (context.getString(R.string.use_weibo).equals("1")
                        || context.getString(R.string.use_weibo).equals("use_weibo_ok")) {
                    return true;
                }
                break;


        }
        return false;
    }

    /**
     * 登录用户是否是uid用户
     *
     * @return
     */
    public static boolean isOwner(FragmentActivity context, String uid) {
        ClanApplication mApplication = (ClanApplication) context.getApplication();
        if (AppSPUtils.getUid(context).equals(uid)) {
            return true;
        }

        return false;
    }


    public static void logout(FragmentActivity activity, String msg) {
        ZogUtils.printError(ClanUtils.class, "logout_succeed");

        ToastUtils.mkLongTimeToast(activity, msg);

        PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().commit();
        AppSPUtils.saveAvatarUrl(activity, "");
        MyFavUtils.deleteAllForum(activity);
        MyFavUtils.deleteAllThread(activity);
        MyFavUtils.deleteAllArticle(activity);

        AppSPUtils.setLoginInfo(activity, false, "0", "");

        activity.setResult(ResultCode.RESULT_CODE_EXIT);
        activity.finish();
    }

    public static boolean isToLogin(Activity activity, BundleData bundleData, int requestCode, boolean isFinishThis) {
        if (!AppSPUtils.isLogined(activity)) {
            gotoLogin(activity, bundleData, requestCode, isFinishThis);
            return true;
        }
        return false;
    }

    public static boolean isToLogin(Fragment fragment, BundleData bundleData, int requestCode, boolean isFinishThis) {
        if (!AppSPUtils.isLogined(fragment.getActivity())) {
            gotoLogin(fragment, bundleData, requestCode, isFinishThis);
            return true;
        }
        return false;
    }

    /**
     * 跳转到注册界面
     *
     * @param activity
     * @param bundleData
     * @param requestCode
     * @param isFinishThis
     */
    public static void gotoRegsiter(Activity activity, BundleData bundleData, int requestCode, boolean isFinishThis) {
        int regMode = AppSPUtils.getConfig(activity).getLoginInfo().getRegMod();
        ZogUtils.printError(ClanUtils.class, "AppConfig.REG_MODE:" + regMode);

        if (regMode == RegisterMode.NATIVE) {
            //native
            IntentUtils.gotoSingleNextActivity(activity, RegisterActivity.class, bundleData, isFinishThis, requestCode);
        } else {
            //web
            if (bundleData == null) {
                String url = AppSPUtils.getConfig(activity).getLoginInfo().getRegUrl();
//                String url = "http://192.168.180.23:8080/discuz/member.php?mod=register&mobile=2";

                String cookie = CookieUtils.getCookie(activity, url);

                bundleData = new BundleData();
                bundleData.put("title", activity.getResources().getString(R.string.register));
                bundleData.put("content", url);
                bundleData.put("cookie", cookie);
            }
            IntentUtils.gotoNextActivity(activity, CallBackWebActivity.class, bundleData, requestCode);
        }
    }


    /**
     * 跳转到注册界面
     *
     * @param activity
     * @param requestCode
     * @param isFinishThis
     */
    public static void gotoThirdLogin(Activity activity, String url, String title, int requestCode, boolean isFinishThis) {
        ZogUtils.printError(ClanUtils.class, "AppConfig.REG_MODE:" + AppSPUtils.getConfig(activity).getLoginInfo().getRegMod());

        //web
//                String url = "http://192.168.180.23:8080/discuz/member.php?mod=register&mobile=2";
        String cookie = CookieUtils.getCookie(activity, url);

        BundleData bundleData = new BundleData();
        bundleData.put("title", title);
        bundleData.put("content", url);
        bundleData.put("cookie", cookie);
        IntentUtils.gotoNextActivity(activity, CallBackWebActivity.class, bundleData, isFinishThis, requestCode);
    }


    public static void gotoPlatformLogin(FragmentActivity context, Handler.Callback callback, InjectDo injectDo) {
        DoLogin.authorize(context, Wechat.NAME, callback, injectDo);
    }

    /**
     * 跳转到登录界面
     *
     * @param activity
     * @param bundleData
     * @param requestCode
     * @param isFinishThis
     */
    public static void gotoLogin(Context activity, BundleData bundleData, int requestCode, boolean isFinishThis) {
        LoginInfo loginInfo = AppSPUtils.getLoginInfo(activity);

        if (loginInfo == null) {
            return;
        }
        ZogUtils.printError(ClanUtils.class, "activity LOGIN_MODE:" + loginInfo.getLoginMod());

        if (loginInfo.getLoginMod() == 0) {
            //native
            IntentUtils.gotoSingleNextActivity(activity, LoginActivity.class, bundleData, isFinishThis, requestCode);
        } else {
            //web

            if (bundleData == null) {
                String url = loginInfo.getLoginUrl();
//                String url = "http://192.168.180.23:8080/discuz/member.php?mod=logging&action=login&mobile=2";

                String cookie = CookieUtils.getCookie(activity, url);

                bundleData = new BundleData();
                bundleData.put("title", activity.getResources().getString(R.string.login));
                bundleData.put("content", url);
                bundleData.put("cookie", cookie);
            }
            IntentUtils.gotoNextActivity(activity, WebLoginActivity.class, bundleData, requestCode);
        }
    }


    /**
     * 跳转到登录界面
     *
     * @param fragment
     * @param bundleData
     * @param requestCode
     * @param isFinishThis
     */
    public static void gotoLogin(Fragment fragment, BundleData bundleData, int requestCode, boolean isFinishThis) {
        LoginInfo loginInfo = AppSPUtils.getLoginInfo(fragment.getActivity());

        if (loginInfo == null) {
            return;
        }
        ZogUtils.printError(ClanUtils.class, "fragment LOGIN_MODE:" + loginInfo.getLoginMod());

        if (loginInfo.getLoginMod() == 0) {
            //native
            IntentUtils.gotoSingleNextActivity(fragment, LoginActivity.class, bundleData, requestCode, isFinishThis);
        } else {
            //web

            if (bundleData == null) {
                String url = loginInfo.getLoginUrl();
//                String url = "http://192.168.180.23:8080/discuz/member.php?mod=logging&action=login&mobile=2";

                String cookie = CookieUtils.getCookie(fragment.getActivity(), url);

                bundleData = new BundleData();
                bundleData.put("title", fragment.getActivity().getResources().getString(R.string.login));
                bundleData.put("content", url);
                bundleData.put("cookie", cookie);
            }
            IntentUtils.gotoSingleNextActivity(fragment, WebLoginActivity.class, bundleData, requestCode);
        }
    }


    /**
     * 获取domain
     *
     * @param url
     * @return
     */
    public static String getDomainName(String url) {
//        String s = "http://www.baidu.com";
//String s = "www.baidu.com";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher m = p.matcher(url);
        if (m.find()) {
            ZogUtils.printLog(ClanUtils.class, m.group());
            return m.group();
        }

        return null;
    }


//    public static void toProfilePage(FragmentActivity context, String uid) {
//        ClanApplication mApplication = (ClanApplication) context.getApplication();
//
//        if (!AppSPUtils.isLogined(context)) {
////			toLogin();
//            ClanUtils.gotoLogin(context, null, 1, false);
//            return;
//        }
////        Intent intent = new Intent();
////        if (uid.equals(myUid)) {
////        intent.setClass(context, MyHomePageActivity.class);
////        }else{
////          intent.setClass(context, OthersPageActivity.class);
////        }
//
////        intent.setClass(context, HomePageActivity.class);
//
//        ZogUtils.printError(ClanUtils.class, "toProfilePage uid:" + uid);
////        intent.putExtra(Key.KEY_UID, uid);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(intent);
//        Bundle bundle = new Bundle();
//        bundle.putString(Key.KEY_UID, uid);
//        IntentUtils.gotoNextActivity(context, HomePageActivity.class, bundle);
//    }


    public static void showAd(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        JumpWebUtils.gotoWeb(context, "", url);
    }

    public static String getLevelStr(Context context, Space space) {
        StringBuffer sb = new StringBuffer();
        Extcredit[] extcredits = space.getExtcredits();
        sb.append(context.getString(R.string.credits) + " " + space.getCredits());
        sb.append("  |  ");

        if (extcredits != null) {
            int size = Math.min(extcredits.length, 2);
            for (int i = 0; i < size; i++) {
                Extcredit extcredit = extcredits[i];
                sb.append(extcredit.getName() + " ");
                sb.append(extcredit.getValue());
                if (i != size - 1) {
                    sb.append("  |  ");
                }
            }
        }
        String str = sb.toString().trim();
        if (str.length() > 0 && str.substring(str.length() - 1, str.length()).equals("|")) {
            str = str.substring(str.length() - 1, str.length()).trim();

        }
        return str;
    }


    public static void setLevel(Space space, ArrayList<LinearLayout> lls) {

        if ((lls == null || lls.size() < 1)) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        Extcredit[] extcredits = space.getExtcredits();
        if (extcredits != null) {
            int size = Math.min(extcredits.length, lls.size());
            for (int i = 0; i < size; i++) {
                Extcredit extcredit = extcredits[i];
                lls.get(i).setVisibility(View.VISIBLE);
                TextView textView = (TextView) lls.get(i).findViewById(R.id.resource);
                TextView textView2 = (TextView) lls.get(i).findViewById(R.id.resourceName);
                textView.setText(extcredit.getValue());
                textView2.setText(extcredit.getName());
            }
        }
    }


    public static String computeThreadTime(long timestaps) {
        long ts = System.currentTimeMillis() - timestaps;
        if (ts > 12 * 60 * 60 * 1000) {
            return DateUtils.timestampToDate(timestaps,
                    DateUtils.DATE_SMALL_STR);
        }
        if (ts < 60 * 1000) {
            return ts / 1000 + "秒前";
        }
        if (ts < 60 * 60 * 1000) {
            return ts / (60 * 1000) + "分钟前";
        }

        return ts / (60 * 60 * 1000) + "小时前";
    }


    /**
     * @param context
     * @param json
     * @param successTag
     * @param successStringId
     * @param errorStringId
     * @param callback
     * @param isUseSuccessTagMsg 是否使用tag对应的msg，如果不使用，则提示successStringId
     * @param isUseFailTagMsg    是否使用失败的时候返回的msg，如果不使用，则提示errorStringId
     * @param injectDo
     * @return
     */
    public static BaseJson dealMsg(Context context, String json, String successTag, int successStringId,
                                   int errorStringId, HttpCallback callback, boolean isUseSuccessTagMsg,
                                   boolean isUseFailTagMsg, InjectDo injectDo) {
        String info = null;

        String success = context.getString(successStringId);
        String error = context.getString(errorStringId);

        BaseJson baseJson = ClanUtils.parseObject(json, BaseJson.class);

        if (baseJson != null
                && baseJson.getMessage() != null
                && !StringUtils.isEmptyOrNullOrNullStr(baseJson.getMessage().getMessageval())) {
            String tag = baseJson.getMessage().getMessageval();
            String msg = baseJson.getMessage().getMessagestr();

            if (tag.equals(successTag)) {//成功
                info = success;
                if (isUseSuccessTagMsg && !StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = msg;
                }
                if (injectDo != null) {
                    if (injectDo.doSuccess(baseJson) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                        ToastUtils.mkShortTimeToast(context, info);
                    }
                }
//                callback.onSuccess(baseJson);
            } else {//失败
                info = error;
                if (isUseFailTagMsg && !StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = msg;

                }
                if (injectDo != null && injectDo.doFail(baseJson, tag) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                    ToastUtils.mkShortTimeToast(context, info);
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, ErrorTag.NO_SHOW);
                } else
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, info);
            }


        } else {
//            Message serverError = ClanBaseUtils.dealFail(context, json);
//            if (!StringUtils.isEmptyOrNullOrNullStr(serverError.getMessagestr())) {
//                error = serverError.getMessagestr();
//                ToastUtils.mkShortTimeToast(context, error);
//            }

            callback.onFailed(context, ErrorCode.ERROR_DEFAULT, json);
        }

        return baseJson;

    }

    /**
     * @param context
     * @param json
     * @param successStringId
     * @param errorStringId
     * @param callback
     * @param injectDo
     * @return
     */
    public static BaseJson dealMsg(Context context, String json, int successStringId, int errorStringId, HttpCallback callback, InjectDo injectDo) {
        String info = null;
        String success = "";
        String error = "";

        if (successStringId != 0)
            success = context.getString(successStringId);

        if (errorStringId != 0)
            error = context.getString(errorStringId);

        BaseJson baseJson = ClanUtils.parseObject(json, BaseJson.class);
        if (baseJson != null) {
            String msg = "";
            if (baseJson.getMessage() != null) {
                String tag = baseJson.getMessage().getMessageval();
                msg = baseJson.getMessage().getMessagestr();

                info = error;
                if (!StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = msg;
                }
                if (injectDo != null && injectDo.doFail(baseJson, tag) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                    ToastUtils.mkShortTimeToast(context, info);
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, ErrorTag.NO_SHOW);
                } else
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, info);
            } else {
                if (StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = success;
                }

                if (injectDo != null && injectDo.doSuccess(baseJson) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                    ToastUtils.mkShortTimeToast(context, info);
                }

            }
        } else {
            callback.onFailed(context, ErrorCode.ERROR_DEFAULT, json);
        }

        return baseJson;

    }

    /**
     * @param context
     * @param json
     * @param successStringId
     * @param errorStringId
     * @param callback
     * @param injectDo
     * @return
     */
    public static BaseResponse dealMsgByBaseResponse(Context context, String json, int successStringId, int errorStringId, HttpCallback callback, InjectDo injectDo) {
        String info = null;

        String success = context.getString(successStringId);
        String error = context.getString(errorStringId);

        BaseResponse baseJson = ClanUtils.parseObject(json, BaseResponse.class);
        if (baseJson != null) {
            String msg = baseJson.getErrorMsg();
            String code = baseJson.getErrorCode();

            if (baseJson != null
                    && "0".equals(baseJson.getErrorCode())) {
                if (StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = success;
                }

                if (injectDo != null && injectDo.doSuccess(baseJson) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                    ToastUtils.mkShortTimeToast(context, info);
                }
//                callback.onSuccess(baseJson);
            } else {//失败

                if (StringUtils.isEmptyOrNullOrNullStr(msg)) {
                    info = error;
                }

                if (injectDo != null && injectDo.doFail(baseJson, code) && !StringUtils.isEmptyOrNullOrNullStr(info)) {
                    ToastUtils.mkShortTimeToast(context, info);
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, ErrorTag.NO_SHOW);
                } else
                    callback.onFailed(context, ErrorCode.ERROR_DEFAULT, info);
            }

        } else {
//            Message serverError = ClanBaseUtils.dealFail(context, json);
//            if (!StringUtils.isEmptyOrNullOrNullStr(serverError.getMessagestr())) {
//                error = serverError.getMessagestr();
//            }
//
//            ToastUtils.mkShortTimeToast(context, error);

            callback.onFailed(context, ErrorCode.ERROR_DEFAULT, json);
        }

        return baseJson;

    }


    public static int getEnvironment(Context context) {
        return ClanEnvironment.ONLINE;
    }


    public static int parseInt(String text) {
        if (StringUtils.isEmptyOrNullOrNullStr(text)) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }


    public static <T> T parseObject(String text, Class<T> clazz, Feature... features) {

        T t = null;
        try {
            t = JsonUtils.parseObject(text, clazz);
        } catch (Exception e) {

            ZogUtils.printError(ClanUtils.class, GsonUtils.toJson(e));

            ZogUtils.printError(ClanUtils.class, e.toString());
        }
        return t;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz, Feature... features) {

        List<T> t = null;
        try {
            t = JsonUtils.parseArray(text, clazz);
        } catch (Exception e) {
            ZogUtils.printError(ClanUtils.class, e.toString());
        }
        return t;
    }

    public static LinkedHashMap<String, String> getOrder(Map<String, String> map) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                int p1 = Integer.parseInt(o1.getKey());
                int p2 = Integer.parseInt(o2.getKey());
                return p1 - p2;
            }
        });

        /*转换成新map输出*/
        LinkedHashMap<String, String> newMap = new LinkedHashMap<String, String>();

        for (Map.Entry<String, String> entity : infoIds) {
            newMap.put(entity.getKey(), entity.getValue());
        }

        return newMap;
    }

    public static MessageUrl checkUrlPatten(Context context, String url) {
        ZogUtils.printError(ClanUtils.class, "检查URL格式 checkUrlPatten url = " + url + ", baseUrl = " + Url.BASE);
        if (TextUtils.isEmpty(url) || !url.startsWith(Url.BASE)) {
            return null;
        }
        MessageUrl messageUrl = null;
        try {
            ContentConfig config = AppSPUtils.getContentConfig(context);
            if (config != null && config.getSettingRewrite() != null && config.getSettingRewrite().size() > 0) {
                ArrayList<SettingRewrite> list = config.getSettingRewrite();
                for (SettingRewrite item : list) {
                    if (item != null) {
                        Pattern p_item = Pattern.compile(item.getRegex(), Pattern.CASE_INSENSITIVE);
                        if (p_item != null) {
                            Matcher m_item = p_item.matcher(url);
                            if (m_item != null && m_item.find()) {
                                //匹配到了
                                if ("portal_article".equals(item.getType())) {//文章详情
                                    ArrayList<String> insteads = item.getInsteads();
                                    if (insteads != null && m_item.groupCount() == insteads.size()) {
                                        messageUrl = new MessageUrl();
                                        messageUrl.type = "portal_article";
                                        for (int i = 1; i <= m_item.groupCount(); i++) {
                                            if ("id".equals(insteads.get(i - 1))) {
                                                messageUrl.id = m_item.group(i);
                                            } else if ("page".equals(insteads.get(i - 1))) {
                                                messageUrl.page = m_item.group(i);
                                            }
                                        }
                                    }
                                    break;
                                } else if ("forum_forumdisplay".equals(item.getType())) {//版块详情
                                    ArrayList<String> insteads = item.getInsteads();
                                    if (insteads != null && m_item.groupCount() == insteads.size()) {
                                        messageUrl = new MessageUrl();
                                        messageUrl.type = "forum_forumdisplay";
                                        for (int i = 1; i <= m_item.groupCount(); i++) {
                                            if ("fid".equals(insteads.get(i - 1))) {
                                                messageUrl.id = m_item.group(i);
                                            } else if ("page".equals(insteads.get(i - 1))) {
                                                messageUrl.page = m_item.group(i);
                                            }
                                        }
                                    }
                                    break;
                                } else if ("forum_viewthread".equals(item.getType())) {//帖子详情
                                    ArrayList<String> insteads = item.getInsteads();
                                    if (insteads != null && m_item.groupCount() == insteads.size()) {
                                        messageUrl = new MessageUrl();
                                        messageUrl.type = "forum_viewthread";
                                        for (int i = 1; i <= m_item.groupCount(); i++) {
                                            if ("tid".equals(insteads.get(i - 1))) {
                                                messageUrl.id = m_item.group(i);
                                            } else if ("page".equals(insteads.get(i - 1))) {
                                                messageUrl.page = m_item.group(i);
                                            } else if ("prevpage".equals(insteads.get(i - 1))) {
                                                messageUrl.prevpage = m_item.group(i);
                                            }
                                        }
                                    }
                                    break;
                                } else if ("home_space".equals(item.getType())) {//个人主页
                                    ArrayList<String> insteads = item.getInsteads();
                                    if (insteads != null && m_item.groupCount() == insteads.size()) {
                                        messageUrl = new MessageUrl();
                                        messageUrl.type = "home_space";
                                        for (int i = 1; i <= m_item.groupCount(); i++) {
                                            if ("user".equals(insteads.get(i - 1))) {
                                                messageUrl.key = m_item.group(i);
                                            } else if ("value".equals(insteads.get(i - 1))) {
                                                messageUrl.value = m_item.group(i);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (messageUrl == null) {
                //如果静态没有匹配上，则匹配动态url
                //个人主页
                Pattern p_item = Pattern.compile("uid=(\\d+)", Pattern.CASE_INSENSITIVE);
                Matcher m_item = p_item.matcher(url);
                if (m_item != null && m_item.find()) {
                    messageUrl = new MessageUrl();
                    messageUrl.type = "home_space";
                    messageUrl.key = "uid";
                    messageUrl.value = m_item.group(1);
                    return messageUrl;
                }
                p_item = Pattern.compile("username=(\\d+)", Pattern.CASE_INSENSITIVE);
                m_item = p_item.matcher(url);
                if (m_item != null && m_item.find()) {
                    messageUrl = new MessageUrl();
                    messageUrl.type = "home_space";
                    messageUrl.key = "username";
                    messageUrl.value = m_item.group(1);
                    return messageUrl;
                }
                //文章详情
                p_item = Pattern.compile("aid=(\\d+)", Pattern.CASE_INSENSITIVE);
                m_item = p_item.matcher(url);
                if (m_item != null && m_item.find()) {
                    messageUrl = new MessageUrl();
                    messageUrl.type = "portal_article";
                    messageUrl.id = m_item.group(1);
                    //获取page
                    p_item = Pattern.compile("page%3D(\\d+)", Pattern.CASE_INSENSITIVE);
                    m_item = p_item.matcher(url);
                    if (m_item != null && m_item.find()) {
                        messageUrl.page = m_item.group(1);
                    }
                    return messageUrl;
                }
                //版块详情
                p_item = Pattern.compile("fid=(\\d+)", Pattern.CASE_INSENSITIVE);
                m_item = p_item.matcher(url);
                if (m_item != null && m_item.find()) {
                    messageUrl = new MessageUrl();
                    messageUrl.type = "forum_forumdisplay";
                    messageUrl.id = m_item.group(1);
                    //获取page
                    p_item = Pattern.compile("page%3D(\\d+)", Pattern.CASE_INSENSITIVE);
                    m_item = p_item.matcher(url);
                    if (m_item != null && m_item.find()) {
                        messageUrl.page = m_item.group(1);
                    }
                    return messageUrl;
                }
                //帖子详情
                p_item = Pattern.compile("tid=(\\d+)", Pattern.CASE_INSENSITIVE);
                m_item = p_item.matcher(url);
                if (m_item != null && m_item.find()) {
                    messageUrl = new MessageUrl();
                    messageUrl.type = "forum_viewthread";
                    messageUrl.id = m_item.group(1);
                    //获取page
                    p_item = Pattern.compile("page%3D(\\d+)", Pattern.CASE_INSENSITIVE);
                    m_item = p_item.matcher(url);
                    if (m_item != null && m_item.find()) {
                        messageUrl.page = m_item.group(1);
                    }
                    return messageUrl;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageUrl = null;
        }
        return messageUrl;
    }

}
