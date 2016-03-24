package com.youzu.clan.base.net;

import android.content.Context;

import com.kit.utils.ListUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.http.RequestParams;
import com.youzu.clan.base.config.AppBaseConfig;

/**
 * http请求参数
 *
 * @author wangxi
 */
public class ClanHttpParams extends RequestParams {

    private int cacheMode = HttpCache.ONLY_NET;
    private long cacheTime = 10 * 1000;

    private Context context;

    //超时时间
    private int timeout = 30 * 1000;

    public ClanHttpParams() {
        init();
    }

    public ClanHttpParams(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        addQueryStringParameter("version", "4");
        addQueryStringParameter("android", "1");
        addQueryStringParameter("iyzversion", AppBaseConfig.IYZ_VERSION);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getCacheMode() {
        return cacheMode;
    }

    public ClanHttpParams setCacheMode(int cacheMode) {
        this.cacheMode = cacheMode;
        return this;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public ClanHttpParams setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }

    public void clearParams() {
        if (!ListUtils.isNullOrContainEmpty(getQueryStringParams()))
            getQueryStringParams().clear();

        if (!ListUtils.isNullOrContainEmpty(getBodyParams()))
            getBodyParams().clear();
    }

}
