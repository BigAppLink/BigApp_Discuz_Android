package com.youzu.clan.act.manage;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.youzu.clan.R;

public class DialogActRefuse implements View.OnClickListener {
    private Dialog dialog;
    private OnRefuseCallBack mLisener;
    private EditText mEt_content;

    public DialogActRefuse(Context context, OnRefuseCallBack lisener) {
        mLisener = lisener;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_act_refuse, null);
        dialog = new Dialog(context, R.style.Dialog_act_agree);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = LayoutParams.MATCH_PARENT;

        view.findViewById(R.id.tv_refuse_zl).setOnClickListener(this);
        view.findViewById(R.id.tv_refuse_zj).setOnClickListener(this);
        view.findViewById(R.id.tv_refuse_cancel).setOnClickListener(this);
        mEt_content = (EditText) view.findViewById(R.id.et_content);
    }

    public String getReason() {
        if (mEt_content != null) {
            return mEt_content.getText().toString();
        }
        return null;
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
        if (vId == R.id.tv_refuse_zl) {
            mLisener.refuse(1, mEt_content.getText().toString());
        } else if (vId == R.id.tv_refuse_zj) {
            mLisener.refuse(2, mEt_content.getText().toString());
        }
    }

    public interface OnRefuseCallBack {
        /***
         * @param type 1-打回完善资料，2-直接拒绝
         * @param msg  拒绝的理由
         */
        void refuse(int type, String msg);
    }
}
