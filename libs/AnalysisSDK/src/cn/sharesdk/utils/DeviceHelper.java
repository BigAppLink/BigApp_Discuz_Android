/**
 * ************************************************************
 * ShareSDKStatistics
 * An open source analytics android sdk for mobile applications
 * ************************************************************
 * 
 * @package		ShareSDK Statistics
 * @author		ShareSDK Limited Liability Company
 * @copyright	Copyright 2014-2016, ShareSDK Limited Liability Company
 * @since		Version 1.0
 * @filesource  https://github.com/OSShareSDK/OpenStatistics/tree/master/Android
 *  
 * *****************************************************
 * This project is available under the following license
 * *****************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.sharesdk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceHelper {

	private Context context;
	private static DeviceHelper deviceHelper;

	private DeviceHelper(Context c) {
		this.context = c.getApplicationContext();
	}

	public static DeviceHelper getInstance(Context context) {
		if (deviceHelper == null && context != null) {
			deviceHelper = new DeviceHelper(context);
		}
		return deviceHelper;
	}

	/**
	 * checkPermissions
	 * 
	 * @param permission
	 * @return true or false
	 */
	public boolean checkPermissions(String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Determine the current networking is WIFI
	 * 
	 * @return true of false
	 */
	public boolean currentNetworkTypeIsWIFI() {
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectionManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * Judge wifi is available
	 * 
	 * @return boolean
	 */
	public boolean isWiFiActive() {
		if (checkPermissions("android.permission.ACCESS_WIFI_STATE")) {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			Ln.e("lost permission", "lost--->android.permission.ACCESS_WIFI_STATE");
			return false;
		}
	}

	/**
	 * Testing equipment networking and networking WIFI
	 * 
	 * @return true or false
	 */
	public boolean isNetworkAvailable() {
		if (checkPermissions("android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				return true;
			} else {
				Ln.e("error", "Network error");
				return false;
			}
		} else {
			Ln.e(" lost  permission", "lost----> android.permission.INTERNET");
			return false;
		}
	}

	/**
	 * Get the current time format yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public String getTime() {
		Date date = new Date();
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return localSimpleDateFormat.format(date);
	}

	/**
	 * long type of milliseconds time format yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public String getTime(long milliseconds) {
		Date date = new Date(milliseconds);
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return localSimpleDateFormat.format(date);
	}

	/**
	 * get APPKEY
	 * 
	 * @return appkey
	 */
	public String getAppKey() {
		return getMetaDataValue("SHARESDK_APPKEY");
	}

	/**
	 * get CHANNEL VALUE
	 * 
	 * @return appkey
	 */
	public String getChannel() {
		return getMetaDataValue("SHARESDK_CHANNEL");
	}

	/** MD5 encrypt */
	public String md5(String str) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(str.getBytes());
			byte[] arrayOfByte = localMessageDigest.digest();
			StringBuffer localStringBuffer = new StringBuffer();
			for (int i = 0; i < arrayOfByte.length; i++) {
				int j = 0xFF & arrayOfByte[i];
				if (j < 16)
					localStringBuffer.append("0");
				localStringBuffer.append(Integer.toHexString(j));
			}
			return localStringBuffer.toString();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			Ln.e("MD5Utility", "getMD5 error");
			localNoSuchAlgorithmException.printStackTrace();
		}
		return "";
	}

	/**
	 * get MetaData VALUE
	 * 
	 * @return appkey
	 */
	private String getMetaDataValue(String name) {
		String appkey = "";
		try {
			PackageManager localPackageManager = context.getPackageManager();
			ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(context.getPackageName(), 128);
			if (localApplicationInfo != null) {
				Object value = localApplicationInfo.metaData.get(name);
				if(value != null){
					String str = String.valueOf(value);
					if (!TextUtils.isEmpty(str)) {
						appkey = str;
						return appkey.toString();
					}
				}
				Ln.e("getMetaDataValue", "Could not read " + name + " meta-data from AndroidManifest.xml.");
			}
		} catch (Exception localException) {
			Ln.e("getMetaDataValue", "Could not read " + name + " meta-data from AndroidManifest.xml.");
			localException.printStackTrace();
		}
		return "";
	}

	/**
	 * get currnet activity's name
	 */
	public String getActivityName() {
		if (context == null) {
			Ln.e("getActivityName", "context is null that do not get the activity's name ");
			return "";
		}
		// String activityName = context.getClass().getName();
		// if(!activityName.contains("android.app.Application")){
		// return activityName;
		// }
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (checkPermissions("android.permission.GET_TASKS")) {
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
			return cn.getClassName();
		} else {
			Ln.e("lost permission", "android.permission.GET_TASKS");
			return "";
		}

	}

	/**
	 * get PackageName
	 */
	public String getPackageName() {
		return context.getPackageName();
	}

	/**
	 * get OS number
	 * 
	 * @return the type of 4.4.2
	 */
	public String getSysVersion() {
		String osVersion = "";
		if (checkPermissions("android.permission.READ_PHONE_STATE")) {
			osVersion = android.os.Build.VERSION.RELEASE;
			Ln.w("android_osVersion", "OsVerson" + osVersion);
			return osVersion;
		} else {
			Ln.e("android_osVersion", "OsVerson get failed");
			return null;
		}
	}

	/**
	 * get deviceid
	 * 
	 * add <uses-permission android:name="READ_PHONE_STATE" />
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getDeviceID() {
		if (context == null) {
			return "";
		}
		if (checkPermissions("android.permission.READ_PHONE_STATE")) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = tm.getDeviceId();
			String backId = "";
			if (deviceId != null) {
				backId = new String(deviceId);
				backId = backId.replace("0", "");
			}

			if (((TextUtils.isEmpty(deviceId)) || TextUtils.isEmpty(backId)) && (Build.VERSION.SDK_INT >= 9)) {
				Ln.e("getDeviceID is null ==>>", " getting from the permission READ_PHONE_STATE ==>>");
				try {
					Class c = Class.forName("android.os.SystemProperties");
					Method get = c.getMethod("get", new Class[] { String.class, String.class });
					deviceId = (String) get.invoke(c, new Object[] { "ro.serialno", "unknown" });
				} catch (Exception t) {
					Ln.e("getDeviceID ==>>", " deviceid is null ==>>", t);
					deviceId = null;
				}
			}

			if (!TextUtils.isEmpty(deviceId)) {
				Ln.w("getDeviceID", "deviceId:" + deviceId);
				return deviceId;
			} else {
				Ln.e("getDeviceID", "deviceId is null");
				return "";
			}
		} else {
			Ln.e("lost permissioin", "lost----->android.permission.READ_PHONE_STATE");
			return "";
		}
	}

	/**
	 * Get the version number of the current program
	 * 
	 * @return String
	 */

	public String getCurVersion() {
		String curversion = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			curversion = pi.versionName;
			if (curversion == null || curversion.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Ln.e("VersionInfo", "Exception", e);
		}
		return curversion;
	}

	/**
	 * To determine whether it contains a gyroscope
	 * 
	 * @return boolean
	 */
	public boolean isHaveGravity() {
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (manager == null) {
			return false;
		}
		return true;
	}

	/**
	 * Determine the current network type
	 * 
	 * @return boolean
	 */
	public boolean isNetworkTypeWifi() {
		// TODO Auto-generated method stub

		if (checkPermissions("android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cManager.getActiveNetworkInfo();

			if (info != null && info.isAvailable() && info.getTypeName().equals("WIFI")) {
				return true;
			} else {
				Ln.e("error", "Network not wifi");
				return false;
			}
		} else {
			Ln.e(" lost  permission", "lost----> android.permission.INTERNET");
			return false;
		}

	}

	/**
	 * Get the current application version name
	 * 
	 * @return String
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			if (context == null) {
				Ln.e("getVersionName", "context is null");
				return "";
			}
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Ln.e("getVersionName", "Exception", e);

		}
		return versionName;
	}

	/**
	 * Get the current application version code
	 * 
	 * @return String
	 */
	public String getVersionCode() {
		try {
			if (context == null) {
				Ln.e("getVersionCode", "context is null");
				return "";
			}
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return String.valueOf(pi.versionCode);
		} catch (Exception e) {
			Ln.e("getVersionCode", "Exception", e);
		}
		return "0";
	}

	/** get the carrier number */
	public String getCarrier() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return "-1";
		}

		String operator = tm.getSimOperator();
		if (TextUtils.isEmpty(operator)) {
			operator = "-1";
		}
		Ln.w("getCarrier =================>> ", operator);
		return operator;
	}

	public String getScreenSize() {		
	  //first method
		if (Build.VERSION.SDK_INT < 17) {
			DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
			// 竖屏
			if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				return dm2.widthPixels + "x" + dm2.heightPixels;
			} else {// 横屏
				return dm2.heightPixels + "x" + dm2.widthPixels;
			}
		} else {
			WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();
			Point size = new Point();
			try {
				Method method = display.getClass().getMethod("getRealSize", Point.class);
				method.invoke(display, size);
			} catch (Exception e) {
				e.printStackTrace();
			}

			int screenWidth = size.x;
			int screenHeight = size.y;
			if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				return screenWidth + "x" + screenHeight;
			} else {
				return screenHeight + "x" + screenWidth;
			}
		}
	  		
		// second method
