package com.youzu.clan.base.net;

import android.content.Context;

import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;

/**
 * Created by Zhao on 15/8/12.
 */
public class SmileyHttp {


    /**
     * @param callback
     */
    public static void getSmileyConfig(Context context, HttpCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "smilies");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("type", "1");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    /**
     * @param callback
     */
    public static void setSmileyConfig(Context context, HttpCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "smilies");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("pull", "1");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    /**
     * @param callback
     */
    public static void loadSmileyMap(Context context, HttpCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "smilies");
        params.addQueryStringParameter("iyzmobile", "1");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }
}
