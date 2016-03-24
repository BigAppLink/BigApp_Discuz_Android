package com.youzu.clan.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.kit.app.ActivityManager;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.edittext.PasswordEditText;
import com.kit.widget.edittext.WithDelEditText;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.ClanHttp;
import com.youzu.clan.base.net.LoginHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.username)
    WithDelEditText accountEdit;
    @ViewInject(R.id.password)
    PasswordEditText passwordEdit;
    @ViewInject(R.id.confirm_password)
    PasswordEditText confirmPasswordEdit;
    @ViewInject(R.id.email)
    WithDelEditText emailEdit;


    private String openid;
    private String token;
    private String platform;

    private String username;

    ProfileJson profileJson;

    @Override
    public boolean getExtra() {
        BundleData bd = IntentUtils.getData(getIntent());

        if (bd == null) {
            return false;
        }

        openid = bd.getObject(Key.KEY_QQ_LOGIN_OPENID, String.class);
        token = bd.getObject(Key.KEY_QQ_LOGIN_TOKEN, String.class);
        platform = bd.getObject(Key.KEY_THIRD_LOGIN_PLATFORM, String.class);

        return super.getExtra();
    }

    @OnClick(R.id.register)
    public void register(View view) {
        username = accountEdit.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, R.string.please_input_username);
            return;
        }
        final String password = passwordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, R.string.please_input_password);
            return;
        }

        final String confirmPassword = confirmPasswordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPassword)) {
            ToastUtils.show(this, R.string.please_confirm_password);
            return;
        }
        final String email = emailEdit.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show(this, R.string.please_input_email);
            return;
        }
        if (!password.equals(confirmPassword)) {
            ToastUtils.show(this, R.string.inconformity_password);
            return;
        }

        LoadingDialogFragment.getInstance(this).show();
        if (StringUtils.isEmptyOrNullOrNullStr(openid)) {
            //原始注册
            ClanHttp.register(this, username, password, confirmPassword, email, new JSONCallback() {
                        @Override
                        public void onSuccess(Context ctx,String t) {
                            super.onSuccess(ctx,t);
                            afterRegister(t, this);
                        }

                        @Override
                        public void onFailed(Context cxt,int errorCode, String msg) {
                            super.onFailed(RegisterActivity.this,errorCode, msg);
                            LoadingDialogFragment.getInstance(RegisterActivity.this).dismissAllowingStateLoss();

                        }
                    }

            );
        } else {
            //第三方登录，注册并绑定新用户
            LoginHttp.platformNewUser(RegisterActivity.this, token, openid, platform, username, password, email, new JSONCallback() {
                @Override
                public void onSuccess(Context ctx, String jsonStr) {
                    super.onSuccess(ctx, jsonStr);
                    afterRegister(jsonStr, this);

                }

                @Override
                public void onFailed(Context cxt, int errorCode, String errorMsg) {
                    super.onFailed(RegisterActivity.this, errorCode, errorMsg);
                    LoadingDialogFragment.getInstance(RegisterActivity.this).dismissAllowingStateLoss();

                }
            });
        }


    }

    private void afterRegister(String s, HttpCallback callback) {
        ClanUtils.dealMsg(RegisterActivity.this, s, MessageVal.REGISTER_SUCCEED, R.string.register_success, R.string.register_failed, callback, true, true, new InjectDo<BaseJson>() {
            @Override
            public boolean doSuccess(final BaseJson baseJson) {
                getProfile(baseJson);
                return true;
            }

            @Override
            public boolean doFail(BaseJson baseJson, String tag) {
                return true;
            }
        });
    }

    private void getProfile(BaseJson baseJson) {

        ClanHttp.getProfile(RegisterActivity.this, new JSONCallback() {
            @Override
            public void onSuccess(Context ctx,String jsonStr) {
                super.onSuccess(ctx,jsonStr);
                profileJson = ClanUtils.parseObject(jsonStr, ProfileJson.class);
                ProfileVariables variables = profileJson.getVariables();
                if (variables != null) {
                    Space space = variables.getSpace();
                    if (space != null) {
                        onLoginSuccess(profileJson);
                    }
                }
                LoadingDialogFragment.getInstance(RegisterActivity.this).dismissAllowingStateLoss();

            }

            @Override
            public void onFailed(Context cxt,int errorCode, String errorMsg) {
                super.onFailed(RegisterActivity.this,errorCode, errorMsg);
                LoadingDialogFragment.getInstance(RegisterActivity.this).dismissAllowingStateLoss();

            }
        });
    }

    private void onLoginSuccess(ProfileJson profileJson) {
        String uid = profileJson.getVariables().getSpace().getUid();
        AppSPUtils.setLoginInfo(RegisterActivity.this, true, uid, username);
        Intent intent = new Intent();
        intent.putExtra(Key.KEY_LOGINED, true);
        intent.putExtra(Key.KEY_LOGIN_RESULT, profileJson);
        setResult(ResultCode.RESULT_CODE_LOGIN, intent);
        ActivityManager.getInstance().popActivity(LoginActivity.class);
        finish();
    }

}
