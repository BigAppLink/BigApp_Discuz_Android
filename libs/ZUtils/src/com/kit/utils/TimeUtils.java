package com.kit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TimeUtils {

	public static long getTime(String type, String time1, String time2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat(type).parse(time1);
			date2 = new SimpleDateFormat(type).parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();

		return l;
	}

	public static long getTime24(String type, Date date1, Date date2) {

		//System.out.println(date1.getTime() - date2.getTime());
		
		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : ((24 * 60 * 60 * 1000)
				+ (date2.getTime() - date1.getTime()));

		return l;
	}

	public static String completionTime(int t) {

		if (t >= 10) {
			return t + "";
		} else {
			return "0" + t;
		}

	}

	public static String isAmPm(int t) {

		if (t == 0) {
			return "am";
		} else {
			return "pm";
		}

	}

	public static long getMinuteTime(String type, String time1, String time2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat(type).parse(time1);
			date2 = new SimpleDateFormat(type).parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();

		long minute = (l / 1000) / 60;

		return minute;
	}

	public static long getMinute(String type, String time) {
		Date date = null;

		try {
			date = new SimpleDateFormat(type).parse(time);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date.getTime();

		long minute = (l / 1000) / 60;

		return minute;
	}

	public static HashMap getMinuteDetail(String type, String time1,
			String time2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat(type).parse(time1);
			date2 = new SimpleDateFormat(type).parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();

		long minute = (l / 1000) / 60;

		HashMap map = new HashMap();
		map.put("time1", time1);
		map.put("time2", time2);

		map.put("minute", minute);

		return map;
	}

	public static long compireMinute(String type, String time1, String time2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat(type).parse(time1);
			date2 = new SimpleDateFormat(type).parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();

		long minute = (l / 1000) / 60;

		return minute;
	}

	public static long getSecond(String type, String time1, String time2) {
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = new SimpleDateFormat(type).parse(time1);
			date2 = new SimpleDateFormat(type).parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long l = date1.getTime() - date2.getTime() > 0 ? date1.getTime()
				- date2.getTime() : date2.getTime() - date1.getTime();

		return l / 1000;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
