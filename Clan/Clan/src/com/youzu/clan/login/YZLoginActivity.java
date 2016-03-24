package com.youzu.clan.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.YZLoginCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.login.YZLoginParams;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ToastUtils;


@ContentView(R.layout.activity_yz_login)
public class YZLoginActivity extends BaseActivity {

    @ViewInject(value = R.id.username)
    private EditText mUsernameEdit;
    @ViewInject(value = R.id.password)
    private EditText mPasswordEdit;

    private ClanApplication mApplication;
    private YZLoginParams mLoginParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (ClanApplication) getApplication();
        mUsernameEdit.setText(AppSPUtils.getUsername(this));
    }


    @OnClick(R.id.login)
    public void login(View view) {
        final String username = mUsernameEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, R.string.please_input_account);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, R.string.please_input_password);
            return;
        }
        ClanHttp.yzLogin(this, username, password, mYZLoginCallback);
    }


    private void onLoginSuccess(ProfileJson json) {
        ProfileVariables variables = null;
        Space space = null;
        String uid = "";
        String username = "";
        if (json != null &&
                (variables = json.getVariables()) != null &&
                (space = variables.getSpace()) != null) {
            uid = space.getUid();
            username = variables.getMemberUsername();
        }
        AppSPUtils.setLoginInfo(this,true, uid, username);
        Intent intent = new Intent();
        intent.putExtra(Key.KEY_LOGINED, true);
        intent.putExtra(Key.KEY_LOGIN_RESULT, json);
        setResult(ResultCode.RESULT_CODE_LOGIN, intent);
        finish();

    }

    private void inputNickname(YZLoginParams params) {
        mLoginParams = params;
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.input_nickname);
        final EditText editView = (EditText) LayoutInflater.from(this).inflate(R.layout.dialog_nickname_edit, null);
        builder.setView(editView);
        builder.setPositiveButton(R.string.confirm, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (editView.getText().length() < 1) {
                            ToastUtils.show(YZLoginActivity.this, R.string.input_nickname);
                            return;
                        }
                        if (mLoginParams != null) {
                            ClanHttp.setNickname(YZLoginActivity.this, mLoginParams, editView.getText().toString(), mNicknameCallback);
                        }
                    }
                });
            }
        });
        return alertDialog;
    }


    private YZLoginCallback mNicknameCallback = new YZLoginCallback(this) {
        @Override
        public void onSuccess(Context ctx,ProfileJson json) {
            super.onSuccess(ctx,json);
            onLoginSuccess(json);
        }

        @Override
        public void onFailed(Context cxt,int errorCode, String msg) {
            super.onFailed(YZLoginActivity.this,errorCode, msg);
            ToastUtils.show(YZLoginActivity.this, msg);
        }

    };

    private YZLoginCallback mYZLoginCallback = new YZLoginCallback(this) {
        @Override
        public void onSuccess(Context ctx,ProfileJson json) {
            super.onSuccess(ctx,json);
            onLoginSuccess(json);
        }

        @Override
        public void onFailed(YZLoginParams params) {
            super.onFailed(params);
            inputNickname(params);
        }

        @Override
        public void onFailed(Context cxt,int errorCode, String msg) {
            super.onFailed(YZLoginActivity.this,errorCode, msg);
            ToastUtils.show(YZLoginActivity.this, msg);
        }

    };

}
