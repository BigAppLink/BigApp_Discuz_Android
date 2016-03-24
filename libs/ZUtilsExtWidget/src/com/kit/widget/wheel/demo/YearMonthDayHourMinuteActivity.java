package com.kit.widget.wheel.demo;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.kit.extend.widget.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.DateUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.wheel.OnWheelChangedListener;
import com.kit.widget.wheel.WheelConstant;
import com.kit.widget.wheel.WheelView;
import com.kit.widget.wheel.adapters.ArrayWheelAdapter;
import com.kit.widget.wheel.adapters.NumericWheelAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class YearMonthDayHourMinuteActivity extends BaseActivity implements
		OnClickListener {
	private WheelView year;
	private WheelView month;
	private WheelView day;

	private WheelView hours;
	private WheelView mins;
	private WheelView ampm;

	private int yearIndex, monthIndex, dayIndex;

	private int curYear, curMonth, curDay, curHour, curMinute;

	private OnWheelChangedListener listener;
	// 偏移量为上下10年
	private int offset4year = 10;
	private long dateLong;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wheel_year_month_day_hour_minute_layout);
		findViewById(R.id.wheel_view_outside).setOnClickListener(this);

		listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				yearIndex = year.getCurrentItem();
				monthIndex = month.getCurrentItem();
				dayIndex = day.getCurrentItem();

				curHour = hours.getCurrentItem();
				curMinute = mins.getCurrentItem();

				updateDays(year, month, day);
			}
		};
		sethm();

		setymd();

	}

	@Override
	public boolean getExtra() {
		try {
			Bundle bundle = getIntent().getExtras();

			dateLong = bundle.getLong(WheelConstant.WHEEL_EXTRAS_KEY_DATE_LONG);

		} catch (Exception e) {
			ZogUtils.showException(e);
		}
		return super.getExtra();
	}

	@Override
	protected void onStart() {
		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

		//System.out.println("d.getHeight():" + d.getHeight());
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 1.0); // 宽度设置为屏幕的0.8
		p.alpha = 1.0f; // 设置本身透明度
		p.dimAmount = 0.0f; // 设置黑暗度

		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM); // 设置靠右对齐
		super.onStart();
	}

	private void sethm() {
		hours = (WheelView) findViewById(R.id.hour);
		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23);
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		hours.setViewAdapter(hourAdapter);

		mins = (WheelView) findViewById(R.id.mins);
		NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59,
				"%02d");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		mins.setViewAdapter(minAdapter);
		mins.setCyclic(true);

		ampm = (WheelView) findViewById(R.id.ampm);
		ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
				this, new String[] { "AM", "PM" });
		ampmAdapter.setItemResource(R.layout.wheel_text_item);
		ampmAdapter.setItemTextResource(R.id.text);
		ampm.setViewAdapter(ampmAdapter);

		// set current time
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		// 24hour
		hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		// 12hour
		// hours.setCurrentItem(calendar.get(Calendar.HOUR));

		mins.setCurrentItem(calendar.get(Calendar.MINUTE));
		ampm.setCurrentItem(calendar.get(Calendar.AM_PM));

		hours.addChangingListener(listener);
		mins.addChangingListener(listener);
		ampm.addChangingListener(listener);
	}

	private void setymd() {
		year = (WheelView) findViewById(R.id.year);
		month = (WheelView) findViewById(R.id.month);
		day = (WheelView) findViewById(R.id.day);

		Calendar calendar;
		if (dateLong != 0) {
			// 如果上一个activity传过来的有时间日期
			Date date = DateUtils.getDateFormLong(dateLong);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
		} else {
			calendar = Calendar.getInstance(Locale.CHINA);
		}

		// month
		curMonth = calendar.get(Calendar.MONTH);

		// String months[] = new String[] { "January", "February", "March",
		// "April", "May", "June", "July", "August", "September",
		// "October", "November", "December" };
		String months[] = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };
		month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// year
		curYear = calendar.get(Calendar.YEAR);
		// 偏移量为上下10年
		offset4year = 10;
		year.setViewAdapter(new DateNumericAdapter(this, curYear - offset4year,
				curYear + offset4year, offset4year));
		year.addChangingListener(listener);
		year.setCurrentItem(offset4year);

		// day
		updateDays(year, month, day);
		day.addChangingListener(listener);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar
				.get(Calendar.DAY_OF_MONTH) - 1));
		curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);

			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.wheel_view_outside) {

			String yearStr = (curYear - offset4year + yearIndex) + "";
			String monthStr = (monthIndex + 1) + "";
			String dayStr = (curDay) + "";

			String hourStr = (curHour) + "";
			String minuteStr = (curMinute) + "";

			ZogUtils.printLog(getClass(), yearStr + "-" + monthStr + "-"
                    + dayStr + " " + hourStr + ":" + minuteStr);

			Intent intent = new Intent();
			String timeString = yearStr + "-" + monthStr + "-" + dayStr + " "
					+ hourStr + ":" + minuteStr;

			Bundle bundle = new Bundle();
			dateLong = DateUtils.getDate2Long(timeString,
					WheelConstant.WHEEL_YEAR_MONTH_DAY_HOUR_MINUTE);

			intent.putExtra("timeString", timeString);
			bundle.putLong(WheelConstant.WHEEL_EXTRAS_KEY_DATE_LONG, dateLong);

			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);

			finish();
		}
	}
}
