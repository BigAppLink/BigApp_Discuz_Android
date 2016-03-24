package com.youzu.clan.base.util;


import android.content.Context;
import android.view.Gravity;

public class ToastUtils extends com.kit.utils.ToastUtils{
	private static android.widget.Toast mToast;
	
	public static void show(Context context, String msg) {
		if (mToast == null) {
			mToast = android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		}
		mToast.setText(msg);
		mToast.show();
	}
	
	public static void show(Context context, int resId) {
		show(context, context.getResources().getString(resId));
	}
	
}
