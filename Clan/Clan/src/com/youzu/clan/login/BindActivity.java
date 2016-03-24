package com.youzu.clan.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kit.app.ActivityManager;
import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.widget.edittext.PasswordEditText;
import com.kit.widget.edittext.WithDelEditText;
import com.kit.widget.spinner.BetterSpinner;
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
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.net.LoginHttp;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;


@ContentView(R.layout.activity_bind)
public class BindActivity extends BaseActivity {

    @ViewInject(R.id.username)
    private WithDelEditText mUsernameEdit;
    @ViewInject(R.id.password)
    private PasswordEditText mPasswordEdit;

    @ViewInject(R.id.question)
    private BetterSpinner spinner;
    @ViewInject(R.id.answer)
    private EditText answer;

    @ViewInject(R.id.llAnswer)
    private View llAnswer;


    @ViewInject(R.id.register)
    private View register;


    private ClanApplication mApplication;
    private LoginQuestionAdapter adapter;
    private String questiongId = "0";

    private String username;
    private String password;
    private String answerStr;

    private String openid;
    private String token;
    private String platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoginView();
        initSpinner();

    }


    @Override
    public boolean getExtra() {

        Bundle b = getIntent().getExtras();
        openid = b.getString(Key.KEY_QQ_LOGIN_OPENID);
        token = b.getString(Key.KEY_QQ_LOGIN_TOKEN);
        platform = b.getString(Key.KEY_THIRD_LOGIN_PLATFORM);

        return super.getExtra();
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


    }

    @OnClick(R.id.bind)
    public void bind(View view) {
        username = mUsernameEdit.getText().toString().trim();
        password = mPasswordEdit.getText().toString().trim();
        answerStr = answer.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, R.string.please_input_account);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, R.string.please_input_password);
            return;
        }

        LoginHttp.platformLoginBind(this, token, openid, platform, username, password, questiongId, answerStr, new JSONCallback() {
            @Override
            public void onstart(Context cxt) {
                LoadingDialogFragment.getInstance(BindActivity.this).show();
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, final String jsonStr) {
                super.onSuccess(ctx, jsonStr);
                ZogUtils.printError(BindActivity.class, "bind:" + jsonStr);
//                ClanHttp.login(BindActivity.this, username, password, questiongId, answerStr, mLoginCallback);

                ClanUtils.dealMsg(BindActivity.this, jsonStr, "login_succeed", R.string.bind_succeed, R.string.login_failed, this, false, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        ProfileJson profileJson = ClanUtils.parseObject(jsonStr, ProfileJson.class);
                        String uid = profileJson.getVariables().getMemberUid();
                        if (StringUtils.isEmptyOrNullOrNullStr(uid)) {
                            uid = profileJson.getData().getUid();
                        }

                        if (profileJson != null && !StringUtils.isEmptyOrNullOrNullStr(uid))
                            onLoginSuccess(profileJson.getData().getUid(), profileJson);
                        else {
                            doFail(baseJson, "login_fail");
                        }
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                });

            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(BindActivity.this, errorCode, errorMsg);
                LoadingDialogFragment.getInstance(BindActivity.this).dismissAllowingStateLoss();

            }
        });
    }


    @OnClick(R.id.register)
    public void register(View view) {

        BundleData bd = new BundleData();
        bd.put(Key.KEY_QQ_LOGIN_OPENID, openid);
        bd.put(Key.KEY_QQ_LOGIN_TOKEN, token);
        bd.put(Key.KEY_THIRD_LOGIN_PLATFORM, platform);

        ClanUtils.gotoRegsiter(this, bd, ResultCode.REQUEST_CODE_REGISTER, false);
    }


    private void onLoginSuccess(String uid, ProfileJson json) {
        LoadingDialogFragment.getInstance(BindActivity.this).dismissAllowingStateLoss();

        AppSPUtils.setLoginInfo(this, true, uid, json.getVariables().getMemberUsername());
        Intent intent = new Intent();
        intent.putExtra(Key.KEY_LOGINED, true);
        intent.putExtra(Key.KEY_LOGIN_RESULT, json);
        setResult(ResultCode.RESULT_CODE_LOGIN, intent);
        ActivityManager.getInstance().popActivity(LoginActivity.class);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null && intent.getBooleanExtra(Key.KEY_LOGINED, false)) {
            setResult(resultCode, intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
