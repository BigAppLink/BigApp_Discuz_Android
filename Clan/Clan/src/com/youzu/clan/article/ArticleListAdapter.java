package com.youzu.clan.article;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.kit.utils.ListUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.json.article.Article;
import com.youzu.clan.base.json.article.ArticleJson;
import com.youzu.clan.base.net.ArticleHttp;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.main.qqstyle.OnEmptyDataListener;
import com.youzu.clan.main.base.adapter.IndexPageAdapter;
import com.youzu.clan.main.base.listener.OnBannerInitOKListener;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/9/2.
 */
public class ArticleListAdapter extends IndexPageAdapter {
    private String catId;
    private int page = 1;
    private Context context;

    public ArticleListAdapter(FragmentActivity context, OnEmptyDataListener mListener, OnBannerInitOKListener onBannerInitOKListener) {
        super(context, mListener, onBannerInitOKListener);
        this.context = context;
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @Override
    public void refresh(OnLoadListener listener) {

        page=1;
        loadArticle(listener);
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    @Override
    protected String getCatId() {
        return catId;
    }

    @Override
    public void loadMore(final OnLoadListener listener) {

        page++;
        ArticleHttp.getHomeArticle(context, getCatId(), HttpCache.ONLY_NET, page,new JSONCallback() {
            @Override
            public void onSuccess(Context ctx, String jsonStr) {
                super.onSuccess(ctx, jsonStr);

                ArticleJson hotThreadJson = JsonUtils.parseObject(jsonStr, ArticleJson.class);
                boolean hasMore = false;
                if (hotThreadJson != null && hotThreadJson.getVariables() != null) {
                    // 主题列表
                    ArrayList<Article> articles = hotThreadJson.getVariables().getData();

                    hasMore = "1".equals(hotThreadJson.getVariables().getNeedmore());
                    if (!ListUtils.isNullOrContainEmpty(articles)) {
                        mSubjects.addAll(articles);

                    }

                    notifyDataSetChanged();
                } else {
                    loadArticleSuccess();
                }
                listener.onSuccess(hasMore);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                listener.onFailed();

                page--;
            }
        });
    }

}
