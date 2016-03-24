package com.kit.widget.dialog;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class DefaultDialog extends AbstractDialog {

	private ProgressDialog pd;
	private Context context;

	private boolean isAbleGotoNext;

	private String content;

	private int layoutId;
	private boolean haveCancel;

	public DefaultDialog(Context context, String content, int layoutId,
			boolean haveCancel) {
		super(context, content, layoutId);
		this.context = context;
		this.content = content;
		this.layoutId = layoutId;
		this.haveCancel = haveCancel;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		mTVContent.setText(content);
	}

	@Override
	protected void onButtonOK() {
		// TODO Auto-generated method stub
		super.onButtonOK();

	}

	/**
	 * @return the isAbleGotoNext
	 */
	public boolean isAbleGotoNext() {
		return isAbleGotoNext;
	}

	/**
	 * @param isAbleGotoNext
	 *            the isAbleGotoNext to set
	 */
	public void setAbleGotoNext(boolean isAbleGotoNext) {
		this.isAbleGotoNext = isAbleGotoNext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (haveCancel) {
			this.mButtonCancel.setVisibility(View.VISIBLE);

		} else {
			this.mButtonCancel.setVisibility(View.GONE);
		}

	}

}
