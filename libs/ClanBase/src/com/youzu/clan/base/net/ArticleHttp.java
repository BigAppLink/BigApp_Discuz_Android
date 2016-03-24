package com.youzu.clan.base.net;

import android.content.Context;

import com.kit.utils.StringUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.util.ClanBaseUtils;

/**
 * Created by tangh on 2015/8/31.
 */
public class ArticleHttp {

    /**
     * 获取相关分类下的所有文章列表
     * @param context
     * @param catid
     * @param callback
     */
    public static void getHomeArticle(Context context, String catid,int cacheType, int page,final JSONCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.setCacheMode(cacheType);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        params.addQueryStringParameter("module", "myportal");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("catid", catid);
        params.addQueryStringParameter("mod", "list");
        params.addQueryStringParameter("page", page+"");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 获取相关文章
     * @param context
     * @param aid 文章id
     * @param callback
     */
    public static void getArticleDetail(Context context, String aid,final JSONCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        params.addQueryStringParameter("module", "myportal");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("aid", aid);
        params.addQueryStringParameter("mod", "view");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 获取收藏的文件列表的params
     * @param context
     * @param page
     */
    public static ClanHttpParams getArticleFavsParams(Context context,int page){
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "myfavarticle");
        params.addQueryStringParameter("type", "article");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("page", String.valueOf(page));
        return params;
    }
    /**
     * 添加收藏文章
     * @param context
     * @param callback
     */
    public static void addArticleFavs(Context context, String aid, final ProgressCallback callback){
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "favarticle");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("id", aid);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        params.addBodyParameter("favoritesubmit", "true");
        BaseHttp.post(Url.DOMAIN, params, callback);
    }

}
