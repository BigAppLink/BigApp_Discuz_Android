package com.youzu.clan.base.net;

import android.content.Context;

import com.kit.utils.StringUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.util.ClanBaseUtils;

/**
 * Created by Zhao on 15/9/10.
 */
public class MessageHttp {

    /**
     * 发送消息
     *
     * @param context
     * @param message
     * @param toUid
     * @param toUsername
     * @param callback
     */
    public static void send(Context context, String message, String toUid,
                            String toUsername, final HttpCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.setCacheMode(HttpCache.ONLY_NET);
        params.addQueryStringParameter("module", "sendpm");
        params.addQueryStringParameter("touid", toUid);
        params.addBodyParameter("pmsubmit", "true");
        params.addBodyParameter("username", toUsername);
        params.addBodyParameter("pmsubmit_btn", "true");
        params.addBodyParameter("message", message);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));


        BaseHttp.post(Url.DOMAIN, params, callback);
    }


    /**
     * 发送消息
     *
     * @param context
     * @param message
     * @param toUid
     * @param toUsername
     * @param callback
     */
    public static void send(Context context, String requestId, String message, String toUid,
                            String toUsername, final HttpCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.setCacheMode(HttpCache.ONLY_NET);
        params.addQueryStringParameter("module", "sendpm");
        params.addQueryStringParameter("touid", toUid);
        params.addQueryStringParameter("request_id", requestId);
        params.addBodyParameter("pmsubmit", "true");
        params.addBodyParameter("username", toUsername);
        params.addBodyParameter("pmsubmit_btn", "true");
        params.addBodyParameter("message", message);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));


        BaseHttp.post(Url.DOMAIN, params, callback);
    }
}
