package com.youzu.clan.profile.thread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.kit.utils.ZogUtils;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.SimplePagerAdapter;

/**
 * TA的帖子
 *
 * @author wangxi
 */
@ContentView(R.layout.activity_mythread)
public class OwnerThreadActivity extends BaseActivity {
    private ClanApplication mApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        mApplication = (ClanApplication) getApplication();

        Intent intent = getIntent();
        String uid = intent.getStringExtra(Key.KEY_UID);
        String[] tabs;
        if (AppSPUtils.getUid(this).equals(uid)) {
            setTitle(R.string.my_thread);
            tabs = getResources().getStringArray(R.array.my_thread);
        } else {
            setTitle(R.string.other_thread);
            tabs = getResources().getStringArray(R.array.other_thread);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

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
        }

        ZogUtils.printError(MyThreadActivity.class, "getName::::" + getName + " name[0]:" + name[0]);

        if (getName == null || getName.equals(name[0])) {
            indicator.setCurrentItem(0);
        } else {
            indicator.setCurrentItem(1);
        }
    }


}
