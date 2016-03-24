package com.youzu.clan.base.net;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.kit.utils.ZZLogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.blog.ReqBlogListParam;

public class BlogHttp {

    /***
     * 获取日志列表信息
     */
    public static void getBlogList(FragmentActivity activity,
                                   ReqBlogListParam reqParam, int page, HttpCallback callback) {
        ZZLogUtils.log("BlogHttp", "getBlogList:::::" + JsonUtils.toJSONString(reqParam));
        ClanHttpParams params = new ClanHttpParams(activity);
        params.setTimeout(60 * 1000);

        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("iyzversion", "4");
        params.addQueryStringParameter("module", "myblog");
        params.addQueryStringParameter("action", "list");

        params.addQueryStringParameter("view", reqParam.view);
        params.addQueryStringParameter("page", page + "");
        if (!TextUtils.isEmpty(reqParam.classid)) {
            params.addQueryStringParameter("catid", reqParam.catid);
        }
        if (!TextUtils.isEmpty(reqParam.classid)) {
            params.addQueryStringParameter("classid", reqParam.classid);
        }
        if (!TextUtils.isEmpty(reqParam.uid)) {
            params.addQueryStringParameter("uid", reqParam.uid);
        }
        if (!TextUtils.isEmpty(reqParam.order)) {
            params.addQueryStringParameter("order", reqParam.order);
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }
}
