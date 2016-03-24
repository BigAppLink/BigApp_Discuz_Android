package com.youzu.clan.blog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.blog.BlogInfo;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.view.ProfileUtils;
import com.youzu.clan.base.widget.list.RefreshListView;

public class BlogWeFragment extends ZBFragment {

    @ViewInject(R.id.list)
    private RefreshListView mListView;

    private ClanHttpParams getClanHttpParams() {
        ClanHttpParams params = new ClanHttpParams(getActivity());
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("iyzversion", "4");
        params.addQueryStringParameter("module", "myblog");
        params.addQueryStringParameter("action", "list");
        params.addQueryStringParameter("view", "we");
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
        addHeadView();
        BlogContentListAdapter adapter = new BlogContentListAdapter(getActivity(), getClanHttpParams());
        mListView.setAdapter(adapter);
    }

    private ImageView iv_user;
    private TextView tv_city, tv_time;

    private void addHeadView() {
        View headView = LayoutInflater.from(mContext).inflate(R.layout.list_head_blog_we, null);
        iv_user = (ImageView) headView.findViewById(R.id.iv_user);
        tv_city = (TextView) headView.findViewById(R.id.tv_city);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        mListView.getRefreshableView().addHeaderView(headView);
        ProfileJson profileJson = ProfileUtils.getProfile(getActivity());
        if (profileJson != null && profileJson.getVariables() != null && profileJson.getVariables().getSpace() != null) {
            updateHeadView(profileJson.getVariables().getSpace());
        } else {
            updateProfile();
        }
    }

    private void updateHeadView(Space space) {
        log("wenjun updateHeadView space = " + (space == null ? null : JsonUtils.toJSONString(space)));
        if (space == null) {
            return;
        }
        LoadImageUtils.displayMineAvatar(getActivity(), iv_user, space.getAvatar());
//            LoadImageUtils.displayNoHolder(mContext, iv_user,  space.getAvatar()
//                    , R.drawable.ic_profile_nologin_default);
        //TODO
        tv_city.setText("shanghai");
        tv_time.setText("上午，12");
    }

    /**
     * 更新用户信息
     */
    private void updateProfile() {

        ClanHttp.getProfile(getActivity(), "", new HttpCallback<ProfileJson>() {

            @Override
            public void onSuccess(Context ctx, ProfileJson t) {
                if (t != null && t.getVariables() != null && t.getVariables().getSpace() != null) {
                    updateHeadView(t.getVariables().getSpace());
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
            }
        });
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
