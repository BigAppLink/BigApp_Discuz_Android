package com.youzu.clan.main.android;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.slidingtab.SlidingTabLayout;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.json.config.content.SearchSettings;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.SuperViewPager;
import com.youzu.clan.main.base.activity.BaseMainActivity;
import com.youzu.clan.main.qqstyle.OnEmptyDataListener;
import com.youzu.clan.profile.MenuFragment;
import com.youzu.clan.search.SearchActivity;
import com.youzu.clan.thread.ThreadPublishActivity;

@ContentView(R.layout.activity_pure_android_main)
public class PureAndroidMainActivity extends BaseMainActivity implements OnClickListener {

    private boolean direction;
    private MaterialMenuView materialMenu;
    private int actionBarMenuState = MaterialMenuDrawable.IconState.BURGER.ordinal();



    @ViewInject(R.id.slidingIndicator)
    private SlidingTabLayout slidingIndicator;

    private long exitTime;


    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @ViewInject(R.id.pager)
    private SuperViewPager viewPager;

    @ViewInject(R.id.llMenu)
    private LinearLayout llMenu;


    private OnEmptyDataListener mEmptyListener = new OnEmptyDataListener() {
        private boolean isFirstTime = true;

        @Override
        public void onEmpty() {
            if (isFirstTime) {
                slidingIndicator.setCurrentItem(1);
//                getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                isFirstTime = false;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.PureAndroidActionBarTheme);

//        setActionBar();

        ClanUtils.initCustomActionBar(this);
        materialMenu = (MaterialMenuView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_menu);
        materialMenu.setOnClickListener(this);

        setDrawerLayout();


        viewPager.setAdapter(new PureAndroidTabAdapter(this, getSupportFragmentManager(), mEmptyListener));
        slidingIndicator.setDividerColors(0);
        slidingIndicator.setSelectedIndicatorColors(ThemeUtils.getThemeColor(this));
        slidingIndicator.setViewPager(viewPager);
        slidingIndicator.setCurrentItem(0);

//        去除头部区域滑动显示侧边栏
//        getSlidingMenu().addIgnoredView(mIndicator);

        getSupportFragmentManager().beginTransaction().replace(R.id.menuReplace, new MenuFragment()).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PollingUtils.stopCheckNewMessage(this, mPreferenceListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String avatar = AppSPUtils.getAvatartUrl(this);
//        displayMineAvatar(avatar);

//        int messageCount = AppSPUtils.getNewMessage(this);
//        mRedDotView.setVisibility(messageCount > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick(R.id.action_bar_menu)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_bar_menu) {
            // random state on click
            if (actionBarMenuState == MaterialMenuDrawable.IconState.BURGER.ordinal()) {
                int state = MaterialMenuDrawable.IconState.ARROW.ordinal();
                materialMenu.animatePressedState(intToState(state));
                drawerLayout.openDrawer(Gravity.START);
                actionBarMenuState = state;
            } else {
                int state = MaterialMenuDrawable.IconState.BURGER.ordinal();
                materialMenu.animatePressedState(intToState(state));
                drawerLayout.closeDrawers();
                actionBarMenuState = state;

            }
        }
    }


    public void setDrawerLayout() {
        drawerLayout.setScrimColor(Color.parseColor("#66000000"));
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        direction ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                direction = true;
            }

            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                direction = false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pure_android, menu);
        SearchSettings searchSettings =AppSPUtils.getContentConfig(this).getSearchSetting();
        if (!(searchSettings!=null&&!"1".equals(searchSettings.getEnable()))){
            menu.findItem(R.id.action_search).setVisible(true);
        }else{
            menu.findItem(R.id.action_search).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                IntentUtils.gotoNextActivity(this, ThreadPublishActivity.class);
                return true;

            case R.id.action_search:
                IntentUtils.gotoNextActivity(this, SearchActivity.class);
                return true;
        }


        return super.onOptionsItemSelected(item);
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


    public static MaterialMenuDrawable.IconState intToState(int state) {
        switch (state) {
            case 0:
                return MaterialMenuDrawable.IconState.BURGER;
            case 1:
                return MaterialMenuDrawable.IconState.ARROW;
            case 2:
                return MaterialMenuDrawable.IconState.X;
            case 3:
                return MaterialMenuDrawable.IconState.CHECK;
        }
        throw new IllegalArgumentException("Must be a number [0,3)");
    }

}
