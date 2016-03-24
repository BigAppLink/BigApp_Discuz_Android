package com.kit.widget.dialog;

import com.kit.extend.widget.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ThreeBtnDialog extends Dialog implements OnClickListener {

	protected Context mContext;
	protected View mParentView;
	protected View mRootView;

	protected ScrollView mScrollView;
	protected TextView mTVContent;
	public Button mButton1, mButton2;
	public Button mButtonCancel;
	protected ListView mListView;

	protected OnClickListener mOkClickListener;
	protected String content;
	protected int layoutId;

	public ThreeBtnDialog(Context context, String content, int layoutId) {
		super(context);
		// TODO Auto-generated constructor stub

		this.mContext = context;
		this.content = content;
		this.layoutId = layoutId;
	}

	protected void initView(Context context) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(layoutId);

		mTVContent = (TextView) findViewById(R.id.tvContent);
		mButton1 = (Button) findViewById(R.id.btn1);
		mButton1.setOnClickListener(this);
		mButton2 = (Button) findViewById(R.id.btn2);
		mButton2.setOnClickListener(this);
		mButtonCancel = (Button) findViewById(R.id.btnCancel);
		mButtonCancel.setOnClickListener(this);

		Window dialogWindow = getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		//
		// WindowManager m = dialogWindow.getWindowManager();
		// Display d = m.getDefaultDisplay();
		// lp.height = 2 * d.getHeight();
		// lp.width = 2 *d.getWidth();
		// lp.alpha = 0.8f;
		//
		// dialogWindow.setAttributes(lp);

		// lp.flags = lp.flags|WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// dialogWindow.setAttributes(lp);

		ColorDrawable dw = new ColorDrawable(0x0000ff00);
		dialogWindow.setBackgroundDrawable(dw);

		mTVContent.setText(content);
	}

	public void setContent(String content) {

		this.content = content;
	}

	public void setOnOKButtonListener(OnClickListener onClickListener) {
		mOkClickListener = onClickListener;
	}

	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.btn1) {
			onButton1();
		} else if (id == R.id.btn2) {
			onButton2();
		} else if (id == R.id.btnCancel) {
			onButtonCancel();
		}

	}

	protected void onButton1() {
		dismiss();

		if (mOkClickListener != null) {
			mOkClickListener.onClick(this, 0);
		}
	}

	protected void onButton2() {
		dismiss();

		if (mOkClickListener != null) {
			mOkClickListener.onClick(this, 0);
		}
	}

	protected void onButtonCancel() {
		dismiss();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView(mContext);

	}

}
