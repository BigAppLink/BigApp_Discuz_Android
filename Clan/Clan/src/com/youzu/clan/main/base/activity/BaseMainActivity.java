package com.youzu.clan.main.base.activity;

import android.os.Bundle;

import com.kit.utils.DialogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.config.BuildType;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.net.LoadEmojiUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;

/**
 * Created by Zhao on 15/9/15.
 */
public class BaseMainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtils.printAppStyle(this);

        LoadEmojiUtils.startLoadSmiley(this);

        showDialog();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void showDialog() {
        if (getString(R.string.build_type).equals(BuildType.TEST)) {
            DialogUtils.showDialog(this, getString(R.string.tips), getString(R.string.notice_test_build), getString(R.string.confirm), getString(R.string.cancel), true, true, null, null);
        }
    }







}
