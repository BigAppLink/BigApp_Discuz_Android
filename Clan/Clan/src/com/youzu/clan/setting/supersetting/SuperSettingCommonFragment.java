package com.youzu.clan.setting.supersetting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.gc.materialdesign.widgets.ColorSelector;
import com.kit.utils.ZogUtils;
import com.kit.widget.edittext.WithDelEditText;
import com.kit.widget.textview.WithSwitchButtonTextView;
import com.kit.widget.textview.WithTitleTextView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnCompoundButtonCheckedChange;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.InitUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

import java.util.ArrayList;
import java.util.List;


public class SuperSettingCommonFragment extends BaseFragment {

    public ProfileVariables profileVariables;

    @ViewInject(R.id.wsbtvDebug)
    private WithSwitchButtonTextView wsbtvDebug;

    @ViewInject(R.id.wsbtvSuperSetting)
    private WithSwitchButtonTextView wsbtvSuperSetting;


    @ViewInject(R.id.llEasterEgg)
    private View llEasterEgg;

    @ViewInject(R.id.scTail)
    private WithSwitchButtonTextView scTail;

    @ViewInject(R.id.wdetTail)
    private WithDelEditText wdetTail;


    @ViewInject(R.id.wsbtvPraise)
    private WithSwitchButtonTextView wsbtvPraise;

    @ViewInject(R.id.wdetPraise)
    private WithDelEditText wdetPraise;


    @ViewInject(R.id.wsbtvPicSize)
    private WithSwitchButtonTextView wsbtvPicSize;


    @ViewInject(R.id.wsbtvLookPicSize)
    private WithSwitchButtonTextView wsbtvLookPicSize;

    @ViewInject(R.id.wdetPicSize)
    private WithDelEditText wdetPicSize;

    @ViewInject(R.id.wdetLookPicSize)
    private WithDelEditText wdetLookPicSize;

    @ViewInject(R.id.wsbtvShowNewFriendsInChatList)
    private WithSwitchButtonTextView wsbtvShowNewFriendsInChatList;

    @ViewInject(R.id.wttvThemeColorSelector)
    private WithTitleTextView wttvThemeColorSelector;


    @ViewInject(R.id.wsbtvOpenImmersiveMode)
    private WithSwitchButtonTextView wsbtvOpenImmersiveMode;


    @ViewInject(R.id.wsbtvPush)
    private WithSwitchButtonTextView wsbtvPush;


    @ViewInject(R.id.wsbtvShowGroupAndRegisterDate)
    private WithSwitchButtonTextView wsbtvShowGroupAndRegisterDate;



    private Context context;

