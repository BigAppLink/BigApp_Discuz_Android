package com.youzu.clan.setting.supersetting.user_sub;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.kit.utils.ZogUtils;
import com.kit.widget.textview.WithSwitchButtonTextView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnCompoundButtonCheckedChange;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.util.AppUSPUtils;

import java.util.ArrayList;
import java.util.List;


public class SuperSettingSubBottomTabFragment extends BaseFragment {

    public ProfileVariables profileVariables;

    @ViewInject(R.id.wsbtvEnableScrollViewPager)
    private WithSwitchButtonTextView wsbtvEnableScrollViewPager;


    private Context context;

    private boolean isUseful;
    private List<Long> clickTimes = new ArrayList<>();
    private int count;
    private int countDownTimes = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_super_settings_user_sub_bottom_tab, container, false);
        ViewUtils.inject(this, view);

        context = getActivity();

        initWidget();

        return view;
    }


    public boolean initWidget() {

        String[] name = getResources().getStringArray(R.array.type_main);
        ArrayAdapter<String> typeMainAdapter = new ArrayAdapter<String>(context, R.layout.item_spinner, name);


        wsbtvEnableScrollViewPager.setChecked(AppUSPUtils.isEnableScrollViewPager(context));

        return true;
    }


    @OnCompoundButtonCheckedChange(R.id.wsbtvEnableScrollViewPager)
    public void checkedChangeOnEnableScrollViewPager(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingSubBottomTabFragment.class, "saveEnableScrollViewPager:" + isChecked);
        AppUSPUtils.saveEnableScrollViewPager(context, isChecked);
    }

    @OnItemClick(R.id.wstvTypeCheck)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        AppUSPUtils.saveUMainStyle(context, position + 1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }

}
