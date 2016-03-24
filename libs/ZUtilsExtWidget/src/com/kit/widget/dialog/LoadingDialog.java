package com.kit.widget.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;

public class LoadingDialog {
	public Context context;
	public ProgressDialog dlg;
	public int resLayoutId;
	public int resImgId;
	public String text;
	public boolean isCanceledOnTouchOutside;
	public LoadingView mLoadView;
	public ImageView imgView;
	public TextView alert;
	public Handler handler;
	public boolean isFinishActivity;
	public int resImgIdTemp = 0;

	public static ProgressDialog createProgressDialog(Context context,
			String text) {
		final ProgressDialog dlg = new ProgressDialog(context, R.style.Theme_Dialog_Default);

		dlg.show();
		dlg.setContentView(R.layout.loading_layout);
		LinearLayout root = (LinearLayout) dlg
				.findViewById(R.id.progressDialog);
		root.setGravity(android.view.Gravity.CENTER);
		root.getBackground().setAlpha(100);// 0~255透明度值 ，0为完全透明，255为不透明
		LoadingView mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(R.drawable.loading_img);
		root.addView(mLoadView);
		TextView alert = new TextView(context);
		alert.setGravity(android.view.Gravity.CENTER);
		alert.setHeight(50);
		alert.setText(text);
		alert.setTextColor(0xFFFFFFFF);
		root.addView(alert);
		dlg.setCanceledOnTouchOutside(false);
		return dlg;
	}

	public LoadingDialog(Context context, String text) {
		this.context = context;
		this.handler = new MyHandler();
		dlg = new ProgressDialog(context, R.style.Theme_Dialog_Default);

		dlg.show();
		dlg.setContentView(R.layout.loading_layout);
		LinearLayout root = (LinearLayout) dlg
				.findViewById(R.id.progressDialog);
		root.setGravity(android.view.Gravity.CENTER);
		root.getBackground().setAlpha(100);// 0~255透明度值 ，0为完全透明，255为不透明
		mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(R.drawable.loading_img);
		root.addView(mLoadView);

		imgView = new ImageView(context);
		imgView.setImageResource(R.drawable.loading_img);
		root.addView(imgView);
		imgView.setVisibility(View.GONE);

		alert = new TextView(context);
		alert.setGravity(android.view.Gravity.CENTER);
		alert.setHeight(50);
		alert.setText(text);
		alert.setTextColor(0xFFFFFFFF);
		root.addView(alert);
		dlg.setCanceledOnTouchOutside(false);

	}

	public LoadingDialog(Context context, int resLayoutId, int resImgId,
			String text, boolean isCanceledOnTouchOutside) {
		this.context = context;
		this.handler = new MyHandler();

		dlg = new ProgressDialog(context, R.style.Theme_Dialog_Default);

		dlg.show();
		dlg.setContentView(resLayoutId);
		LinearLayout root = (LinearLayout) dlg
				.findViewById(R.id.progressDialog);
		root.setGravity(android.view.Gravity.CENTER);
		root.getBackground().setAlpha(100);// 0~255透明度值 ，0为完全透明，255为不透明
		mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(resImgId);
		root.addView(mLoadView);

		imgView = new ImageView(context);
		imgView.setImageResource(R.drawable.loading_img);
		root.addView(imgView);
		imgView.setVisibility(View.GONE);

		alert = new TextView(context);
		alert.setGravity(android.view.Gravity.CENTER);
		alert.setHeight(50);
		alert.setText(text);
		alert.setTextColor(0xFFFFFFFF);
		root.addView(alert);
		dlg.setCanceledOnTouchOutside(isCanceledOnTouchOutside);

	}

	class MyHandler extends Handler {
		public MyHandler() {

		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// Log.d("LoadingDialog MyHandler", "handleMessage......");
			super.handleMessage(msg);
			// 此处可以更新UI
			Bundle b = msg.getData();

			isFinishActivity = false;

			try {
				resImgIdTemp = b.getInt("resImgId");
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {

				isFinishActivity = b.getBoolean("isFinishActivity");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// System.out.println("resImgIdTemp: " + resImgIdTemp);

			if (resImgIdTemp != 0) {
				mLoadView.setVisibility(View.GONE);

				imgView.setImageResource(resImgIdTemp);
				imgView.setVisibility(View.VISIBLE);
				MyCount mc = new MyCount(1000, 100);
				mc.start();
			} else {
				imgView.setVisibility(View.GONE);
				mLoadView.setVisibility(View.VISIBLE);
			}

			text = b.getString("text");
			alert.setText(text);

			// Message msg = new Message();
			// Bundle b = new Bundle();// 存放数据
			// b.putString("color", "我的");
			// msg.setData(b);
			// myHandler.sendMessage(msg); // 向Handler发送消息,更新UI

		}

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// System.out.println("onFinish onFinish");
			dlg.cancel();
			if (isFinishActivity) {
				((Activity) context).finish();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// System.out.println("onTick onTick " + millisUntilFinished);

			// tv.setText("请等待30秒(" + millisUntilFinished / 1000 + ")...");
			// Toast.makeText(NewActivity.this, millisUntilFinished / 1000 + "",
			// Toast.LENGTH_LONG).show();// toast有显示时间延迟
		}
	}

}