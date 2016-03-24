package com.youzu.clan.main.wechatstyle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wuyexiong on 4/25/15.
 */
public class WeChatStyleFragmentAdapter extends FragmentPagerAdapter {
//
//    protected static final String[] CONTENT = new String[]{"This", "Is", "Msg", "Mine",};
//    private int mCount = CONTENT.length;

    private FragmentManager fm;

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public WeChatStyleFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {

//        if(position==0){
//            return HotThreadFragment.getInstance();
//        }else if(position==1){
//            return ForumnavFragment.getInstance();
//        }else if(position ==2){
//            return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
//
//        }else if(position ==3){
//            return MyHomePageFragment.newInstance();
//
//        }
//        return TestFragment.newInstance(CONTENT[position % CONTENT.length]);

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        if (fragments == null)
            return 0;

        return fragments.size();
    }

}
