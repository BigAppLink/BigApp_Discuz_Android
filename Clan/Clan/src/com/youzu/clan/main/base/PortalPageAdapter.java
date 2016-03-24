package com.youzu.clan.main.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.youzu.clan.article.ArticleListFragment;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.config.content.ContentConfig;
import com.youzu.clan.base.json.config.content.ThreadConfigItem;
import com.youzu.clan.base.util.AppSPUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tangh on 2015/9/2.
 */
public class PortalPageAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private  ArrayList<ThreadConfigItem> threadConfigItems;
    public PortalPageAdapter(FragmentManager fragmentManager,Context context) {
        super(fragmentManager);
        this.context=context;
        initData();
    }
    private HashMap<Integer,BaseFragment> maps=new HashMap<>();
    private void initData(){
       ContentConfig contentConfig= AppSPUtils.getContentConfig(context);
        if(contentConfig!=null){
            threadConfigItems= contentConfig.getPortalconfig();
        }
    }

    public HashMap<Integer, BaseFragment> getMaps() {
        return maps;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment baseFragment=null;
        if(position==0){
            baseFragment= new IndexPageFragment(null);
        }else{
            ArticleListFragment articleListFragment= new ArticleListFragment();
            articleListFragment.setCatId(threadConfigItems.get(position).getId());
            baseFragment=  articleListFragment;
        }
        maps.put(position,baseFragment);
        return baseFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        maps.remove(position);
    }

    @Override
    public int getCount() {
        if(threadConfigItems==null){
            return 0;
        }
        return threadConfigItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return threadConfigItems.get(position).getTitle();
    }
}
