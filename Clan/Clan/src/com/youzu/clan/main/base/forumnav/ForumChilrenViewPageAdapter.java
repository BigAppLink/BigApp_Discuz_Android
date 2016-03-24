package com.youzu.clan.main.base.forumnav;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.kit.utils.ZogUtils;
import com.youzu.clan.base.json.forumnav.NavForum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by tangh on 2015/7/30.
 */
public class ForumChilrenViewPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<NavForum> forums;
    private boolean isHorizontal;
    public int type = 2;
    private HashMap<Integer, ForumChildrenFragment> maps = new HashMap<>();
    private boolean isRefresh = true;
    private ForumParentFragment forumParentFragment;


    public ForumChilrenViewPageAdapter(FragmentManager fragmentManager, ForumParentFragment forumParentFragment, boolean isHorizontal) {
        super(fragmentManager);
        this.isHorizontal = isHorizontal;
        this.forumParentFragment = forumParentFragment;
    }


    public void setForums(ArrayList<NavForum> forums) {
        isRefresh = true;
        if (forums != null && this.forums != null) {
            if (forums.size() == this.forums.size()) {
                isRefresh = false;
            }
        }
        this.forums = forums;
    }

    public void setType(int type) {

        this.type = type;
        Set<Integer> set = maps.keySet();
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            refresh(maps.get(i), i);
        }
        if (isRefresh) {
            notifyDataSetChanged();
        }
        ZogUtils.printError(ForumChilrenViewPageAdapter.class, "instantiateItem notifyDataSetChanged  isRefresh=" + isRefresh);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        這個方法會在viewpager生命週期內不停的調用 所以不要在這裏new出來children,new出來children的時候會setAdapter，setAdapter會自動刷新
//        ZogUtils.printError(ForumChilrenViewPageAdapter.class, "instantiateItem position=" + position);

        ForumChildrenFragment fragment = (ForumChildrenFragment) super.instantiateItem(container, position);
        refresh(fragment, position);
        return fragment;
    }

    private void refresh(ForumChildrenFragment fragment, int position) {

        if (type == ForumChildrenFragment.OTHER_TYPE && forums != null && position < forums.size()) {
            if (isHorizontal) {
                fragment.setForums(forums.get(position).getForums());
            } else {
                fragment.setForums(forums);
            }
        }
        fragment.setType(type);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        //  ZogUtils.printError(ForumViewPageAdapter.class,"instantiateItem getItemPosition");
//        return PagerAdapter.POSITION_NONE;
//    }

    @Override
    public Fragment getItem(int position) {
        ForumChildrenFragment fragment = maps.get(position);
        if (fragment == null) {
            fragment = new ForumChildrenFragment();
            fragment.setForumParentFragment(forumParentFragment);
            maps.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (maps.containsKey(position)) {
            maps.remove(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (isHorizontal && forums != null && forums.size() > 0) {
            return forums.get(position).getName();
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
//        ZogUtils.printError(ForumChilrenViewPageAdapter.class, "getCount" );
        if (forums == null || forums.size() == 0) {
            return 1;
        }
        if (isHorizontal) {
            return forums.size();
        }
        return 1;
    }
}
