package com.youzu.clan.setting.supersetting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.utils.AppUtils;
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
@ContentView(R.layout.activity_super_setting)
public class SuperSettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        String[] tabs = getResources().getStringArray(R.array.super_setting);
        String uid = AppSPUtils.getUid(this);
        Fragment[] fragments = {new SuperSettingCommonFragment(), new SuperSettingUserFragment()};
        viewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), tabs, fragments));




        SlidingTabLayout indicator = (SlidingTabLayout) findViewById(R.id.slidingIndicator);
        indicator.setDividerColors(0);
        indicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(this));
        indicator.setViewPager(viewPager);

        setCurr(indicator);

    }

    private void setCurr(SlidingTabLayout indicator) {
        String[] name = getResources().getStringArray(R.array.super_setting);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_super_settings, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.restart_use_it:
                AppUtils.closeApp(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
