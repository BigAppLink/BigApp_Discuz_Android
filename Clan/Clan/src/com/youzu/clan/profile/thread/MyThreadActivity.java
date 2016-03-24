package com.youzu.clan.profile.thread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.SimplePagerAdapter;

/**
 * 我的帖子
 *
 * @author wangxi
 */
@ContentView(R.layout.activity_mythread)
public class MyThreadActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        setTitle(R.string.my_thread);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        String[] tabs = getResources().getStringArray(R.array.my_thread);
        String uid = AppSPUtils.getUid(this);
        Fragment[] fragments = {new MyThreadFragment(uid), new MyReplyFragment(uid)};
        viewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), tabs, fragments));
        SlidingTabLayout indicator = (SlidingTabLayout) findViewById(R.id.slidingIndicator);
        indicator.setDividerColors(0);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(this));
        indicator.setViewPager(viewPager);

        setCurr(indicator);

    }

    private void setCurr(SlidingTabLayout indicator) {
        String[] name = getResources().getStringArray(R.array.my_thread);

        String getName = name[0];
        try {
            getName = (String) getIntent().getExtras().get("name");
        } catch (Exception e) {
            getName = name[0];
        }
//        ZogUtils.printError(MyThreadActivity.class, "getName::::" + getName);

        if (getName == null || getName.equals(name[0])) {
            indicator.setCurrentItem(0);
        } else {
            indicator.setCurrentItem(1);
        }
    }

}
