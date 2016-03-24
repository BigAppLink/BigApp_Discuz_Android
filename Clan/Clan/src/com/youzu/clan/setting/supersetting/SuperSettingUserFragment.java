package com.youzu.clan.setting.supersetting;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.kit.utils.ZogUtils;
import com.kit.widget.textview.WithSpinnerTextView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.enums.MainActivityStyle;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.setting.supersetting.user_sub.SuperSettingSubBottomTabFragment;

import java.util.ArrayList;
import java.util.List;


public class SuperSettingUserFragment extends BaseFragment {

    public ProfileVariables profileVariables;
    @ViewInject(R.id.wstvTypeCheck)
    private WithSpinnerTextView wstvTypeCheck;


    @ViewInject(R.id.llEasterEgg)
    private View llEasterEgg;


    private Context context;

    private boolean isUseful;
    private List<Long> clickTimes = new ArrayList<>();
    private int count;
    private int countDownTimes = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_super_settings_user, container, false);
        ViewUtils.inject(this, view);

        context = getActivity();

        initWidget();

        return view;
    }


    public boolean initWidget() {

        String[] name = getResources().getStringArray(R.array.type_main);
        ArrayAdapter<String> typeMainAdapter = new ArrayAdapter<String>(context, R.layout.item_spinner, name);
        int appStyle = AppSPUtils.getConfig(context).getAppStyle();
        int position = (appStyle - 1) < 0 ? 0 : (appStyle - 1);
        wstvTypeCheck.setSpinnerHint(name[position]);
        wstvTypeCheck.setAdapter(typeMainAdapter);
        setSelectedType(position);


        if (AppUSPUtils.isZhaoMode(context)) {
            llEasterEgg.setVisibility(View.VISIBLE);
        } else {
            AppUSPUtils.saveTailStr(context, "");
            AppUSPUtils.saveMadPraiseStr(context, "");
            AppUSPUtils.savePicSizeStr(context, "");
            AppUSPUtils.saveEnableScrollViewPager(context, false);
        }


        return true;
    }

    @OnItemClick(R.id.wstvTypeCheck)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        AppUSPUtils.saveUMainStyle(context, position + 1);
        setSelectedType(position + 1);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    /**
     * 选中主界面风格之后，联动下方fragment变化
     *
     * @param position
     */
    private void setSelectedType(int position) {

        ZogUtils.printError(SuperSettingUserFragment.class, "position:" + position);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        switch (position) {
            case MainActivityStyle.DEFAULT:
            case MainActivityStyle.TAB_BOTTOM:
                transaction.replace(R.id.replace, new SuperSettingSubBottomTabFragment());
                transaction.commit();
                break;
            case MainActivityStyle.SLIDING:
                transaction.replace(R.id.replace, new Fragment());
                transaction.commit();
                break;
            default:
                transaction.replace(R.id.replace, new Fragment());
                transaction.commit();
        }

    }

}