//		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		DisplayMetrics dm = new DisplayMetrics();
//		windowManager.getDefaultDisplay().getMetrics(dm);
//		// 竖屏
//		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//			return dm.widthPixels + "x" + dm.heightPixels;
//		} else {
//			// 横屏
//			return dm.heightPixels + "x" + dm.widthPixels;
//		}	
	    
//		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);   
//		Display display = windowManager.getDefaultDisplay();		
//		// PORTRAIT
//		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//			return display.getWidth() + "x" + display.getHeight();
//		} else {
//			// LANDSCAPE 
//			return display.getHeight() + "x" + display.getWidth();
//		}
	}

	/**
	 * get current network type, ps:3G��2G
	 * 
	 * @return String
	 * */
	public String getNetworkType() {
		ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn == null) {
			return "none";
		}

		NetworkInfo network = conn.getActiveNetworkInfo();
		if (network == null || !network.isAvailable()) {
			return "none";
		}

		int type = network.getType();
		if (ConnectivityManager.TYPE_WIFI == type) {
			return "wifi";
		}

		if (ConnectivityManager.TYPE_MOBILE == type) {
//			String proxy = Proxy.getDefaultHost();
			String wap = "";
//			if (proxy != null && proxy.length() > 0) {
//				wap = " wap";
//			}

			return (isFastMobileNetwork() ? "3G" : "2G") + wap;
		}

		return "none";
	}

	/**
	 * Get the current networking
	 * 
	 * @return WIFI or MOBILE
	 */
	private boolean isFastMobileNetwork() {
		TelephonyManager phone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (phone == null) {
			return false;
		}

		switch (phone.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case 14: // TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case 12: // TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case 15: // TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case 13: // TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		}
		return false;
	}

	/**
	 * get device key, sha(mac:deviceID:model)
	 * <p>
	 * 
	 */
	public String getDeviceKey() {

		String localKey = null;
		try {
			localKey = getLocalDeviceKey();
		} catch (Throwable t) {
			localKey = null;
		}
		if (localKey != null) {
			return localKey;
		}

		String newKey = null;
		try {
			String mac = getMacAddress();
			String udid = getDeviceID();
			String model = getModel();
			String data = mac + ":" + udid + ":" + model;
			byte[] bytes = SHA1(data);
			newKey = byteToHex(bytes);
		} catch (Throwable t) {
			t.printStackTrace();
			newKey = null;
		}
		// save device key
		if (newKey != null) {
			try {
				saveLocalDeviceKey(newKey);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return newKey;
	}

	/** device model */
	public String getModel() {
		return Build.MODEL;
	}

	/** device factory */
	public String getFactory() {
		return Build.MANUFACTURER;
	}

	/** device manu time */
	public String getManuTime() {
		return String.valueOf(Build.TIME);
	}

	/** device system build version */
	public String getManuID() {
		return Build.ID;
	}

	/** device language */
	public String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/** device system time zone */
	public String getTimeZone() {
		return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
	}

	/** device is rooted or not */
	public boolean isRooted() {
		int kSystemRootStateUnknow = -1;
		int kSystemRootStateDisable = 0;
		int kSystemRootStateEnable = 1;
		int systemRootState = kSystemRootStateUnknow;

		if (systemRootState == kSystemRootStateEnable) {
			return true;
		} else if (systemRootState == kSystemRootStateDisable) {
			return false;
		}
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = kSystemRootStateEnable;
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		systemRootState = kSystemRootStateDisable;
		return false;
	}

	/** get CPU name */
	public String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String byteToHex(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buffer.append(String.format("%02x", data[i]));
		}
		return buffer.toString();
	}

	private byte[] SHA1(String text) throws Throwable {
		byte[] data = text.getBytes("utf-8");
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(data);
		return md.digest();
	}

	/** Judge mount sdcard or not */
	public boolean getSdcardState() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/** get device key from the local file */
	private String getLocalDeviceKey() throws Throwable {
		if (!getSdcardState()) {
			return null;
		}

		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File cacheRoot = new File(sdPath, "ShareSDK");
		if (!cacheRoot.exists()) {
			return null;
		}

		File keyFile = new File(cacheRoot, ".dk");
		if (!keyFile.exists()) {
			return null;
		}

		FileInputStream fis = new FileInputStream(keyFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object key = ois.readObject();
		String strKey = null;
		if (key != null && key instanceof char[]) {
			char[] cKey = (char[]) key;
			strKey = String.valueOf(cKey);
		}
		ois.close();

		return strKey;
	}

	private void saveLocalDeviceKey(String key) throws Throwable {
		if (!getSdcardState()) {
			return;
		}

		String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File cacheRoot = new File(sdPath, "ShareSDK");
		if (!cacheRoot.exists()) {
			cacheRoot.mkdirs();
		}

		File keyFile = new File(cacheRoot, ".dk");
		if (keyFile.exists()) {
			keyFile.delete();
		}

		FileOutputStream fos = new FileOutputStream(keyFile);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		char[] cKey = key.toCharArray();
		oos.writeObject(cKey);
		oos.flush();
		oos.close();
	}

	public String getMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifi == null) {
			return "";
		}

		String mac = null;
		WifiInfo info = wifi.getConnectionInfo();
		if (info != null) {
			mac = info.getMacAddress();
		}
		return (mac == null) ? "" : mac;
	}

	private Object getSystemService(String name) {
		try {
			return context.getSystemService(name);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	private Location getLocation() {
		if(!checkPermissions("android.permission.ACCESS_FINE_LOCATION") || !checkPermissions("android.permission.ACCESS_COARSE_LOCATION")){
			Ln.e("getLocation ==>>", "Could not get location from GPS or Cell-id, lack ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission?");
			return null;
		}
		LocationManager loctionManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> matchingProviders = loctionManager.getAllProviders();
		Location location = null;
		for (String prociderString : matchingProviders) {
			Ln.d(" Location provider ==>>", prociderString);
			location = loctionManager.getLastKnownLocation(prociderString);
		}
		return location;
	}
	
	
	/**
	 * Getting latitude of location
	 * @return
	 */
	public String getLatitude(){
		String latitude = "";
		Location location = getLocation();
		if(location != null){
			latitude = String.valueOf(location.getLatitude());
		}
		Ln.i("getLatitude ==>>", latitude + "");
		return latitude;
	}
	
	/**
	 * Getting longtitude of location
	 * @return
	 */
	public String getLongitude(){
		String longtitude = "";
		Location location = deviceHelper.getLocation();
		if(location != null){
			longtitude = String.valueOf(location.getLongitude());
		}
		Ln.i("getLongitude ==>>", longtitude + "");
		return longtitude;
	}
	
	public int dipToPx(int dip) {
		float density = 0;
		if (density <= 0.0F) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) (dip * density + 0.5F);
	}
}
