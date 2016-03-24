package com.kit.widget.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * 重新了DatePickerDialog
 * 
 * @author hejinxi *
 */
public class DatePickerDialog4NoDay extends DatePickerDialog {
	public DatePickerDialog4NoDay(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {

		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year + "年" + (month + 1) + "月");

	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	public DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	// 显示开始时间
	public void showDate() {
		this.show();

		DatePicker dp = findDatePicker((ViewGroup) this.getWindow()
				.getDecorView());
		if (this != null) {
			int width1 = ((ViewGroup) dp.getChildAt(0)).getChildAt(0)
					.getLayoutParams().width; // 获取最前一位的宽度
			int width2 = 0;
			if (((ViewGroup) dp.getChildAt(0)).getChildAt(2) != null) {
				width2 = ((ViewGroup) dp.getChildAt(0)).getChildAt(2)
						.getLayoutParams().width;// 获取最后一位的宽度
			}
			if (width1 > width2) { // 做匹配。如果前面一位大于后面一位的话。就意味着是年-月-日的显示方式。否则为倒换的。
				((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(
						View.GONE);
				// 隐藏最后一位
			} else {
				((ViewGroup) dp.getChildAt(0)).getChildAt(0).setVisibility(
						View.GONE);
				// 隐藏前一位

			}

		}

	}
}