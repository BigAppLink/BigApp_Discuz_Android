package com.kit.widget.calendar;

import java.util.*;

public class DayStyle {
	// methods

	// fields
	private final static String[] vecStrWeekDayNames = getWeekDayNames();

	// 表头显示数据
	private static String[] getWeekDayNames() {
		String[] vec = new String[10];
		vec[Calendar.SUNDAY] = "周日";
		vec[Calendar.MONDAY] = "周一";
		vec[Calendar.TUESDAY] = "周二";
		vec[Calendar.WEDNESDAY] = "周三";
		vec[Calendar.THURSDAY] = "周四";
		vec[Calendar.FRIDAY] = "周五";
		vec[Calendar.SATURDAY] = "周六";
		return vec;
	}

	// 表头Name
	public static String getWeekDayName(int iDay) {
		return vecStrWeekDayNames[iDay];
	}

	// methods
	public static int getColorFrameHeader(boolean bHoliday) {
		// 标题栏颜色
		if (bHoliday)
			return 0x80FF8C00;
		return 0x80FF8C00;
	}

	public static int getColorTextHeader(boolean bHoliday) {
		// 标题栏字体
		if (bHoliday)
			return 0xffd0d0d0;
		return 0xffcccccc;
	}

	public static int getColorText(boolean bHoliday, boolean bToday,
			boolean bMark) {
		if (bToday || bMark)
			return 0xffffffff;
		
		if (bHoliday)
			return 0xfff0f0f0;

		return 0xff4682B4;
	}

	// public static int getColorText(boolean bHoliday, boolean bToday) {
	// if (bToday)
	// return 0xff002200;
	// if (bHoliday)
	// return 0xfff0f0f0;
	// return 0xffdddddd;
	// }
	// public static int getColorBkg(boolean bHoliday, boolean bToday) {
	// if (bToday)
	// return 0xff88bb88;
	// if (bHoliday)
	// return 0xffaaaaaa;
	// return 0xff888888;
	// }

	public static int getColorBkg(boolean bHoliday, boolean bToday,
			boolean bMark) {
		
		if (bToday)
			return 0xff88bb88;
		
		if (bMark)
			return 0xffff4500;
		
		if (bHoliday)
			return 0x99FFF5EE;
		


		

		return 0x90ffffff;
	}

	public static int getWeekDay(int index, int iFirstDayOfWeek) {
		int iWeekDay = -1;

		if (iFirstDayOfWeek == Calendar.MONDAY) {
			iWeekDay = index + Calendar.MONDAY;
			if (iWeekDay > Calendar.SATURDAY)
				iWeekDay = Calendar.SUNDAY;
		}

		if (iFirstDayOfWeek == Calendar.SUNDAY) {
			iWeekDay = index + Calendar.SUNDAY;
		}

		return iWeekDay;
	}

}
