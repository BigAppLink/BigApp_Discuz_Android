package com.kit.utils;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";

    public static String YEAR_MONTH_DAY = "yyyy-MM-dd";

    public static String MONTH_DAY = "MM-dd";

    public static String MONTH_DAY_HOUR_MIN = "MM-dd HH:mm";

    public static String YEAR_MONTH_DAY_HOUR_MIN = "yyyy-MM-dd HH:mm";

    public static String WEIBO_DATE = "EEE MMM d HH:mm:ss Z yyyy";



    public static Date getDateFormLong(long dateLong) {

		Date sd = new Date(dateLong);
		return sd;
	}

    /**
     *根据字符串的时间转换成Date
     * @param date
     * @param format
     * @param locale
     * @return
     */
	public static Date getDate4StringFormat(String date, String format,Locale locale) {

        if(locale==null){
            locale = Locale.US;
        }

		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		long create = 0;
		try {
			create = sdf.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date sd = new Date(create);
		return sd;

	}

	public static long getDate2Long(Date date) {
		long l = date.getTime();

		return l;
	}

	public static long getLongDate4Net(String date) {

		long create = 0;
		try {

			String[] dateStr = date.split("\\+");

			// System.out.println("dateStr[0]: " + dateStr[0]);

			String[] dateStr2 = dateStr[0].split("\\)");
			String ss = dateStr2[0].substring(6, dateStr2[0].length());

			// System.out.println("dateStr2[0]: " + dateStr2[0] + "  ss:  " +
			// ss);

			create = Long.parseLong(ss);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return create;
	}

	public static String getDate4LongFormat(long dateLong, String format) {

		Date sd = null;
		if ((dateLong + "").length() == 10) {
			sd = new Date(dateLong * 1000);
		} else {
			sd = new Date(dateLong);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String c = sdf.format(sd);

		return c;
	}



	public static String getNetDate4Net(String createTime, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long create = 0;
		try {
			create = sdf.parse(createTime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/Date(" + create + "+0800)/";
	}

	public static String getDate4Net(String date, String format) {

		long create = 0;
		String c = "";
		try {

			String[] dateStr = date.split("\\+");

			// System.out.println("dateStr[0]: " + dateStr[0]);

			String[] dateStr2 = dateStr[0].split("\\)");
			String ss = dateStr2[0].substring(6, dateStr2[0].length());

			// System.out.println("dateStr2[0]: " + dateStr2[0] + "  ss:  " +
			// ss);

			create = Long.parseLong(ss);

			Date sd = new Date(create);

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			c = sdf.format(sd);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public static String getDateFormat(Date date, String format) {
		// dateStrWithZone = 1351582444220+0800;

		SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
		String strDate = dateformat1.format(date);
		// System.out.println("DateFormat: " + strDate);

		return strDate;
	}

	public static long getDate2Long(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		long create = 0;
		try {
			create = sdf.parse(date).getTime();
		} catch (ParseException e) {
//			e.printStackTrace();
			ZogUtils.printError(DateUtils.class,e.toString());
		}

		return create;
	}

	public static String getCurrDateFormat(String format) {
		// dateStrWithZone = 1351582444220+0800;
		if (format == null || format.equals("")) {
			format = "";
		}

		long dateLong = System.currentTimeMillis();

		Date date = new Date(dateLong);

		SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
		String strDate = dateformat1.format(date);
		// System.out.println("DateFormat: " + strDate);

		return strDate;
	}


	public static long getCurrDateLong() {

		long dateLong = System.currentTimeMillis();

		return dateLong;
	}

	public static String getHommizationDate(String createTime, String format) {

		try {
			String ret = "";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			long create = sdf.parse(createTime).getTime();
			Date sd = new Date(create);

			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 1天毫秒数

			long hms = (1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND))) / 24;// 1小时毫秒数

			long ms_now = now.getTimeInMillis();
			long sub = ms_now - create;
			if (sub > 0) {
				if (sub < ms) {
					// System.out.println("hms: " + hms);
					if (sub < hms) {
						// System.out.println();
						long mms = (sub / 1000) / 60;
						ret = mms + "分钟前";
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "昨天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "前天";
				} else {

					ret = sdf.format(sd);

					// ret = "更早";
				}
			} else {
				sub = Math.abs(sub);
				if (sub < ms) {
					if (sub < hms) {
						long mms = ((ms / 24) / 1000) / 60;
						ret = mms + "分钟后";
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "明天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "后天";
				} else {

					ret = sdf.format(sd);
					// ret = "更晚";
				}

			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHommizationDate(long time, String format) {

		if (format == null || format.equals(""))
			format = "MM-dd HH:mm";
		try {
			String ret = "";
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			long create = time;
			Date sd = new Date(create);

			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 1天毫秒数

			long hms = (1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND))) / 24;// 1小时毫秒数

			long ms_now = now.getTimeInMillis();
			long sub = ms_now - create;
			if (sub > 0) {
				if (sub < ms) {
					// System.out.println("hms: " + hms);
					if (sub < hms) {
						// System.out.println();
						long mms = (sub / 1000) / 60;
						if (mms == 0) {
							ret = "刚刚";
						} else {
							ret = mms + "分钟前";
						}
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {

					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
					ret = "昨天 " + sdf2.format(sd);

				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
					ret = "前天 " + sdf2.format(sd);

				} else {

					ret = sdf.format(sd);

					// ret = "更早";
				}
			} else {
				sub = Math.abs(sub);
				if (sub < ms) {
					if (sub < hms) {
						long mms = ((ms / 24) / 1000) / 60;
						ret = mms + "分钟后";
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "明天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "后天";
				} else {

					ret = sdf.format(sd);
					// ret = "更晚";
				}

			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHommizationDate(Date date, String format) {

        long time = date.getTime();
        return  getHommizationDate(time,format);
//		try {
//			String ret = "";
//			SimpleDateFormat sdf = new SimpleDateFormat(format);
//			long create = date.getTime();
//			Calendar now = Calendar.getInstance();
//			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
//					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 1天毫秒数
//
//			long hms = 1 * 60 * 60 * 1000;// 1小时毫秒数
//
//			long ms_now = now.getTimeInMillis();
//			long sub = ms_now - create;
//			if (sub > 0) {
//				if (sub < ms) {
//					// System.out.println("hms: " + hms);
//					if (sub < hms) {
//						// System.out.println();
//						long mms = (sub / 1000) / 60;
//                        if (mms == 0) {
//                            ret = "刚刚";
//                        } else {
//                            ret = mms + "分钟前";
//                        }
//					} else {
//
//						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//						ret = "今天 " + sdf2.format(date);
//						// ret = "今天";
//					}
//				} else if (sub < (ms + 24 * 3600 * 1000)) {
//					ret = "昨天";
//				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
//					ret = "前天";
//				} else {
//
//					ret = sdf.format(date);
//
//					// ret = "更早";
//				}
//			} else {
//				sub = Math.abs(sub);
//				if (sub < ms) {
//					if (sub < hms) {
//						long mms = ((ms / 24) / 1000) / 60;
//                        if (mms == 0) {
//                            ret = "刚刚";
//                        } else {
//                            ret = mms + "分钟后";
//                        }
//					} else {
//
//						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//						ret = "今天 " + sdf2.format(date);
//						// ret = "今天";
//					}
//				} else if (sub < (ms + 24 * 3600 * 1000)) {
//					ret = "明天";
//				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
//					ret = "后天";
//				} else {
//
//					ret = sdf.format(date);
//					// ret = "更晚";
//				}
//
//			}
//			return ret;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	public static String getHommizationDate4Net(String date, String format) {

		try {
			String ret = "";
			String[] dateStr = date.split("\\+");

			// System.out.println("dateStr[0]: " + dateStr[0]);

			String[] dateStr2 = dateStr[0].split("\\)");
			String ss = dateStr2[0].substring(6, dateStr2[0].length());

			// System.out.println("dateStr2[0]: " + dateStr2[0] + "  ss:  " +
			// ss);

			SimpleDateFormat sdf = new SimpleDateFormat(format);
			long create = Long.parseLong(ss);
			Date sd = new Date(create);

			// long create = date.getTime();
			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 1天毫秒数

			long hms = 1 * 60 * 60 * 1000;// 1小时毫秒数

			long ms_now = now.getTimeInMillis();
			long sub = ms_now - create;
			if (sub > 0) {
				if (sub < ms) {
					// System.out.println("hms: " + hms);
					if (sub < hms) {
						// System.out.println();
						long mms = (sub / 1000) / 60;
						ret = mms + "分钟前";
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "昨天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "前天";
				} else {

					// SimpleDateFormat sdf = new SimpleDateFormat(format);
					// ret = sdf.format(date);

					ret = sdf.format(sd);

					// ret = "更早";
				}
			} else {
				sub = Math.abs(sub);
				if (sub < ms) {
					if (sub < hms) {
						long mms = (sub / 1000) / 60;
						ret = mms + "分钟后";
					} else {
						SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
						ret = "今天 " + sdf2.format(sd);
						// ret = "今天";
					}
				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "明天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "后天";
				} else {

					ret = sdf.format(sd);
					// ret = "更晚";
				}

			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHommizationDateWithoutMinute(Date date,
			String format) {

		try {
			String ret = "";

			long create = date.getTime();
			Calendar now = Calendar.getInstance();
			long ms = 1000 * (now.get(Calendar.HOUR_OF_DAY) * 3600
					+ now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 1天毫秒数

			long ms_now = now.getTimeInMillis();
			long sub = ms_now - create;
			if (sub > 0) {
				if (sub < ms) {

					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
					ret = "今天 " + sdf2.format(date);
					// ret = "今天";

				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "昨天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "前天";
				} else {

					SimpleDateFormat sdf = new SimpleDateFormat(format);
					ret = sdf.format(date);

					// ret = "更早";
				}
			} else {
				sub = Math.abs(sub);
				if (sub < ms) {

					SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
					ret = "今天 " + sdf2.format(date);
					// ret = "今天";

				} else if (sub < (ms + 24 * 3600 * 1000)) {
					ret = "明天";
				} else if (sub < (ms + 24 * 3600 * 1000 * 2)) {
					ret = "后天";
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat(format);
					ret = sdf.format(date);
					// ret = "更晚";
				}

			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
