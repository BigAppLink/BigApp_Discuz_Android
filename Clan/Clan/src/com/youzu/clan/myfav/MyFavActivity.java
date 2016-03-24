package com.youzu.clan.myfav;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.EditableActivity;
import com.youzu.clan.base.EditableFragment;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.SimplePagerAdapter;
import com.youzu.clan.base.widget.list.RefreshListView;

@ContentView(R.layout.activity_myfav)
public class MyFavActivity extends EditableActivity {

    private ViewPager mViewPager;
    private EditableFragment[] mFragments = {new FavThreadFragment(mObserver), new FavForumFragment(mObserver),new FavArticleFragment(mObserver)};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        String[] tabs = getResources().getStringArray(R.array.my_favorites);
        viewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), tabs, mFragments));
        SlidingTabLayout indicator = (SlidingTabLayout) findViewById(R.id.slidingIndicator);
        indicator.setDividerColors(0);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(this));
        indicator.setViewPager(viewPager);

        setCurr(indicator);

        indicator.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                setEditable(false);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager = viewPager;
    }

    private void setCurr(SlidingTabLayout indicator) {
        String[] name = getResources().getStringArray(R.array.my_favorites);

        String getName = null;
        try {
            getName = (String) getIntent().getExtras().get("name");
        } catch (Exception e) {
        }

        if (getName == null || getName.equals(name[0])) {
            indicator.setCurrentItem(0);
        } else {
            indicator.setCurrentItem(1);
        }
    }

    public RefreshListView getListView(int index) {
        return mFragments[index].getListView();
    }

    @Override
    public RefreshListView getListView() {
        int index = mViewPager.getCurrentItem();
        return mFragments[index].getListView();
    }
}
