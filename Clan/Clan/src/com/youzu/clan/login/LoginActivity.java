package com.youzu.clan.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kit.app.UIHandler;
import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.AppUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.edittext.PasswordEditText;
import com.kit.widget.edittext.WithDelEditText;
import com.kit.widget.spinner.BetterSpinner;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.common.ResultCode;
import com.youzu.clan.base.json.LoginJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.config.content.PlatformLogin;
import com.youzu.clan.base.json.profile.Space;
import com.youzu.clan.base.net.DoLogin;
import com.youzu.clan.base.net.DoProfile;
import com.youzu.clan.base.net.LoginHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

import cn.sharesdk.wechat.friends.Wechat;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements Handler.Callback {

    private static final int GET_PROFILE = 123;

    @ViewInject(R.id.username)
    private WithDelEditText mUsernameEdit;
    @ViewInject(R.id.password)
    private PasswordEditText mPasswordEdit;
    @ViewInject(R.id.yz_login)
    private LinearLayout llYZLogin;

    @ViewInject(R.id.webLogin)
    private LinearLayout webLogin;
    @ViewInject(R.id.question)
    private BetterSpinner spinner;
    @ViewInject(R.id.answer)
    private EditText answer;

    @ViewInject(R.id.llAnswer)
    private View llAnswer;


    @ViewInject(R.id.thirdLogin)
    private View thirdLogin;


    @ViewInject(R.id.qqLogin)
    private View qqLogin;

    @ViewInject(R.id.weiboLogin)
    private View weiboLogin;

    @ViewInject(R.id.wechatLogin)
    private View wechatLogin;


    private ClanApplication mApplication;
    private LoginQuestionAdapter adapter;
    private String questiongId = "0";


    ProfileJson mProfileJson;

    PlatformLogin platformLogin;


    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        LoadingDialogFragment.getInstance(this).dismissAllowingStateLoss();

        switch (what) {
            case LoginPlatformActionListener.MSG_AUTH_CANCEL:
                break;
            case LoginPlatformActionListener.MSG_AUTH_ERROR:
                Toast.makeText(this
                        , (msg.obj != null ? msg.obj.toString() : getString(R.string.login_failed))
                        , Toast.LENGTH_SHORT).show();
                break;
            case LoginPlatformActionListener.MSG_AUTH_COMPLETE:
                DoLogin.platformLogin(this, Wechat.NAME, this, new InjectDo() {
                    @Override
                    public boolean doSuccess(Object baseJson) {
                        UIHandler.sendEmptyMessage(GET_PROFILE, LoginActivity.this);
                        return false;
                    }

                    @Override
                    public boolean doFail(Object baseJson, String tag) {
                        return false;
                    }
                });
                break;
            case GET_PROFILE:
                DoProfile.getProfile(LoginActivity.this, null, new InjectDo() {
                    @Override
                    public boolean doSuccess(Object baseJson) {
                        LoadingDialogFragment.getInstance(LoginActivity.this).dismissAllowingStateLoss();
                        return false;
                    }

                    @Override
                    public boolean doFail(Object baseJson, String tag) {
                        LoadingDialogFragment.getInstance(LoginActivity.this).dismissAllowingStateLoss();
                        return false;
                    }
                });
                break;

        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoginView();
        initSpinner();

    }


    private void initSpinner() {
        final String[] questions = getResources().getStringArray(R.array.loginQuestins);
        adapter = new LoginQuestionAdapter(this, R.layout.item_login_question, questions, new DoSomeThing() {
            @Override
            public void execute(Object... objects) {
                int position = (int) objects[0];
                spinner.setText(questions[position]);
                spinner.dismissDropDown();

                questiongId = position + "";

                if (position == 0) {
                    spinner.setTextColor(getResources().getColor(R.color.text_gray));
                    answer.getText().clear();
                    llAnswer.setVisibility(View.GONE);
                } else {
                    spinner.setTextColor(getResources().getColor(R.color.text_black));
                    llAnswer.setVisibility(View.VISIBLE);
                }
            }
        });
//        ArrayAdapter<String> questionsAdapter = new ArrayAdapter<String>(this,
//                R.layout.item_spinner, questions);

        spinner.setAdapter(adapter);
        spinner.setHint(questions[0]);

    }

    private void initLoginView() {
        mUsernameEdit.setText(AppSPUtils.getUsername(this));
        AppSPUtils.setLoginInfo(this, false, AppSPUtils.getUid(this), AppSPUtils.getUsername(this));

        llYZLogin.setVisibility(View.GONE);

        platformLogin = AppSPUtils.getContentConfig(this).getPlatformLogin();

        if (platformLogin != null && (platformLogin.isQQLogin() || platformLogin.isWeChatLogin() || platformLogin.isWeiboLogin())) {
            thirdLogin.setVisibility(View.VISIBLE);
            if (platformLogin.isQQLogin()) {
                qqLogin.setVisibility(View.VISIBLE);
            }
            if (platformLogin.isWeiboLogin()) {
                weiboLogin.setVisibility(View.VISIBLE);
            }
            if (platformLogin.isWeChatLogin()) {
                wechatLogin.setVisibility(View.VISIBLE);
            }
        } else {
            thirdLogin.setVisibility(View.GONE);
        }

        if (AppSPUtils.getLoginInfo(this).getLoginMod() == 1) {
            webLogin.setVisibility(View.VISIBLE);
        } else {
            webLogin.setVisibility(View.GONE);
        }


    }

    @OnClick(R.id.login)
    public void login(View view) {
        final String username = mUsernameEdit.getText().toString();
        String password = mPasswordEdit.getText().toString();
        String answerStr = answer.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, R.string.please_input_account);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, R.string.please_input_password);
            return;
        }
        AppSPUtils.saveUsername(this, username);

        LoginHttp.login(this, username, password, questiongId, answerStr, mLoginCallback);

