package com.youzu.clan.blog;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.json.config.content.BlogConf;
import com.youzu.clan.base.json.config.content.ContentConfig;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;

public class BlogFragment extends ZBFragment implements View.OnClickListener {
    @ViewInject(value = R.id.rl_top_bar)
    private View rl_top_bar;
    @ViewInject(value = R.id.rl_top_middle)
    private View rl_top_middle;
    @ViewInject(value = R.id.tv_catalog_1)
    private TextView tv_catalog_1;
    @ViewInject(value = R.id.tv_catalog_2)
    private TextView tv_catalog_2;
    @ViewInject(value = R.id.iv_right)
    private View iv_right;

    private int mCurrentTab = 0;
    private BlogConf mBlogConf;

    private View blog_all_fragment_container, blog_we_fragment_container;

    private BlogAllFragment mBlogAllFragment;
    private BlogWeFragment mBlogWeFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blog_all_fragment_container = view.findViewById(R.id.blog_all_fragment_container);
        blog_we_fragment_container = view.findViewById(R.id.blog_we_fragment_container);
        rl_top_bar.setBackgroundColor(_themeColor);
        GradientDrawable _selected_drawable = new GradientDrawable();
        _selected_drawable.setStroke(getResources().getDimensionPixelSize(R.dimen.z_blog_gc_top_stroke_size), _themeColorNon);
        _selected_drawable.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.z_blog_gc_top_stroke_corner));
        _selected_drawable.setShape(GradientDrawable.RECTANGLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rl_top_middle.setBackground(_selected_drawable);
        } else {
            rl_top_middle.setBackgroundDrawable(_selected_drawable);
        }
        tv_catalog_1.setOnClickListener(this);
        tv_catalog_2.setOnClickListener(this);
        ContentConfig config = AppSPUtils.getContentConfig(getActivity());
        if (config != null) {
            mBlogConf = config.getBlogconf();
        }
        if (mBlogAllFragment == null) {
            mBlogAllFragment = BlogAllFragment.newInstance(mBlogConf.getOrder());
            getChildFragmentManager().beginTransaction().replace(R.id.blog_all_fragment_container, mBlogAllFragment, mBlogAllFragment.getClass().getSimpleName()).commit();
        }
        if (AppSPUtils.isLogined(getActivity())) {
            if (mBlogWeFragment == null) {
                mBlogWeFragment = new BlogWeFragment();
                getChildFragmentManager().beginTransaction().replace(R.id.blog_we_fragment_container, mBlogWeFragment, mBlogWeFragment.getClass().getSimpleName()).commit();
            }
        }
        onClick(tv_catalog_1);
    }

    private void setTopTabSelected(int tabId) {
        if (mBlogConf == null || mCurrentTab == tabId) {
            return;
        }
        if (tabId == 1) {
            mCurrentTab = tabId;
            tv_catalog_1.setTextColor(_themeColor);
            tv_catalog_1.setBackgroundColor(_themeColorNon);
            tv_catalog_2.setTextColor(_themeColorNon);
            tv_catalog_2.setBackgroundColor(Color.parseColor("#00000000"));
            blog_all_fragment_container.setVisibility(View.VISIBLE);
            blog_we_fragment_container.setVisibility(View.GONE);
            return;
        }
        if (!AppSPUtils.isLogined(getActivity())) {
            if (mBlogWeFragment != null) {
                getChildFragmentManager().beginTransaction().remove(mBlogWeFragment).commit();
                mBlogWeFragment = null;
            }
            ClanUtils.gotoLogin(getActivity(), null, 1, false);
            return;
        }
        if (mBlogWeFragment == null) {
            mBlogWeFragment = new BlogWeFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.blog_we_fragment_container, mBlogWeFragment, mBlogWeFragment.getClass().getSimpleName()).commit();
        }
        mCurrentTab = tabId;
        tv_catalog_2.setTextColor(_themeColor);
        tv_catalog_2.setBackgroundColor(_themeColorNon);
        tv_catalog_1.setTextColor(_themeColorNon);
        tv_catalog_1.setBackgroundColor(Color.parseColor("#00000000"));
        blog_all_fragment_container.setVisibility(View.GONE);
        blog_we_fragment_container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentTab == 2 && !AppSPUtils.isLogined(getActivity())) {
            onClick(tv_catalog_1);
        }
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.tv_catalog_1) {
            setTopTabSelected(1);
        } else if (vId == R.id.tv_catalog_2) {
            setTopTabSelected(2);
        }
    }
}
