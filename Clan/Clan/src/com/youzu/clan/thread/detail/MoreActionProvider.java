package com.youzu.clan.thread.detail;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.kit.extend.ui.web.MoreActionWebProvider;
import com.kit.utils.BrowserUtils;
import com.kit.utils.ClipboardUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ToastUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.WebActivity;

public class MoreActionProvider extends ActionProvider {

    private WebActivity activity;
    MoreActionProviderCallback callback;

    private OnMenuItemClickListener mListener;

    public MoreActionProvider(Context context) {
        super(context);
    }

    public void setCallback(WebActivity activity, MoreActionProviderCallback callback) {
        this.activity = activity;
        this.callback = callback;

        ZogUtils.printLog(MoreActionProvider.class, "callback:" + callback);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {


        subMenu.clear();


        if (callback.isHaveShare()) {
            subMenu.add(getContext().getString(R.string.share))
                    .setIcon(R.drawable.ic_share)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            callback.doShare();
                            return true;
                        }
                    });
        }
        if (callback.isHaveFav()) {
            if (callback.isFav()) {
                subMenu.add(getContext().getString(R.string.fav_alright))
                        .setIcon(R.drawable.ic_fav_white_solid)
                        .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                ZogUtils.printLog(MoreActionProvider.class, getContext().getString(R.string.fav_alright));
                                callback.delFav();
                                return true;
                            }
                        });
            } else {
                subMenu.add(getContext().getString(R.string.fav))
                        .setIcon(R.drawable.ic_fav_white)
                        .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                ZogUtils.printLog(MoreActionProvider.class, getContext().getString(R.string.fav));
                                callback.addFav();
                                return true;
                            }
                        });
            }
        }
//        subMenu.add(context.getString(R.string.order))
//                .setIcon(R.drawable.ic_order)
//                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        return true;
//                    }
//                });
        if (callback.isHaveJumpPage()) {
            subMenu.add(getContext().getString(R.string.jump))
                    .setIcon(R.drawable.ic_jump)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            callback.doJumpPage();
                            return true;
                        }
                    });
        }
        if (callback.isHaveJumpPost()) {
            subMenu.add(getContext().getString(R.string.jump_post))
                    .setIcon(R.drawable.ic_jump_post)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            callback.doJumpPost();
                            return true;
                        }
                    });
        }

        if (callback.isHaveDelete()) {
            subMenu.add(getContext().getString(R.string.delete))
                    .setIcon(R.drawable.ic_menu_white_delete)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            callback.delete();
                            return true;
                        }
                    });
        }



        if (callback.isHaveReport()) {
            subMenu.add(getContext().getString(R.string.report))
                    .setIcon(R.drawable.ic_report)
                    .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            callback.report();
                            return true;
                        }
                    });
        }

        subMenu.add(activity.getString(com.kit.extend.R.string.menu_web_open_in_bowser))
                .setIcon(R.drawable.ic_bowser_white)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String url  = callback.getShareUrl();
                        ZogUtils.printError(MoreActionWebProvider.class, "url:" + url);
                        if (!StringUtils.isEmptyOrNullOrNullStr(url))
                            BrowserUtils.gotoBrowser(activity, url);
                        return true;
                    }
                });

        subMenu.add(activity.getString(com.kit.extend.R.string.menu_web_copy_url))
                .setIcon(R.drawable.ic_copy_white)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String url  = callback.getShareUrl();
                        ZogUtils.printError(MoreActionWebProvider.class, "url:" + url);
                        if (!StringUtils.isEmptyOrNullOrNullStr(url)){
                            ClipboardUtils.copy(activity, url);
                            ToastUtils.mkShortTimeToast(activity, activity.getResources().getString(com.kit.extend.R.string.copy_ok_default));
                        }
                        return true;
                    }
                });
    }




    @Override
    public boolean hasSubMenu() {
        return true;
    }

}