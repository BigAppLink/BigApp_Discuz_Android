package com.kit.widget.calendar;

import java.util.*;

public class DayStyle2 {
	// methods

	

	// fields
	private final static String[] vecStrWeekDayNames = getWeekDayNames();

	//��ͷ��ʾ���
	private static String[] getWeekDayNames() {
		String[] vec = new String[10];
		vec[Calendar.SUNDAY] = "����";
		vec[Calendar.MONDAY] = "��һ";
		vec[Calendar.TUESDAY] = "�ܶ�"; 
		vec[Calendar.WEDNESDAY] = "����"; 
		vec[Calendar.THURSDAY] = "����"; 
		vec[Calendar.FRIDAY] = "����"; 
		vec[Calendar.SATURDAY] ="����";
		return vec;
	}

	//��ͷName
	public static String getWeekDayName(int iDay) {
		return vecStrWeekDayNames[iDay];
	}
	
	// methods
	public static int getColorFrameHeader(boolean bHoliday) {
		if (bHoliday)
			return 0xff707070;
		return 0xff666666;
	}

	public static int getColorTextHeader(boolean bHoliday) {
		if (bHoliday)
			return 0xffd0d0d0;
		return 0xffcccccc;
	}

	public static int getColorText(boolean bHoliday, boolean bToday) {
		if (bToday)
			return 0xff002200;
		if (bHoliday)
			return 0xfff0f0f0;
		return 0xffdddddd;
	}

	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return 0xff88bb88;
		if (bHoliday)
			return 0xffaaaaaa;
		return 0xff888888;
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
