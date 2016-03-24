package com.kit.utils;

import java.util.Date;
import java.util.Calendar;

/**
 * @author Joey.Zhao Email: laozhao1005@gmail.com
 * 
 * @date 2014年3月21日
 */
public class CalendarUtils {

	public static int getYear() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH);// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		int minute = ca.get(Calendar.MINUTE);// 分
		int hour = ca.get(Calendar.HOUR);// 小时
		int second = ca.get(Calendar.SECOND);// 秒
		int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);
		return year;
	}

	public static int getMonth() {
		Calendar ca = Calendar.getInstance();

		int month = ca.get(Calendar.MONTH);// 获取月份

		return month;
	}

	public static int getDay() {
		Calendar ca = Calendar.getInstance();
		int day = ca.get(Calendar.DATE);// 获取日
		return day;
	}

	public static int getTheMonthDayCount(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDay;
	}

	public static int getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
				.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	 /**
     * 获取当前日期是星期几
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
			w = 0;
		return weekDays[w];
	}
}
