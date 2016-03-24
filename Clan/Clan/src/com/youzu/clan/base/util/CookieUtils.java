package com.youzu.clan.base.util;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * Created by Zhao on 15/5/21.
 */
public class CookieUtils extends com.kit.utils.CookieUtils {



    public static void cleanCookie(Context context){
        CookieStore cookieStore = com.youzu.clan.base.net.CookieManager.getInstance().getCookieStore(context);
        cookieStore.clear();

        removeWebViewCookie(context);
    }

    public static List<Cookie>  getCookies(Context context) {

        CookieStore cookieStore = com.youzu.clan.base.net.CookieManager.getInstance().getCookieStore(context);
        List<Cookie> cookies = cookieStore.getCookies();
        return cookies;
    }



    public static void setCookieFromCookieStore(Context context, String url) {
        syncCookie(context);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = getCookies(context);
        Log.e("cookies", "cookies.size:" + cookies.size());
        if (!cookies.isEmpty()) {
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                String cookieStr = cookie.getName() + "=" + cookie.getValue() + ";"
                        + "expiry=" + cookie.getExpiryDate() + ";"
                        + "domain=" + cookie.getDomain() + ";"
                        + "path=/";

                cookieManager.setCookie(url, cookieStr);//cookies是在HttpClient中获得的cookie

            }
        }

    }




}
