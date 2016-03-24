package com.youzu.clan.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.utils.ZZLogUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

/**
 * Created by wjwu on 2015/11/24.
 */
public class ZBFragment extends BaseFragment {
    protected int _themeColor;
    protected int _themeColorNon;
    public ZBCallBack mZBCallBack;

    protected void log(String msg) {
        ZZLogUtils.log(getTag() + "", msg);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mZBCallBack = (ZBCallBack) activity;
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mZBCallBack = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
        _themeColor = ThemeUtils.getThemeColor(getActivity());
        _themeColorNon = Color.WHITE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
