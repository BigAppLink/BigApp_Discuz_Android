package com.youzu.clan.blog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.youzu.clan.base.json.blog.BlogCatagory;
import com.youzu.clan.base.json.blog.ReqBlogListParam;

import java.util.ArrayList;

/**
 * Created by tangh on 2015/7/30.
 */
public class BlogCatalogFragmentPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<BlogCatagory> list;
    private String _view, _order, _uid;

    public BlogCatalogFragmentPageAdapter(FragmentManager fragmentManager, String view, String order, String uid) {
        super(fragmentManager);
        _view = view;
        _order = order;
        _uid = uid;
    }

    public void setCatagory(ArrayList<BlogCatagory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        String catid = null;
        String classid = null;
        if (list != null && list.size() > 0 && position < list.size()) {
            if ("all".equals(_view)) {
                //我的日志
                classid = list.get(position).getId();
            } else if ("me".equals(_view)) {
                //日志广场
                catid = list.get(position).getId();
            }
        }
        return BlogContentListFragment.newInstance(new ReqBlogListParam(_view, _order, _uid, catid, classid));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position).getName();
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
