package com.kit.utils;

import java.util.Vector;

public class DeviceTokenUtils {

	public static String getMd5DeviceToken() {

		// 1、取得本地时间：
		java.util.Calendar cal = java.util.Calendar.getInstance();

		// 2、取得时间偏移量：
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);

		// 3、取得夏令时差：
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);

		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		// 之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
		// System.out.println("UTC:"+new Date(cal.getTimeInMillis()));

		long utcTimes = cal.getTimeInMillis();
		String str = utcTimes + "" + getRandomNum();

		// System.out.println(str + " str str " + str.length());

		MD5 md5 = new MD5();
		String deviceToken = md5.getMD5FromStr(str);

		// System.out.println(deviceToken + " deviceToken deviceToken "
		// + deviceToken.length());
		return deviceToken;

	}

	public static int getRandomNum() {

		Vector<Integer> k = new Vector<Integer>();

		int randomNum = 0;
		for (int m = 100; m < 1000; m++)
			k.add(m);

		for (int m = 0; m < 1; m++) {// m控制生成几个随机数
			int n = (int) (Math.random() * k.size());

			randomNum = k.get(n);
			//System.out.println("RandomNum: " + randomNum);

		}

		return randomNum;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
