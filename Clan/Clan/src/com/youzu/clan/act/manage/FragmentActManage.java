package com.youzu.clan.act.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.AppUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.http.HttpCache;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.net.DoAct;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;
import com.youzu.clan.base.widget.list.RefreshListView;

public class FragmentActManage extends ZBFragment implements View.OnClickListener {
    private AdapterActManage mAdapter;
    @ViewInject(R.id.list)
    private RefreshListView mListView;
    @ViewInject(R.id.ll_bottom)
    private View ll_bottom;

    public static FragmentActManage newInstance(Bundle extras) {
        FragmentActManage fragment = new FragmentActManage();
        fragment.setArguments(extras);
        return fragment;
    }

    private String _tid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_act_manage_player_list, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_bottom.findViewById(R.id.ll_agree).setOnClickListener(this);
        ll_bottom.findViewById(R.id.ll_refuse).setOnClickListener(this);
        Bundle extras = getArguments();
        if (extras == null) {
            return;
        }
        ClanHttpParams params = new ClanHttpParams(getActivity());
        _tid = extras.getString("tid");
        String fid = extras.getString("fid");
        String pid = extras.getString("pid");
        params.addQueryStringParameter("fid", fid);
        params.addQueryStringParameter("tid", _tid);
        params.addQueryStringParameter("pid", pid);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("version", "4");
        params.addQueryStringParameter("module", "activityapplylist");
        params.setCacheMode(HttpCache.CACHE_AND_REFRESH);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);
        AdapterActManage adapter = new AdapterActManage(getActivity(), params, _themeColor);
        mListView.setAdapter(adapter);
        mAdapter = adapter;
    }

    public RefreshListView getListView() {
        return mListView;
    }

    @Override
    public void refresh() {
        super.refresh();
        mListView.getRefreshableView().smoothScrollToPosition(0);
        mListView.setRefreshing(true);
        mListView.refresh();
    }

    @Override
    public void onClick(View view) {
        if (mAdapter == null || mAdapter.getActPlayer() == null || mAdapter.getActPlayer().size() < 1) {
            ToastUtils.show(getActivity(), R.string.z_act_manage_toast_refuse_or_pass_null);
            return;
        }
        if (view.getId() == R.id.ll_agree) {
            LoadingDialogFragment.getInstance(getActivity()).show();
            DoAct.passApply(getActivity(), handler, _tid, null, mAdapter.getActPlayer());
        } else if (view.getId() == R.id.ll_refuse) {
            showRefuseDialog();
        }
    }

    private DialogActRefuse mDialogActRefuse;

    private void showRefuseDialog() {
        if (mDialogActRefuse == null) {
            mDialogActRefuse = new DialogActRefuse(getActivity(), new DialogActRefuse.OnRefuseCallBack() {
                @Override
                public void refuse(int type, String msg) {
                    LoadingDialogFragment.getInstance(getActivity()).show();
                    if (type == 1) {
                        //完善资料
                        DoAct.refuseApplyZl(getActivity(), handler, _tid, mDialogActRefuse.getReason(), mAdapter.getActPlayer());
                    } else {
                        //直接打回
                        DoAct.refuseApplyZj(getActivity(), handler, _tid, mDialogActRefuse.getReason(), mAdapter.getActPlayer());
                    }
                }
            });
        }
        mDialogActRefuse.show();
    }

    private String request_success_messagestr = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DoAct.SEND_PASS_APPLY_OK:
                case DoAct.SEND_REFUSE_APPLY_ZJ_OK:
                case DoAct.SEND_REFUSE_APPLY_ZL_OK:
                    LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
                    if (msg != null && msg.obj != null) {
                        request_success_messagestr = (String) msg.obj;
                    }
                    if (TextUtils.isEmpty(request_success_messagestr)) {
                        request_success_messagestr = getString(R.string.z_act_manage_toast_refuse_or_pass_success);
                    }
                    AppUtils.delay(500, new DoSomeThing() {
                        @Override
                        public void execute(Object... objects) {
                            ToastUtils.show(getActivity(), request_success_messagestr);
                            mListView.refresh();
                        }
                    });
                    break;
                case DoAct.SEND_FAIL:
                    String messagestr = "";
                    if (msg != null && msg.obj != null) {
                        messagestr = (String) msg.obj;
                    }
                    sendFail(TextUtils.isEmpty(messagestr)
                            ? getString(R.string.z_act_manage_toast_refuse_or_pass_fail) : messagestr);
                    break;

            }
        }
    };

    private boolean sendFail(String str) {
        LoadingDialogFragment.getInstance(getActivity()).dismissAllowingStateLoss();
        ToastUtils.mkShortTimeToast(getActivity(), str);
        return true;
    }
}
