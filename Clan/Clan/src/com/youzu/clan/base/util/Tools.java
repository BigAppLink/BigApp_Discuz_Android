package com.youzu.clan.base.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public final class Tools {
	
	
	public static String getDeviceId(Context context) {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return "0_0";
		}

		StringBuffer sb = new StringBuffer();
		String imei = tm.getDeviceId();
		sb.append(TextUtils.isEmpty(imei) ? "0" : imei);
		sb.append("_");

		String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		sb.append(TextUtils.isEmpty(androidId) ? "0" : androidId);

		return sb.toString();
	}
	
	/**
	 * 获取网络类型
	 * 
	 * @return
	 */
	public static String getNetworkType(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return "";
		}
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
				return networkInfo.getTypeName();
			}
			return networkInfo.getExtraInfo();
		}

		return "network unavailable";
	}
	
	/**
	 * 获取屏幕分辨率
	 * 
	 * @return
	 */
	public static String getDevicePixels(Context context) {

		DisplayMetrics dm = context.getResources().getDisplayMetrics();  
        int width = dm.widthPixels;  
        int heigth = dm.heightPixels;
		return width + "X" + heigth;
	}
	
	/**
	 * 获取系统版本
	 * 
	 * @return
	 */
	public static String getOs(Context context) {
		String version = Build.VERSION.RELEASE;
		return "Android" + (TextUtils.isEmpty(version) ? "1.0" : version);
	}
	
	/**
	 * 获取手机型号
	 * @return
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}
	
	/**
	 * 判断是否有sim卡
	 * @param context
	 * @return
	 */
	public static boolean hasSimCard(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null && (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT);
	}
	
	/**
	 * 获取手机服务商信息 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/>
	 */
	public static String getNetOperator(Context context) {

		String netOperator = "other";
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			// 返回唯一的用户ID;就是这张卡的编号神马的
			String imsi = telephonyManager.getSubscriberId();
			if (TextUtils.isEmpty(imsi)) {
				return netOperator;
			}
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
				netOperator = "chinaMobile";// 中国移动
			} else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
				netOperator = "chinaUnicom";// 中国联通
			} else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
				netOperator = "chinaTelecom";// "chinaNet";//中国电信
			} else if (imsi.startsWith("46020")) {
				netOperator = "chinaTietong";// 中国铁通
			}
		}
		return netOperator;

	}
	
	
	/**
	 * 获取app版本
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if (info != null) {
				return "a." + info.versionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "a.1.0.0";
	}
	
	/**
	 * 获取version code
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
	    try {  
	        PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 1);  
	        return pi.versionCode;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return 1;  
	    }  
	}  
	
	/**
	 * 获取应用名
	 * @param context
	 * @return
	 */
	public static String getApplicationName(Context context) {  
        try {  
        	PackageManager packageManager = context.getPackageManager();  
        	ApplicationInfo applicationInfo = context.getApplicationInfo();  
        	return (String) packageManager.getApplicationLabel(applicationInfo);  
        } catch (Exception e) {  
        }  
        return "";
    }  
	
}
