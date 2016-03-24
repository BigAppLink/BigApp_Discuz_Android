package com.youzu.clan.main.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.kit.utils.ZogUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.article.ArticleListFragment;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.main.qqstyle.OnEmptyDataListener;

/**
 * Created by tangh on 2015/9/2.
 */
public class PortalPageFragment extends BaseFragment {

    @ViewInject(R.id.covering)
    private View covering;

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    @ViewInject(value = R.id.indicator)
    private TabPageIndicator indicator;

    private static PortalPageFragment fragment;
    private OnEmptyDataListener mListener;

    PortalPageAdapter adapter;

    public static PortalPageFragment getInstance() {
        if (fragment == null) {
            fragment = new PortalPageFragment();
        }
        return fragment;
    }

    public void setOnEmptyDataListener(OnEmptyDataListener listener) {
        mListener = listener;
    }

    public PullToRefreshAdapterViewBase getListView() {
        Fragment f = adapter.getMaps().get(viewPager.getCurrentItem());
        ZogUtils.printError(PortalPageFragment.class, "viewPager.getCurrentItem():" + viewPager.getCurrentItem() + " f:" + f);
        if (f instanceof IndexPageFragment) {
            return ((IndexPageFragment) f).getListView();
        } else {
            return ((ArticleListFragment) f).getListView();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_father_white_yellow, null);
        ViewUtils.inject(this, view);
        adapter = new PortalPageAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        indicator.setViewPager(viewPager);
        indicator.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        return view;
    }

    @Override
    public void refresh() {
        Fragment f = adapter.getMaps().get(viewPager.getCurrentItem());
        ZogUtils.printError(PortalPageFragment.class, "viewPager.getCurrentItem():" + viewPager.getCurrentItem() + " f:" + f);
        if (f instanceof IndexPageFragment) {
            ((IndexPageFragment) f).getListView().refresh();
        } else {
            ((ArticleListFragment) f).getListView().refresh();
        }
    }
}
