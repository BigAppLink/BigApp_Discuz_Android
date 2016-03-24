package com.kit.utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by Zhao on 15/5/21.
 */
public class CookieUtils {


    public static void syncCookie(Context context) {
        //尼玛 获取cookie的时候先手动同步，不然会跪
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
        cookieSyncManager.startSync();
    }

    /**
     * 该方法可以根据要跳转的url得到需要什么格式的cookie
     *
     * @param url
     * @return
     */
    public static String getCookie(Context context,String url) {
        syncCookie(context);

        String cookie = CookieManager.getInstance().getCookie(url);
        ZogUtils.printLog(CookieUtils.class, cookie);

        return cookie;
    }

    /**
     * @param url
     * @return
     */
    public static String getCookie(Context context,String url, String domain, String path) {
        syncCookie(context);

        String cookie = CookieManager.getInstance().getCookie(url);
        cookie = cookie + "; domain=" + domain + "; path=" + path;

        ZogUtils.printLog(CookieUtils.class, cookie);

        return cookie;
    }




    public static void removeWebViewCookie(Context context) {
        syncCookie(context);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();//移除
//        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
//        CookieSyncManager.getInstance().sync();
        CookieManager.getInstance().removeAllCookie();
    }



}
