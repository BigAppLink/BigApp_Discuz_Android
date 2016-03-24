package cn.sharesdk.applist;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.sharesdk.analysis.R;
import cn.sharesdk.utils.DeviceHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PRTHeader extends LinearLayout {
	private TextView tvHeader;
	private RotateImageView ivArrow;
	private ProgressBar pbRefreshing;
	private TextView tvRefTime;
	
	public PRTHeader(Context context) {
		super(context);
		setOrientation(VERTICAL);
		
		LinearLayout llInner = new LinearLayout(context);
		LinearLayout.LayoutParams lpInner = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpInner.gravity = Gravity.CENTER_HORIZONTAL;
		addView(llInner, lpInner);
		
		ivArrow = new RotateImageView(context);
		ivArrow.setImageResource(R.drawable.analysissdk_ptr_ptr);
		int dp_32 = DeviceHelper.getInstance(context).dipToPx(32);
		LayoutParams lpIv = new LayoutParams(dp_32, dp_32);
		lpIv.gravity = Gravity.CENTER_VERTICAL;
		llInner.addView(ivArrow, lpIv);
		
		pbRefreshing = new ProgressBar(context);
		llInner.addView(pbRefreshing, lpIv);
		pbRefreshing.setVisibility(View.GONE);
		
		LinearLayout llHeader = new LinearLayout(context);
		llHeader.setOrientation(VERTICAL);
		LayoutParams lpLl = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpLl.gravity = Gravity.CENTER_VERTICAL;
		llInner.addView(llHeader, lpLl);
		
		tvHeader = new TextView(getContext());
		tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvHeader.setGravity(Gravity.CENTER);
		int dp_10 = DeviceHelper.getInstance(context).dipToPx(10);
		tvHeader.setPadding(dp_10, dp_10, dp_10, 0);
		tvHeader.setTextColor(0xff000000);
		LayoutParams lpTv = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpTv.gravity = Gravity.CENTER_HORIZONTAL;
		llHeader.addView(tvHeader, lpTv);
		
		tvRefTime = new TextView(getContext());
		tvRefTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvRefTime.setGravity(Gravity.CENTER);
		tvRefTime.setPadding(dp_10, 0, dp_10, dp_10);
		tvRefTime.setTextColor(0xff7f7f7f);
		llHeader.addView(tvRefTime, lpTv);
		
		SharedPreferences sp = context.getSharedPreferences("sharesdk_analysis", Context.MODE_PRIVATE);
		long last = sp.getLong("last_refresh_time", 0);
		if (last == 0) {
			last = System.currentTimeMillis();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(last);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String time = sdf.format(cal.getTime());
		tvRefTime.setText(getContext().getString(R.string.analysissdk_lv_last_refresh_time, time));
	}
	
	public void onPullDown(int percent) {
		if (percent > 100) {
			int degree = (percent - 100) * 180 / 20;
			if (degree > 180) {
				degree = 180;
			}
			if (degree < 0) {
				degree = 0;
			}
			ivArrow.setRotation(degree);
		} else {
			ivArrow.setRotation(0);
		}
		
		if (percent < 100) {
			tvHeader.setText(R.string.analysissdk_lv_pull_to_refresh);
		} else {
			tvHeader.setText(R.string.analysissdk_lv_release_to_refresh);
		}
	}
	
	public void onRequest() {
		ivArrow.setVisibility(View.GONE);
		pbRefreshing.setVisibility(View.VISIBLE);
		tvHeader.setText(R.string.analysissdk_lv_refreshing);
	}
	
	public void reverse() {
		pbRefreshing.setVisibility(View.GONE);
		ivArrow.setRotation(180);
		ivArrow.setVisibility(View.VISIBLE);
		
		long last = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(last);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String time = sdf.format(cal.getTime());
		tvRefTime.setText(getContext().getString(R.string.analysissdk_lv_last_refresh_time, time));
		
		SharedPreferences sp = getContext().getSharedPreferences("sharesdk_analysis", Context.MODE_PRIVATE);
		sp.edit().putLong("last_refresh_time", last);
	}

	private static class RotateImageView extends ImageView {
		private int rotation;
	
		public RotateImageView(Context context) {
			super(context);
		}
	
		public void setRotation(int degree) {
			rotation = degree;
			invalidate();
		}
		
		protected void onDraw(Canvas canvas) {
			canvas.rotate(rotation, getWidth() / 2, getHeight() / 2);
			super.onDraw(canvas);
		}
		
	}

}
