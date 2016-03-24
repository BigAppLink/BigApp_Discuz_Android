package com.youzu.clan.blog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BlogListJson;
import com.youzu.clan.base.json.blog.BlogCatagory;
import com.youzu.clan.base.json.blog.BlogListVariables;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.EmptyView;

import java.util.ArrayList;

public class BlogAllFragment extends ZBFragment {
    @ViewInject(R.id.horizontalTitle)
    private View horizontalTitle;
    @ViewInject(R.id.covering)
    private View covering;
    @ViewInject(R.id.viewPage)
    private ViewPager viewPager;
    @ViewInject(value = R.id.indicator)
    private SlidingTabLayout indicator;
    @ViewInject(value = R.id.content_view_container)
    private ViewGroup content_view_container;

    private BlogCatalogFragmentPageAdapter mBlogCatalogFragmentPageAdapter;
    private EmptyView mEmptyView;

    private String _order = "dateline";

    public static BlogAllFragment newInstance(String order) {
        BlogAllFragment fragment = new BlogAllFragment();
        Bundle extras = new Bundle();
        extras.putString("order", order);
        fragment.setArguments(extras);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_all, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            _order = getArguments().getString("order");
            if (TextUtils.isEmpty(_order)) {
                _order = "dateline";
            }
        }
        mBlogCatalogFragmentPageAdapter = new BlogCatalogFragmentPageAdapter(getChildFragmentManager(), "all", _order, null);
        viewPager.setAdapter(mBlogCatalogFragmentPageAdapter);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(getActivity()));
        indicator.setDividerColors(0);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isShowCover();
                    }
                }, 100);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        indicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isShowCover();
                return false;
            }
        });

        requestDatas();
    }

    private void isShowCover() {
        int maxScrollX = indicator.getChildAt(0).getMeasuredWidth() - indicator.getMeasuredWidth();
        if (indicator.getScrollX() == maxScrollX) {
            covering.setVisibility(View.GONE);
        } else {
            covering.setVisibility(View.VISIBLE);
        }
    }

    public void requestDatas() {
        mEmptyView = new EmptyView(getActivity());
        mEmptyView.setEmptyViewEnable(true);
        mEmptyView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
        mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        content_view_container.addView(mEmptyView);
        request();
    }

    private void request() {
        mEmptyView.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        horizontalTitle.setVisibility(View.GONE);
        mEmptyView.showLoading();
        ClanHttpParams params = new ClanHttpParams(getActivity());
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("iyzversion", "4");
        params.addQueryStringParameter("module", "myblog");
        params.addQueryStringParameter("action", "list");
        params.addQueryStringParameter("view", "all");
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        BaseHttp.get(Url.DOMAIN, params, new HttpCallback<BlogListJson>() {
            @Override
            public void onSuccess(Context ctx, BlogListJson t) {
                if (t != null) {
                    BlogListVariables variables = t.getVariables();
                    if (variables != null) {
                        ArrayList<BlogCatagory> listCatalog = variables.getCategory();
                        if (listCatalog != null && listCatalog.size() > 0 && mBlogCatalogFragmentPageAdapter != null) {
                            horizontalTitle.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.VISIBLE);
                            if (mEmptyView != null) {
                                mEmptyView.setVisibility(View.GONE);
                            }
                            mBlogCatalogFragmentPageAdapter.setCatagory(listCatalog);
                            indicator.notifyDataSetChanged();
                            return;
                        }
                    }
                }
                if (mEmptyView != null) {
                    mEmptyView.showEmpty();
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                if (mEmptyView != null) {
                    mEmptyView.showError();
                }
            }
        });
    }

}
