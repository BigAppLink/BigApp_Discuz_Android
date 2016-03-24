package com.youzu.clan.friends;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kit.utils.ToastUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseActivity;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;

/**
 * Created by tangh on 2015/7/15.
 */
@ContentView(R.layout.activity_apply_friends)
public class ApplyFriendsActivity extends BaseActivity {

    @ViewInject(value = R.id.applyEdit)
    private EditText applyEdit;
    private String uid;
    private String userName;
    private String note;
    private String module = FriendsModule.ADD_FRIENDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getIntent().getStringExtra(Key.KEY_UID);
        applyEdit.setText(getString(R.string.i_am_, AppSPUtils.getUsername(this)));
        applyEdit.setSelection(applyEdit.getText().length());
    }

    private ClanHttpParams getClanHttpParams() {
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", module);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("note", note);
        params.addQueryStringParameter("uid", uid);
        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(this));

        return params;
    }

    @OnClick(R.id.delete)
    private void delete(View view) {
        applyEdit.getText().clear();
    }

    private void post() {
        BaseHttp.post(Url.DOMAIN, getClanHttpParams(), new HttpCallback<String>() {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                ClanUtils.dealMsg(ApplyFriendsActivity.this, s, "request_has_been_sent", R.string.send_success, R.string.send_fail, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        setResult(RESULT_OK, getIntent());
                        finish();
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
                super.onFailed(ApplyFriendsActivity.this, errorCode, errorMsg);
                ToastUtils.mkLongTimeToast(ApplyFriendsActivity.this, errorMsg);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apply_friends, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apply_friends:
                note = applyEdit.getText().toString();
                post();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
