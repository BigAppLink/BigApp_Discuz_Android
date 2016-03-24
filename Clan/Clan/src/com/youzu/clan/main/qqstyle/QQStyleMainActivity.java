package com.youzu.clan.main.qqstyle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kit.utils.ZogUtils;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.json.config.content.SearchSettings;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.SlidingMenuUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.util.view.MainTopUtils;
import com.youzu.clan.base.util.view.ViewDisplayUtils;
import com.youzu.clan.main.base.activity.BaseSlidingActivity;

@ContentView(R.layout.activity_main_qq)
public class QQStyleMainActivity extends BaseSlidingActivity {

    public SharedPreferences mSharedPreferences;

    @ViewInject(R.id.slidingIndicator)
    public SlidingTabLayout slidingIndicator;

    public long exitTime;

    public LinearLayout mTop;
    public ProfileVariables mProfileVariables;

    @ViewInject(R.id.pager)
    public ViewPager viewPager;

    public QQStyleMainTabAdapter qqStyleMainTabAdapter;


    public OnClickListener mAvatarClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getSlidingMenu().showMenu();
        }
    };

    public OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageSelected(int index) {

            ZogUtils.printError(QQStyleMainActivity.class, "index:::::::" + index);
//            getSlidingMenu().setTouchModeAbove(index == 0 ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_MARGIN);
            getSlidingMenu().setTouchModeAbove(index == 0 ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_MARGIN);
        }
    };


    public OnSharedPreferenceChangeListener mPreferenceListener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Key.KEY_NEW_MESSAGE)) {
                final int messageCount = sharedPreferences.getInt(key, 0);
                try {
                    MainTopUtils.mRedDotView.setVisibility(messageCount > 0 ? View.VISIBLE : View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (key.equals(Key.KEY_AVATAR)) {
                final String avatar = sharedPreferences.getString(Key.KEY_AVATAR, "");
                displayAvatar(avatar);
            }
        }
    };

    public OnEmptyDataListener mEmptyListener = new OnEmptyDataListener() {
        public boolean isFirstTime = true;

        @Override
        public void onEmpty() {
            ZogUtils.printError(QQStyleMainActivity.class, "onEmpty onEmpty isFirstTime:" + isFirstTime);
            if (isFirstTime) {
                slidingIndicator.setCurrentItem(1);
                getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                isFirstTime = false;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTopBar();

        slidingIndicator = (SlidingTabLayout) findViewById(R.id.slidingIndicator);
        viewPager = (ViewPager) findViewById(R.id.pager);

        qqStyleMainTabAdapter = new QQStyleMainTabAdapter(this, getSupportFragmentManager(), mEmptyListener);
        viewPager.setAdapter(qqStyleMainTabAdapter);
        slidingIndicator.setDividerColors(0);
        slidingIndicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(this));
        slidingIndicator.setViewPager(viewPager);
        slidingIndicator.setCurrentItem(0);
        slidingIndicator.setOnPageChangeListener(mPageChangeListener);

        initSlidingMenu();

        AppSPUtils.setSPListener(this, mPreferenceListener);

    }


    public boolean getData() {
        Intent intent = getIntent();
        if (intent == null) {
            return false;
        }

        mProfileVariables = (ProfileVariables) intent.getSerializableExtra(Key.KEY_PROFILE);
        if (mProfileVariables == null) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PollingUtils.stopCheckNewMessage(this, mPreferenceListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshAvatar();

        int messageCount = AppSPUtils.getNewMessage(this);
        MainTopUtils.mRedDotView.setVisibility(messageCount > 0 ? View.VISIBLE : View.GONE);
        ViewDisplayUtils.setBadgeCount(this, messageCount);

    }

    public void refreshAvatar() {
        String avatar = AppSPUtils.getAvatartUrl(this);
        displayAvatar(avatar);
    }

    public void displayAvatar(String url) {

        ZogUtils.printLog(QQStyleMainActivity.class, "displayMineAvatar url:" + url + " mApplication.isLogined():" + AppSPUtils.isLogined(this));

        if (!TextUtils.isEmpty(url) && AppSPUtils.isLogined(this)) {
            LoadImageUtils.displayMineAvatar(this, MainTopUtils.mAravarImage, url);
        } else {
            MainTopUtils.mAravarImage.setImageResource(R.drawable.ic_profile_topbar);
        }
    }

    public void initSlidingMenu() {
//        View[] ignoreView = {viewPager};
        SlidingMenuUtils.initSilidingMenu(this, null, false, getResources().getDrawable(R.drawable.bg_settings), true);
    }

    public void setTopBar() {
        mTop = (LinearLayout) findViewById(R.id.top);

        MainTopUtils.setActivityTopbar(this, mTop, R.layout.top_main, getString(R.string.forum_name), null);


        MainTopUtils.mAravarImage.setVisibility(View.VISIBLE);
        SearchSettings searchSettings = AppSPUtils.getContentConfig(this).getSearchSetting();
        if (!(searchSettings != null && !"1".equals(searchSettings.getEnable())))
            MainTopUtils.ibMenu0.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - exitTime > 2000) {
            ToastUtils.show(this, R.string.click_to_exit);
            exitTime = currentTime;
            return;
        }
        this.finish();

    }


}
