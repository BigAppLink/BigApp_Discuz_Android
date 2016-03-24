package com.youzu.clan.blog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.json.blog.BlogInfo;
import com.youzu.clan.base.json.blog.ReqBlogListParam;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.widget.list.RefreshListView;

public class BlogContentListFragment extends ZBFragment {

    @ViewInject(R.id.list)
    private RefreshListView mListView;

    public static BlogContentListFragment newInstance(ReqBlogListParam reqParam) {
        BlogContentListFragment fragment = new BlogContentListFragment();
        if (reqParam != null) {
            Bundle extras = new Bundle();
            extras.putSerializable("reqParam", reqParam);
            fragment.setArguments(extras);
        }
        return fragment;
    }

    private ClanHttpParams getClanHttpParams() {
        if (getArguments() == null) {
            return new ClanHttpParams(getActivity());
        }
        ReqBlogListParam reqParam = (ReqBlogListParam) getArguments().getSerializable("reqParam");
        if (reqParam == null) {
            return new ClanHttpParams(getActivity());
        }
        ClanHttpParams params = new ClanHttpParams(getActivity());
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("iyzversion", "4");
        params.addQueryStringParameter("module", "myblog");
        params.addQueryStringParameter("action", "list");
        params.addQueryStringParameter("view", reqParam.view);
        if (!TextUtils.isEmpty(reqParam.catid)) {
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
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        return params;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (RefreshListView) inflater.inflate(R.layout.fragment_blog_content_list, container, false);
        ViewUtils.inject(this, mListView);
        return mListView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BlogContentListAdapter adapter = new BlogContentListAdapter(getActivity(), getClanHttpParams());
        mListView.setAdapter(adapter);
    }

    @Override
    public void refresh() {
        super.refresh();
        mListView.getRefreshableView().smoothScrollToPosition(0);
        mListView.setRefreshing(true);
        mListView.refresh();
    }

    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == null || parent.getAdapter() == null) {
            return;
        }
        BlogInfo blogInfo = (BlogInfo) parent.getAdapter().getItem(position);
        //TODO
//        JumpForumUtils.gotoForum(getActivity(), forum);
    }
}
