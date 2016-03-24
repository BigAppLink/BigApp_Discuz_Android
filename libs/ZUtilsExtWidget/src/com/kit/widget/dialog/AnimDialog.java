package com.kit.widget.dialog;

import com.kit.extend.widget.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

// 自定义Dialog
class AnimDialog extends Dialog {

	private Window window = null;

	public AnimDialog(Context context) {
		super(context);
	}

	public void showDialog(int layoutResID, int resAnim, int x, int y) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
		setContentView(layoutResID);

		windowDeploy(resAnim, x, y);

		// 设置触摸对话框意外的地方取消对话框
		setCanceledOnTouchOutside(true);
		show();
	}

	public void windowDeploy(int resAnim, int x, int y) {
		window = getWindow(); // 得到对话框

		window.setWindowAnimations(resAnim); // 设置窗口弹出动画
		window.setBackgroundDrawableResource(R.color.transparent);
		// //设置对话框背景为透明
		WindowManager.LayoutParams wl = window.getAttributes();

		// 根据x，y坐标设置窗口需要显示的位置
		wl.x = x; // x小于0左移，大于0右移
		wl.y = y; // y小于0上移，大于0下移
		// wl.alpha = 0.6f; //设置透明度
		wl.gravity = Gravity.TOP; // 设置重力
		window.setAttributes(wl);
	}
}