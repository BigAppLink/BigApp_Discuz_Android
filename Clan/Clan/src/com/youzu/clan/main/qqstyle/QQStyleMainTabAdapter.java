package com.youzu.clan.main.qqstyle;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.youzu.clan.R;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.main.base.IndexPageFragment;

public class QQStyleMainTabAdapter extends FragmentPagerAdapter {

    private Context context;
    private OnEmptyDataListener listener;

    public QQStyleMainTabAdapter(Context context, FragmentManager fm, OnEmptyDataListener listener) {
        super(fm);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int arg0) {
        return arg0 == 0 ? IndexPageFragment.getInstance(listener) : ClanUtils.getForumNav(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
//		super.getPageTitle(position);
        String[] tabTiles = context.getResources().getStringArray(R.array.main_tab);
        return tabTiles[position];

    }

}
