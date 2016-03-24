package com.youzu.clan.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kit.utils.FileUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.textview.WithSegmentedControlTextView;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.android.framework.view.annotation.event.OnRadioGroupCheckedChange;
import com.youzu.clan.R;
import com.youzu.clan.app.AboutActivity;
import com.youzu.clan.app.CrashHandler;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.IDClan;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.AppUSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ImageUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.setting.supersetting.SuperSettingCommonFragment;
import com.youzu.clan.setting.supersetting.SuperSettingsActivity;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    public ProfileVariables profileVariables;

    @ViewInject(R.id.gtvClearCache)
    private View gtvClearCache;

    @ViewInject(R.id.gtvSuperSettings)
    private View gtvSuperSettings;


    @ViewInject(R.id.wsctvShowGoToTop)
    private WithSegmentedControlTextView wsctvShowGoToTop;

    @ViewInject(R.id.gtvAbout)
    private View gtvAbout;


    @ViewInject(R.id.logout)
    private View logout;


    private Context context;

    private boolean isUseful;
    private List<Long> clickTimes = new ArrayList<>();
    private int count;
    private int countDownTimes = 5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

    }

    @Override
    public boolean initWidget() {

        if (AppUSPUtils.isZhaoMode(this)) {
            gtvSuperSettings.setVisibility(View.VISIBLE);
        }

        if (!AppSPUtils.isLogined(this)) {
            logout.setVisibility(View.GONE);
        }


        String[] items = getResources().getStringArray(R.array.show_go_to_top_items);
        int[] ids = {IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_LEFT
                , IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_HIDDEN
                , IDClan.ID_RADIOBUTTON_SHOW_GO_TO_TOP_RIGHT};

        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            String s = items[i];
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(ids[i]);
            radioButton.setText(s);
            radioButtons.add(radioButton);
        }

        //设置选中色
        wsctvShowGoToTop.setTintColor(ThemeUtils.getThemeColor(this));
        wsctvShowGoToTop.setSegmentedControl(radioButtons);
        wsctvShowGoToTop.setCheckByRadioButtonID(AppSPUtils.getShowGoToTop(this));

        return super.initWidget();
    }

    @Override
    public boolean getExtra() {

        BundleData bundleData = IntentUtils.getData(getIntent());
        profileVariables = bundleData.getObject("ProfileVariables", ProfileVariables.class);

        return super.getExtra();
    }

    @OnRadioGroupCheckedChange(R.id.wsctvShowGoToTop)
    public void checkedChangeOnShowGoToTop(RadioGroup group, int checkedId) {
        ZogUtils.printError(SuperSettingCommonFragment.class, "checkedId:" + checkedId);
        RadioButton tempButton = (RadioButton) group.findViewById(checkedId);
        ZogUtils.printError(SettingsActivity.class, "tempButton.getText():" + tempButton.getText());
        AppSPUtils.saveShowGoToTop(this, checkedId);
    }


    @OnClick(R.id.gtvClearCache)
    public void clearCache(View view) {
        LoadImageUtils.clearCache(this);
        FileUtils.deleteFile(ImageUtils.getDefaultCacheDir());
        FileUtils.deleteFile(SmileyUtils.getSmileyZipFilePath(this));
        FileUtils.deleteFile(CrashHandler.getLogDir());

        ToastUtils.mkLongTimeToast(this, getString(R.string.clear_cache_success));
    }


    @OnClick(R.id.gtvSuperSettings)
    public void superSettings(View view) {
        IntentUtils.gotoNextActivity(this, SuperSettingsActivity.class);
    }

    @OnClick(R.id.gtvAbout)
    public void about(View view) {
        IntentUtils.gotoNextActivity(this, AboutActivity.class);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unregister_account);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClanUtils.logout(SettingsActivity.this, getResources().getString(R.string.logout_succeed));
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }


    /**
     * 退出登录
     *
     * @param view
     */
    @OnClick(R.id.logout)
    public void logout(View view) {
        showDialog(1);
    }

    @Override
    public boolean initWidgetWithData() {
        return super.initWidgetWithData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AppUSPUtils.isZhaoMode(this)) {
            gtvSuperSettings.setVisibility(View.GONE);
        } else {
            gtvSuperSettings.setVisibility(View.VISIBLE);
        }
    }

}
