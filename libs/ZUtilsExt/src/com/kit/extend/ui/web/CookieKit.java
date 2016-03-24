package com.kit.extend.ui.web;

import android.content.Context;

import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * Created by Zhao on 15/8/10.
 */
public interface CookieKit {
    public void setCookieFromCookieStore(Context context, String url, List<Cookie> cookies);
}
