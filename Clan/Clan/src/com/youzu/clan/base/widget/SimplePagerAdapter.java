package com.youzu.clan.base.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimplePagerAdapter extends FragmentPagerAdapter {

	private String[] mTabs;
	private Fragment[] mFragments;
	
	public SimplePagerAdapter(FragmentManager fm, String[] tabs, Fragment[] fragments) {
		super(fm);
		mTabs = tabs;
		mFragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments[arg0];
	}

	@Override
	public int getCount() {
		return mTabs.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mTabs[position];
	}

}
