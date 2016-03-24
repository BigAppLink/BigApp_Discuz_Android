package com.youzu.clan.main.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.youzu.clan.base.json.homepageconfig.ViewTabConfig;
import com.youzu.clan.base.json.homepageconfig.NavPage;
import com.youzu.clan.base.util.ClanUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tangh on 2015/9/2.
 *
 * 可变导航的Adapter
 */
public class ChangeableNavPageAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private ViewTabConfig viewTabConfig;
    private ArrayList<NavPage> navPages;

    public ChangeableNavPageAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    private HashMap<Integer, Fragment> maps = new HashMap<>();

    public HashMap<Integer, Fragment> getMaps() {
        return maps;
    }

    public void setViewTabConfig(ViewTabConfig viewTabConfig) {
        this.viewTabConfig = viewTabConfig;
        this.navPages = viewTabConfig.getNavPage();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment baseFragment = ClanUtils.getNavPageFragment(navPages.get(position).getNavSetting());
        maps.put(position, baseFragment);
        return baseFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        maps.remove(position);
    }

    @Override
    public int getCount() {
        if (navPages == null) {
            return 0;
        }
        return navPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return navPages.get(position).getNaviName();
    }
}