    private boolean isUseful;
    private List<Long> clickTimes = new ArrayList<>();
    private int count;
    private int countDownTimes = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_super_settings, container, false);
        ViewUtils.inject(this, view);

        context = getActivity();

        initWidget();

        return view;
    }


    public boolean initWidget() {


        if (AppUSPUtils.isZhaoMode(context)) {
            llEasterEgg.setVisibility(View.VISIBLE);
        } else {
            AppUSPUtils.saveTailStr(context, "");
            AppUSPUtils.saveMadPraiseStr(context, "");
            AppUSPUtils.savePicSizeStr(context, "");
            AppUSPUtils.saveEnableScrollViewPager(context, false);
        }

        if (AppUSPUtils.isTail(context)) {
            scTail.setChecked(true);
            wdetTail.setVisibility(View.VISIBLE);
            wdetTail.setText(AppUSPUtils.getTailStr(context));
        } else {
            wdetTail.setVisibility(View.GONE);
        }


        if (AppUSPUtils.isMadPraise(context)) {
            wsbtvPraise.setChecked(true);
            wdetPraise.setVisibility(View.VISIBLE);
            wdetPraise.setText(AppUSPUtils.getMadPraiseStr(context));
        } else {
            wdetPraise.setVisibility(View.GONE);
        }


        if (AppUSPUtils.isPicSize(context)) {
            wsbtvPicSize.setChecked(true);
            wdetPicSize.setVisibility(View.VISIBLE);
            wdetPicSize.setText(AppUSPUtils.getPicSizeStr(context));
        } else {
            wdetPicSize.setVisibility(View.GONE);
        }

        if (AppUSPUtils.isLookPicSize(context)) {
            wsbtvLookPicSize.setChecked(true);
            wdetLookPicSize.setVisibility(View.VISIBLE);
            wdetLookPicSize.setText(AppUSPUtils.getLookPicSizeStr(context));
        } else {
            wdetLookPicSize.setVisibility(View.GONE);
        }

        wsbtvSuperSetting.setChecked(AppUSPUtils.isZhaoMode(context));
        wsbtvDebug.setChecked(AppUSPUtils.isUDebug(context));

        ZogUtils.printError(SuperSettingCommonFragment.class, "bg_title:" + getResources().getColor(R.color.bg_title));

        wttvThemeColorSelector.setContentColor(ThemeUtils.getThemeColor(context));
        wttvThemeColorSelector.getTextViewContent().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorSelector colorSelector = new ColorSelector(context, ThemeUtils.getThemeColor(context), new ColorSelector.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        ZogUtils.printError(SuperSettingCommonFragment.class, "onColorSelected:" + color);
                        ThemeUtils.setThemeColor(context, color);
                        wttvThemeColorSelector.setContentColor(ThemeUtils.getThemeColor(context));
                    }
                });
                colorSelector.show();
            }
        });


        wsbtvShowNewFriendsInChatList.setChecked(AppUSPUtils.isShowNewFriendsInChatList(context));
        wsbtvOpenImmersiveMode.setChecked(AppUSPUtils.isOpenImmersiveMode(context));

        wsbtvPush.setChecked(AppUSPUtils.isUUsePush(context));
        wsbtvShowGroupAndRegisterDate.setChecked(AppUSPUtils.isUShowGroupAndRegisterDate(context));



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            wsbtvOpenImmersiveMode.setVisibility(View.GONE);
//        }

        return true;
    }

    @OnCompoundButtonCheckedChange(R.id.wsbtvDebug)
    public void checkedChangeOnUDebug(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveUDebug:" + isChecked);
        AppUSPUtils.saveUDebug(context, isChecked);


        InitUtils.initEnvironment(getActivity());
    }


    @OnCompoundButtonCheckedChange(R.id.wsbtvSuperSetting)
    public void checkedChangeOnUSuperSetting(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveZhaoMode:" + isChecked);

        if (!isChecked) {
            AppUSPUtils.clear(context);
        }

        AppUSPUtils.saveZhaoMode(context, isChecked);

        InitUtils.initEnvironment(getActivity());
    }

    @OnCompoundButtonCheckedChange(R.id.wsbtvOpenImmersiveMode)
    public void checkedOpenImmersiveMode(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveOpenImmersiveMode:" + isChecked);
        AppUSPUtils.saveOpenImmersiveMode(context, isChecked);
    }




    @OnCompoundButtonCheckedChange(R.id.wsbtvShowNewFriendsInChatList)
    public void checkedChangeOnShowNewFriendsInChatList(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveEnableScrollViewPager:" + isChecked);
        AppUSPUtils.saveShowNewFriendsInChatList(context, isChecked);
    }


    @OnCompoundButtonCheckedChange(R.id.scTail)
    public void checkedChangeOnScTail(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveTail:" + isChecked);
        AppUSPUtils.saveTail(context, isChecked);
        if (isChecked) {
            wdetTail.setVisibility(View.VISIBLE);
        } else {
            wdetTail.setVisibility(View.GONE);
        }
    }

    @OnCompoundButtonCheckedChange(R.id.wsbtvPraise)
    public void checkedChangeOnPraise(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveMadPraise:" + isChecked);
        AppUSPUtils.saveMadPraise(context, isChecked);
        if (isChecked) {
            wdetPraise.setVisibility(View.VISIBLE);
        } else {
            wdetPraise.setVisibility(View.GONE);
        }
    }

    @OnCompoundButtonCheckedChange(R.id.wsbtvPicSize)
    public void checkedChangeOnPicSize(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "savePicSize:" + isChecked);
        AppUSPUtils.savePicSize(context, isChecked);
        if (isChecked) {
            wdetPicSize.setVisibility(View.VISIBLE);
        } else {
            wdetPicSize.setVisibility(View.GONE);
        }
    }


    @OnCompoundButtonCheckedChange(R.id.wsbtvLookPicSize)
    public void checkedChangeOnLookPicSize(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveLookPicSize:" + isChecked);
        AppUSPUtils.saveLookPicSize(context, isChecked);
        if (isChecked) {
            wdetLookPicSize.setVisibility(View.VISIBLE);
        } else {
            wdetLookPicSize.setVisibility(View.GONE);
        }
    }


    @OnCompoundButtonCheckedChange(R.id.wsbtvPush)
    public void checkedChangeOnUsePush(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveUUsePush:" + isChecked);
        AppUSPUtils.saveUUsePush(context, isChecked);
    }




    @OnCompoundButtonCheckedChange(R.id.wsbtvShowGroupAndRegisterDate)
    public void checkedChangeOnShowGroupAndRegisterDate(CompoundButton buttonView, boolean isChecked) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "saveUShowGroupAndRegisterDate:" + isChecked);
        AppUSPUtils.saveUShowGroupAndRegisterDate(context, isChecked);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (scTail.isChecked()) {
            AppUSPUtils.saveTailStr(context, wdetTail.getText().toString());
        } else {
            AppUSPUtils.saveTailStr(context, "");
        }

        if (wsbtvPraise.isChecked()) {
            AppUSPUtils.saveMadPraiseStr(context, wdetPraise.getText().toString());
        } else {
            AppUSPUtils.saveMadPraiseStr(context, "");
        }

        if (wsbtvPicSize.isChecked()) {
            AppUSPUtils.savePicSizeStr(context, wdetPicSize.getText().toString());
        } else {
            AppUSPUtils.savePicSizeStr(context, "");
        }

        if (wsbtvLookPicSize.isChecked()) {
            AppUSPUtils.saveLookPicSizeStr(context, wdetLookPicSize.getText().toString());
        } else {
            AppUSPUtils.saveLookPicSizeStr(context, "");
        }
//
    }

}
