//package com.youzu.clan.profile.thread;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//
//import com.viewpagerindicator.TabPageIndicator;
//import com.youzu.android.framework.ViewUtils;
//import com.youzu.android.framework.view.annotation.ContentView;
//import com.youzu.clan.app.ClanApplication;
//import com.youzu.clan.R;
//import com.youzu.clan.base.BaseActivity;
//import com.youzu.clan.base.common.Key;
//import com.youzu.clan.base.util.AppSPUtils;
//import com.youzu.clan.base.widget.SimplePagerAdapter;
//
///**
// * TA的帖子
// *
// * @author wangxi
// */
//@ContentView(R.layout.activity_mythread)
//public class OthersThreadActivity extends BaseActivity {
//    private ClanApplication mApplication;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ViewUtils.inject(this);
//
//        mApplication = (ClanApplication) getApplication();
//
//        Intent intent = getIntent();
//        String uid = intent.getStringExtra(Key.KEY_UID);
//        if (AppSPUtils.getUid(this).equals(uid)) {
//            setTitle(R.string.my_thread);
//        }else{
//            setTitle(R.string.other_thread);
//
//        }
//
//        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        String[] tabs = getResources().getStringArray(R.array.other_thread);
//
//        Fragment[] fragments = {new MyThreadFragment(uid), new MyReplyFragment(uid)};
//        viewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager(), tabs, fragments));
//        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//        indicator.setViewPager(viewPager);
//        setCurr(indicator);
//
//    }
//    private void setCurr(TabPageIndicator indicator) {
//        String[] name = getResources().getStringArray(R.array.my_thread);
//
//        String getName = name[0];
//        try {
//            getName = (String) getIntent().getExtras().get("name");
//        } catch (Exception e) {
//        }
////        ZogUtils.printError(MyThreadActivity.class, "getName::::" + getName);
//
//        if (getName.equals(name[0])) {
//            indicator.setCurrentItem(0);
//        } else {
//            indicator.setCurrentItem(1);
//        }
//    }
//
//
//}
