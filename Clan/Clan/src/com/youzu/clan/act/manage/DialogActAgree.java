package com.youzu.clan.act.manage;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.youzu.clan.R;

public class DialogActAgree implements View.OnClickListener {
    private Dialog dialog;
    private OnAgreeCallBack mLisener;

    public DialogActAgree(Context context, OnAgreeCallBack lisener) {
        mLisener = lisener;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_act_agree, null);
        dialog = new Dialog(context, R.style.Dialog_act_agree);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = LayoutParams.MATCH_PARENT;
        window.setWindowAnimations(R.style.dialogWindowAnim);

        view.findViewById(R.id.ll_agree).setOnClickListener(this);
        view.findViewById(R.id.ll_refuse).setOnClickListener(this);
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    /***
     * 显示对话框
     */
    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void onDestry() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
        mLisener = null;
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (mLisener == null) {
            return;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (vId == R.id.ll_agree) {
            mLisener.hasAgree(true);
        } else if (vId == R.id.ll_refuse) {
            mLisener.hasAgree(false);
        }
    }

    public interface OnAgreeCallBack {
        void hasAgree(boolean agree);
    }
}