//        ClanHttp.login(this, username, password, questiongId, answerStr, mLoginCallback);
    }

//    @OnClick(R.id.yz_login)
//    public void yzLogin(View view) {
//        IntentUtils.gotoNextActivity(this, YZLoginActivity.class, ResultCode.REQUEST_CODE_YZ_LOGIN);
//    }


    @OnClick(R.id.qqLogin)
    public void qqLogin(View view) {

        ClanUtils.gotoThirdLogin(this, platformLogin.getQQLogin(), getString(R.string.qq_login), Activity.RESULT_OK, false);
//        IntentUtils.gotoNextActivity(this, WebActivity.class, ResultCode.REQUEST_CODE_YZ_LOGIN);
    }

    @OnClick(R.id.wechatLogin)
    public void wechatLogin(View view) {
        ClanUtils.gotoPlatformLogin(this, this, new InjectDo() {
            @Override
            public boolean doSuccess(Object baseJson) {
                UIHandler.sendEmptyMessage(GET_PROFILE, LoginActivity.this);
                return false;
            }

            @Override
            public boolean doFail(Object baseJson, String tag) {
                return false;
            }
        });
    }

    @OnClick(R.id.weiboLogin)
    public void weiboLogin(View view) {
    }

    @OnClick(R.id.webLogin)
    public void webLogin(View view) {

        ClanUtils.gotoLogin(this, null, ResultCode.REQUEST_CODE_WEB_LOGIN, false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && intent.getBooleanExtra(Key.KEY_LOGINED, false)) {
            setResult(resultCode, intent);
            finish();
        }
    }


    private JSONCallback mLoginCallback = new JSONCallback() {

        @Override
        public void onstart(Context cxt) {
            super.onstart(cxt);
            LoadingDialogFragment.getInstance(LoginActivity.this).show();
        }

        @Override
        public void onSuccess(Context ctx, final String json) {
            ZogUtils.printError(LoginActivity.class, "mLoginCallback onSuccess");
            super.onSuccess(ctx, json);

            ClanUtils.dealMsg(LoginActivity.this, json, "login_succeed", R.string.login_succeed, R.string.login_failed, this, true, true, new InjectDo() {
                @Override
                public boolean doSuccess(Object baseJson) {
//                    UIHandler.sendEmptyMessage(GET_PROFILE, LoginActivity.this);


                    ZogUtils.printError(LoginActivity.class, "json:::" + json);
                    LoginJson loginJson = JsonUtils.parseObject(json, LoginJson.class);

                    Space space = loginJson.getData();
                    AppSPUtils.setLoginInfo(LoginActivity.this, true, space.getUid(), space.getUsername());
                    setResult(RESULT_OK);
                    LoginActivity.this.finish();
                    return true;
                }

                @Override
                public boolean doFail(Object baseJson, String tag) {
                    return false;
                }
            });


//            DoProfile.getProfile(LoginActivity.this, null);


//            mProfileJson = ClanUtils.parseObject(profileJson, ProfileJson.class);
//            onLoginSuccess(mProfileJson);
        }


        @Override
        public void onFailed(Context cxt, int errorCode, String msg) {
            ZogUtils.printError(LoginActivity.class, "mLoginCallback onFailed");
            super.onFailed(LoginActivity.this, errorCode, msg);
            LoadingDialogFragment.getInstance(LoginActivity.this).dismissAllowingStateLoss();

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppSPUtils.getContentConfig(this).getLoginInfo().getRegSwitch() == 1
                || AppSPUtils.getConfig(this).getLoginInfo().getRegSwitch() == 1) {
            getMenuInflater().inflate(R.menu.menu_login, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                ClanUtils.gotoRegsiter(this, null, Activity.RESULT_OK, false);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
