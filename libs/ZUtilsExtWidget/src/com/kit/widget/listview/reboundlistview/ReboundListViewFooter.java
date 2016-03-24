/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.kit.widget.listview.reboundlistview;

import com.kit.extend.widget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReboundListViewFooter extends LinearLayout {

	// 未 拖拉或点击 状态
	public final static int STATE_NORMAL = 0;

	// 松开加载更多状态
	public final static int STATE_READY = 1;

	// 加载中
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	private Boolean footerProgressBarEnabled, footerTextViewEnabled;

	// public ReboundListViewFooter(Context context) {
	// super(context);
	// initView(context);
	// }

	public ReboundListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public void setState(int state) {

		mProgressBar.setVisibility(View.GONE);

		// mHintView.setVisibility(View.GONE);
		if (state == STATE_READY) {
			ready();
		} else if (state == STATE_LOADING) {
			loading();
		} else {
			normal();

		}
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	public void ready() {
		if (footerTextViewEnabled) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.rebound_listview_footer_hint_ready);
		}

		if (footerProgressBarEnabled)
			mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * normal status
	 */
	public void normal() {

		// LogUtils.printLog(getClass(), "normal()");
		if (footerTextViewEnabled) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.rebound_listview_footer_hint_normal);
		}
		if (footerProgressBarEnabled)
			mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		// mHintView.setVisibility(View.GONE);
		if (footerProgressBarEnabled)
			mProgressBar.setVisibility(View.VISIBLE);
		if (footerTextViewEnabled) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.rebound_listview_header_hint_loading);
		}
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
		mContentView.setVisibility(GONE);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
		mContentView.setVisibility(VISIBLE);
	}

	private void initView(Context context, AttributeSet attrs) {

		mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ReboundListView);

		footerProgressBarEnabled = a
				.getBoolean(
						R.styleable.ReboundListView_ReboundListView_FooterProgressBarEnabled,
						false);
		footerTextViewEnabled = a
				.getBoolean(
						R.styleable.ReboundListView_ReboundListView_FooterTextViewEnabled,
						true);

		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.rebound_listview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView
				.findViewById(R.id.rebound_listview_footer_content);

		mProgressBar = moreView
				.findViewById(R.id.rebound_listview_footer_progressbar);

		if (footerProgressBarEnabled) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}

		mHintView = (TextView) moreView
				.findViewById(R.id.rebound_listview_footer_hint_textview);
		if (footerTextViewEnabled) {
			mHintView.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.GONE);
		}
	}

}
