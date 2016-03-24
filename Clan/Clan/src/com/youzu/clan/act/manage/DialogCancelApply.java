package com.youzu.clan.act.manage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.youzu.clan.R;
import com.youzu.clan.base.net.DoAct;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.LoadingDialogFragment;

/**
 * Created by wjwu on 2015/11/30.
 */
public class DialogCancelApply {

    private FragmentActivity _activity;
    private OnCancelCallback mOnCancelCallback;
    private AlertDialog mAlertDialog;

    public DialogCancelApply(FragmentActivity activity, String fid, String tid, String pid, OnCancelCallback callback) {
        _activity = activity;
        mOnCancelCallback = callback;
        init(fid, tid, pid);
    }

    public void init(final String fid, final String tid, final String pid) {
        final EditText et = new EditText(_activity);
        et.setHint(R.string.z_act_apply_hint_input_cancel_reason);
        mAlertDialog = new AlertDialog.Builder(_activity).setTitle(R.string.z_act_apply_cancel)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LoadingDialogFragment.getInstance(_activity).show();
                        DoAct.cancelApply(_activity, handler, fid, tid, pid, et.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null).create();
    }

    public static interface OnCancelCallback {
        void onCancel(String response);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            Bundle extras = msg.getData();
            String response = null;
            if (extras != null) {
                response = extras.getString("cancel_result");
            }
            switch (msg.what) {
                case DoAct.SEND_ACT_CANCEL_APPLY_OK:
                    String request_success_messagestr = null;
                    LoadingDialogFragment.getInstance(_activity).dismissAllowingStateLoss();
                    if (msg != null && msg.obj != null) {
                        request_success_messagestr = (String) msg.obj;
                    }
                    if (TextUtils.isEmpty(request_success_messagestr)) {
                        request_success_messagestr = _activity.getString(R.string.z_act_manage_toast_refuse_or_pass_success);
                    }
                    ToastUtils.show(_activity, request_success_messagestr);
                    cancel();
                    if (mOnCancelCallback != null) {
                        mOnCancelCallback.onCancel(response);
                    }
                    break;
                case DoAct.SEND_FAIL:
                    String messagestr = "";
                    if (msg != null && msg.obj != null) {
                        messagestr = (String) msg.obj;
                    }
                    LoadingDialogFragment.getInstance(_activity).dismissAllowingStateLoss();
                    ToastUtils.mkShortTimeToast(_activity, TextUtils.isEmpty(messagestr)
                            ? _activity.getString(R.string.z_act_manage_toast_refuse_or_pass_fail) : messagestr);
                    if (mOnCancelCallback != null) {
                        mOnCancelCallback.onCancel(response);
                    }
                    break;
            }
        }
    };

    public void show() {
        if (mAlertDialog != null) {
            mAlertDialog.show();
        }
    }

    public void cancel() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }
}
