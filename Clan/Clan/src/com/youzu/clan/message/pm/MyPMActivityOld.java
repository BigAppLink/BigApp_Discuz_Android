package com.youzu.clan.message.pm;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;

import com.kit.utils.ZogUtils;
import com.youzu.android.framework.view.annotation.ContentView;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.EditableActivity;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpChatUtils;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.RefreshListView;

/**
 * 消息
 *
 * @author wangxi
 */
@Deprecated
@ContentView(R.layout.activity_mypm)
public class MyPMActivityOld extends EditableActivity implements OnEditListener {

    @ViewInject(value = R.id.list)
    RefreshListView mListView;

    private MyPMAdatper mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("module", "mypm");
        mAdapter = new MyPMAdatper(this, params);
        mListView.setAdapter(mAdapter);
        mListView.setOnEditListener(this);
        mAdapter.setOnDataSetChangedObserver(mObserver);
        AppSPUtils.saveNewMessage(this, 0);
    }

    @Override
    public RefreshListView getListView() {
        return mListView;
    }

    @OnItemClick(R.id.list)
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Mypm mypm = (Mypm) mAdapter.getItem(position);
        ZogUtils.printLog(MyPMFragment.class, "mypm.getTousername():" + mypm.getTousername());

        if (mypm != null) {
            JumpChatUtils.gotoChat(this, mypm);
        }
    }

    @Override
    public void onDelete() {
        if (mListView.getCheckedItemCount() < 1) {
            return;
        }
        ClanHttpParams params = new ClanHttpParams(this);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "deletepl");

        int headerCount = mListView.getRefreshableView().getHeaderViewsCount();
        SparseBooleanArray array = mListView.getChoicePostions();
        int count = mAdapter.getCount();
        StringBuffer sb = new StringBuffer();
        for (int i = headerCount; i < count + headerCount; i++) {
            if (array.get(i)) {
                Mypm mypm = (Mypm) mAdapter.getItem(i - headerCount);
                if (mypm != null && !TextUtils.isEmpty(mypm.getTouid())) {
                    sb.append(mypm.getTouid()).append("_");
                }
            }
        }
        params.addBodyParameter("deletepm_deluid", sb.toString());
        params.addBodyParameter("deletepmsubmit_btn", "true");
        params.addBodyParameter("deletesubmit", "true");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(this)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(this));

        BaseHttp.post(Url.DOMAIN, params, new ProgressCallback<BaseJson>(this) {
            @Override
            public void onSuccess(Context ctx, BaseJson t) {
                super.onSuccess(ctx,t);
                if (t != null && t.getMessage() != null) {
                    Message message = t.getMessage();
                    if ("delete_pm_success".equals(message.getMessageval())) {
                        ToastUtils.show(getApplicationContext(), R.string.delete_success);
                        mListView.deleteChoices();
                        return;
                    }
                }
                onFailed(ctx,-1, "");
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(MyPMActivityOld.this,errorCode, msg);
                ToastUtils.show(getApplicationContext(), R.string.delete_failed);
            }
        });
    }

}
