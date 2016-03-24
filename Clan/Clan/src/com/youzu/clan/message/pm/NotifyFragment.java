package com.youzu.clan.message.pm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.EditableFragment;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.jump.JumpChatUtils;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.friends.NewFriendsActivity;

import java.util.ArrayList;

public class NotifyFragment extends EditableFragment implements OnEditListener {

    @ViewInject(value = R.id.list)
    RefreshListView mListView;


    private MyPMAdatper mAdapter;

    private static NotifyFragment fragment;
    private OnDataSetChangedObserver mObserver;


    public NotifyFragment() {
    }

    public void setObserver(OnDataSetChangedObserver mObserver) {
        this.mObserver = mObserver;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
//        RefreshListView listView = (RefreshListView) view.findViewById(R.id.list);
        ViewUtils.inject(this, view);


        ClanHttpParams params = new ClanHttpParams(getActivity());
        params.addQueryStringParameter("module", "notifyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        mAdapter = new MyPMAdatper(getActivity(), this, params);
        mAdapter.setOnDataSetChangedObserver(mObserver);
        mListView.setAdapter(mAdapter);
        mListView.setOnEditListener(this);
        AppSPUtils.saveNewMessage(getActivity(), 0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


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
            JumpChatUtils.gotoChat(getActivity(), mypm);
        }
    }


    @OnClick(R.id.newFriends)
    protected void newFriends(View view) {
        IntentUtils.gotoNextActivity(getActivity(), NewFriendsActivity.class);
    }

    /**
     * 为长按单个删除准备 已经无用了
     *
     * @param id
     * @param adapter
     * @param position
     */
    @Deprecated
    private void showDialog(int id, final BaseRefreshAdapter adapter, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.is_delete);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mypm mypm = (Mypm) mAdapter.getItem(position - 1);

                doDelete(mypm.getTouid(), mAdapter, position);

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        Dialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDelete() {

        ZogUtils.printLog(MyPMFragment.class, "mListView.getCheckedItemCount():" + mListView.getCheckedItemCount());
        if (mListView.getCheckedItemCount() < 1) {
            return;
        }
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
        doDelete(sb.toString(), mAdapter, null);
    }


    /**
     * 当传入position,adapter的时候为单个长按删除
     *
     * @param str
     * @param adapter
     * @param position
     */
    private void doDelete(String str, final BaseRefreshAdapter adapter, final Integer position) {

        ClanHttpParams params = new ClanHttpParams(getActivity());
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "deletepl");

        params.addBodyParameter("deletepm_deluid", str);
        params.addBodyParameter("deletepmsubmit_btn", "true");
        params.addBodyParameter("deletesubmit", "true");

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(getActivity())))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(getActivity()));

        BaseHttp.post(Url.DOMAIN, params, new StringCallback(getActivity()) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
//                BaseJson t = ClanUtils.parseObject(s, BaseJson.class);
                ClanUtils.dealMsg(getActivity(), s, "delete_pm_success", R.string.delete_success, R.string.delete_failed, this, true, true, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        mListView.deleteChoices();
                        if (adapter != null && position != null) {
                            ArrayList list = adapter.getData();
                            list.remove(position);
                            adapter.setData(list);
                        }
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String msgVal) {
                        return true;
                    }
                });
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(getActivity(), errorCode, msg);

            }
        });
    }

    public MyPMAdatper getAdapter() {
        return mAdapter;
    }


}
