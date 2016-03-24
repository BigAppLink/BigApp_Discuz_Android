package com.youzu.clan.common;

import com.kit.extend.ui.web.CookieKit;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.interfaces.IRefresh;

import cn.sharesdk.analysis.MobclickAgent;

public class WebFragment extends com.kit.extend.ui.web.WebFragment implements CookieKit, IRefresh {

    public void onResume() {
        super.onResume();
        String tag = getClass().getName();
        ZogUtils.printLog(getClass(), tag + " start");

        MobclickAgent.onPageStart(tag); //统计页面
    }

    public void onPause() {
        super.onPause();
        String tag = getClass().getName();
        ZogUtils.printLog(getClass(), tag + " end");

        MobclickAgent.onPageEnd(tag);
    }


    public void refresh() {
    }



}
