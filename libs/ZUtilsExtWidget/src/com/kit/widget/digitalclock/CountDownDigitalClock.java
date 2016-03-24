package com.kit.widget.digitalclock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
//import android.text.format.DateFormat;
import android.util.AttributeSet;

/**
 * 自定义DigitalClock输出格式
 * 
 * 
 */
public class CountDownDigitalClock extends android.widget.DigitalClock {

	Calendar mCalendar;
	private final static String m12 = "h:mm aa";// h:mm:ss aa
	private final static String m24 = "k:mm";// k:mm:ss
	private FormatChangeObserver mFormatChangeObserver;

	private Runnable mTicker;
	private Handler mHandler;

	private boolean mTickerStopped = false;

	String mFormat;
	// add begin by fly
	private long mDeadtime;
	private DeadtimeListener mDeadtimeLister;

	// add end by fly
	public CountDownDigitalClock(Context context) {
		super(context);
		initClock(context);
	}

	public CountDownDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock(context);
	}

	private void initClock(Context context) {

		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}

		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);

		setFormat();
	}

	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();

		/**
		 * requests a tick on the next hard-second boundary
		 */
		mTicker = new Runnable() {
			public void run() {
				if (mTickerStopped)
					return;
				// mCalendar.setTimeInMillis(System.currentTimeMillis());
				long mCurrentTime = System.currentTimeMillis();
				// add begin by fly

//				java.util.Date dt = new Date(mCurrentTime);
//				SimpleDateFormat sdf2 = new SimpleDateFormat(
//						"yyyy/MM/dd HH:mm:ss");
//				String d = sdf2.format(dt);
//				String d2 = sdf2.format(dt);
//
//				System.out.println(mDeadtime + " = mDeadtime mCurrentTime= "
//						+ mCurrentTime + " " + d);

				if (mCurrentTime >= mDeadtime) {
					mDeadtimeLister.onTimeEnd();
					return;
				}
				long mTimeDistance = mDeadtime - mCurrentTime;
				if (mTimeDistance <= 0) {
					return;
				}

				String deadTimeStr = format(mTimeDistance);
				// String deadTimeStr = "距离活动结束还有" + deadDays + "天" + deadHour
				// + "小时" + deadMinute + "分" + deadMills + "秒";
				//System.out.println(deadTimeStr);
				// add end by fly
				setText(deadTimeStr);
				// setText(DateFormat.format(mFormat, mCalendar));
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mTickerStopped = true;
	}

	/**
	 * Pulls 12/24 mode from system settings
	 */
	private boolean get24HourMode() {
		return android.text.format.DateFormat.is24HourFormat(getContext());
	}

	private void setFormat() {
		if (get24HourMode()) {
			mFormat = m24;
		} else {
			mFormat = m12;
		}
	}

	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {
			setFormat();
		}
	}

	// add begin by fly
	/**
	 * set the dead time
	 * 
	 * @param deadtime
	 */
	public void setDeadtime(long deadtime) {
		this.mDeadtime = deadtime;
	}

	public interface DeadtimeListener {
		public void onTimeEnd();
	}

	public void setDeadtimeListener(DeadtimeListener deadtimeListener) {
		this.mDeadtimeLister = deadtimeListener;
	}

	// add end by fly

	public static String format(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second
				* ss;

		String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
				+ milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
				+ strMilliSecond;
		// return strDay + "天" + strHour + "时" + strMinute + "分" + strSecond +
		// "秒"
		// + strMilliSecond;
		return strDay + "天" + strHour + "时" + strMinute + "分" + strSecond + "秒";
	}
}
