package com.youzu.clan.main.base.forumnav;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.enums.ForumNavStyle;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.list.RefreshListView;

/**
 * 纵向的，顶部水平滚动一级板块的视图
 */
public class ForumParentFragment extends BaseFragment {

    private ForumnavParentAdapter adapter;
    @ViewInject(R.id.list)
    private RefreshListView mListView;
    @ViewInject(R.id.horizontalTitle)
    private View horizontalTitle;
    @ViewInject(R.id.covering)
    private View covering;
    private FragmentActivity act;
    private boolean isHorizontal = false;
    @ViewInject(R.id.viewPage)
    private ViewPager viewPager;
    @ViewInject(value = R.id.indicator)
    private SlidingTabLayout indicator;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_vertical, container, false);
        ViewUtils.inject(this, view);
        act = getActivity();
        isHorizontal = (ForumNavStyle.TOP.equals(AppSPUtils.getContentConfig(getActivity()).getDisplayStyle()));

        ForumChilrenViewPageAdapter forumViewPageAdapter = new ForumChilrenViewPageAdapter(getChildFragmentManager(), this, isHorizontal);
        viewPager.setAdapter(forumViewPageAdapter);

        adapter = new ForumnavParentAdapter(getActivity(), this, getClanHttpParams(), isHorizontal, horizontalTitle, mListView);
        adapter.setForumViewPageAdapter(forumViewPageAdapter);

        if (isHorizontal) {
            adapter.refresh(null);
            mListView.setVisibility(View.GONE);

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
        } else {
            horizontalTitle.setVisibility(View.GONE);
            mListView.setAdapter(adapter);
        }

        mListView.setDefaultMode(PullToRefreshBase.Mode.DISABLED);
        return view;
    }

    private void isShowCover() {
        int maxScrollX = indicator.getChildAt(0).getMeasuredWidth() - indicator.getMeasuredWidth();
        if (indicator.getScrollX() == maxScrollX) {
            covering.setVisibility(View.GONE);
        } else {
            covering.setVisibility(View.VISIBLE);
        }
    }

    public ForumnavParentAdapter getAdapter() {
        return adapter;
    }

    public SlidingTabLayout getIndicator() {
        return indicator;
    }

    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectPosition(position);
        adapter.notifyDataSetChanged();
        adapter.refreshChildData();
    }

    private ClanHttpParams getClanHttpParams() {
        ClanHttpParams params = new ClanHttpParams(act);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "forumnav");
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        return params;
    }


    @Override
    public void refresh() {
        super.refresh();
    }
}
