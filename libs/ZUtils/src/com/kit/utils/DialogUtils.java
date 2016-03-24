package com.kit.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import com.kit.app.core.task.DoSomeThing;

/**
 * Created by Zhao on 15/7/22.
 */
public class DialogUtils {

    public static void showInputDialog(Context context, String title, String confirm, boolean isCanceledOnTouchOutside, boolean isBackDismiss, final DoSomeThing doSomeThing) {

        final EditText inputServer = new EditText(context);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(inputServer);
        builder.setPositiveButton(confirm,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        doSomeThing.execute(inputName);

                    }
                });
        if (!isBackDismiss) {
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    Log.e("APP", "keyCode:::::::::" + keyCode);

                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        dialog.show();
    }


    /**
     * @param context
     * @param title         标题
     * @param message       内容
     * @param btnOK         确定按钮的文字
     * @param btnCancel     取消按钮的文字
     * @param onOK          点击确定时候的事件
     * @param onCancel      点击取消时候的事件
     * @param isBackDismiss 点击返回按钮的时候，是否关闭弹窗
     */
    public static void showDialog(Context context, String title, String message, String btnOK, String btnCancel, boolean isCanceledOnTouchOutside, boolean isBackDismiss, final DialogInterface.OnClickListener onOK, final DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        if (onOK == null) {
            builder.setMessage(message)
                    .setPositiveButton(btnOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
        } else {
            builder.setMessage(message)
                    .setPositiveButton(btnOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (onOK != null) {
                                onOK.onClick(dialog, id);
                            }
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (onCancel != null) {
                                onCancel.onClick(dialog, id);
                            }

                        }
                    });
        }

        if (!isBackDismiss) {
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    Log.e("APP", "keyCode:::::::::" + keyCode);

                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        builder.create().show();
    }
}
