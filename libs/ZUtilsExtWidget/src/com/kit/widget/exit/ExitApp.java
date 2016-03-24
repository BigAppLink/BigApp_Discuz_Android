package com.kit.widget.exit;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kit.extend.widget.R;
import com.kit.utils.ToastUtils;
import com.kit.widget.dialog.DefaultDialog;

import java.util.Timer;
import java.util.TimerTask;

public class ExitApp {

    public static final int EXIT_TYPE_DOUBLE_CLICK = 1;
    public static final int EXIT_TYPE_DIALOG = 2;
    /**
     * 双击退出函数
     */
    public static Boolean isExit = false;
    private boolean isExitSys;

    public static void exitByDoubleClick(Context mContext, String msg, final boolean isExitSys) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出

            ToastUtils.mkShortTimeToast(mContext, msg);
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ((Activity) mContext).finish();

            if (isExitSys)
                System.exit(0);
        }
    }

    public static void showExitDialog(final Context mContext, String msg, final boolean isExitSys) {
        // 截获按键事件
        final DefaultDialog mDialog = new DefaultDialog(mContext, msg,
                R.layout.dialog_default, true);

        mDialog.show();

        mDialog.mButtonOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ((Activity) mContext).finish();
                mDialog.dismiss();
                if (isExitSys)
                    System.exit(0);

            }
        });
    }

    /**
     * 退出APP
     * @param mContext
     * @param msg
     * @param exit_type 退出的样式
     * @param isExitSys 是否完全退出
     */
    public static void exit(Context mContext, String msg, int exit_type, boolean isExitSys) {
        if (exit_type == 1) {
            exitByDoubleClick(mContext, msg, isExitSys);
        } else if (exit_type == 2) {
            showExitDialog(mContext, msg, isExitSys);
        } else {
            String errormsg = "error exit_type";
            ToastUtils.mkLongTimeToast(mContext, errormsg);
        }
    }
}
