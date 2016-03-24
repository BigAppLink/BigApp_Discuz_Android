package com.youzu.clan.base;

import android.widget.TextView;

import com.kit.utils.ZogUtils;
import com.youzu.clan.base.interfaces.IRefresh;
import com.youzu.clan.base.util.view.MainTopUtils;
import com.youzu.clan.base.util.view.MenuJumpTopUtils;
import com.youzu.clan.main.bottomtab.MenuJumpActivity;

import cn.sharesdk.analysis.MobclickAgent;

public class BaseFragment extends com.kit.ui.BaseFragment implements IRefresh {
    public TextView tvPreDo, tvDo;

    public void onResume() {
        super.onResume();
        String tag = getClass().getName();
        ZogUtils.printLog(getClass(), tag + " start");

        if (getActivity() instanceof MenuJumpActivity) {
            tvPreDo = MenuJumpTopUtils.tvPreDo;
            tvDo = MenuJumpTopUtils.tvDo;
        } else {
            tvPreDo = MainTopUtils.tvPreDo;
            tvDo = MainTopUtils.tvDo;
        }


        MobclickAgent.onPageStart(tag); //统计页面
    }

    public void onPause() {
        super.onPause();
        String tag = getClass().getName();
        ZogUtils.printLog(getClass(), tag + " end");

        MobclickAgent.onPageEnd(tag);
    }

    public void refresh(){
    }
}
