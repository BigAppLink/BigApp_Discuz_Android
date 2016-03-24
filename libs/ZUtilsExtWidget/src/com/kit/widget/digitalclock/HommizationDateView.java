package com.kit.widget.digitalclock;

import java.util.Calendar;

import com.kit.utils.DateUtils;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
//import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.DigitalClock;

public class HommizationDateView extends DigitalClock {

	Calendar mCalendar;

	private FormatChangeObserver mFormatChangeObserver;

	private Runnable mTicker;
	private Handler mHandler;

	private long time;
	private String format;

	public HommizationDateView(Context context) {
		super(context);
		initClock(context);
	}

	public HommizationDateView(Context context, AttributeSet attrs) {
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

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mHandler = new Handler();

		/**
		 * requests a tick on the next hard-second boundary
		 */
		mTicker = new Runnable() {
			public void run() {

				String timeStr = DateUtils.getHommizationDate(time, format);

				// System.out.println("timeStr: " + timeStr);
				setText(timeStr);

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

	}

	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {

		}
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	public void setTimeAndFormat(long time, String format) {
		this.time = time;
		this.format = format;
	}

}
