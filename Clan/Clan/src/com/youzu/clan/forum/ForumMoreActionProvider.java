package com.youzu.clan.forum;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.youzu.clan.R;

public class ForumMoreActionProvider extends ActionProvider {

    private MoreActionProviderCallback callback;

    public ForumMoreActionProvider(Context context) {
        super(context);
    }

    public void setCallback(MoreActionProviderCallback callback) {
        this.callback = callback;

    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add(getContext().getString(R.string.z_thread_publish))
                .setIcon(R.drawable.ic_menu_thread_new)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (callback != null) {
                            callback.doReply();
                        }
                        return true;
                    }
                });
        subMenu.add(getContext().getString(R.string.z_act_publish_title))
                .setIcon(R.drawable.ic_menu_thread_new_act)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (callback != null) {
                            callback.doCreateAct();
                        }
                        return true;
                    }
                });
    }


    @Override
    public boolean hasSubMenu() {
        return true;
    }

    public interface MoreActionProviderCallback {
        void doReply();

        void doCreateAct();
    }
}