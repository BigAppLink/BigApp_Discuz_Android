package cn.sharesdk.update;

import android.content.Context;
import android.content.SharedPreferences;


public class UpdateConfig {

	private static boolean updateOnlyWifi = false;
	private static boolean updateAutoPopup = false;
	private static boolean UpdateCheckConfig;
	private static boolean richNotification;
	private static boolean deltaUpdate;
	
	private static boolean updateForce;
	private static boolean silentDownload;
	
	private static String appkey;
	private static String channel;

	//设置更新提示方式:STYLE_DIALOG,STYLE_NOTIFICATION
	private static int updateUIStyle;
	
	public static boolean isUpdateOnlyWifi() {
		return updateOnlyWifi;
	}

	public static void setUpdateOnlyWifi(boolean updateOnlyWifi) {
		UpdateConfig.updateOnlyWifi = updateOnlyWifi;
	}

	public static boolean isUpdateAutoPopup() {
		return updateAutoPopup;
	}

	public static void setUpdateAutoPopup(boolean updateAutoPopup) {
		UpdateConfig.updateAutoPopup = updateAutoPopup;
	}

	public static boolean isDeltaUpdate() {
		return deltaUpdate;
	}

	public static void setDeltaUpdate(boolean deltaUpdate) {
		UpdateConfig.deltaUpdate = deltaUpdate;
	}

	public static boolean isUpdateCheckConfig() {
		return UpdateCheckConfig;
	}

	public static void setUpdateCheckConfig(boolean updateCheckConfig) {
		UpdateCheckConfig = updateCheckConfig;
	}

	public static boolean isRichNotification() {
		return richNotification;
	}

	public static void setRichNotification(boolean richNotification) {
		UpdateConfig.richNotification = richNotification;
	}

	public static boolean isUpdateForce() {
		return updateForce;
	}

	public static void setUpdateForce(boolean updateForce) {
		UpdateConfig.updateForce = updateForce;
	}

	public static boolean isSilentDownload() {
		return silentDownload;
	}

	public static void setSilentDownload(boolean silentDownload) {
		UpdateConfig.silentDownload = silentDownload;
	}

	public static String getAppkey() {
		return appkey;
	}

	public static void setAppkey(String appkey) {
		UpdateConfig.appkey = appkey;
	}

	public static String getChannel() {
		return channel;
	}

	public static void setChannel(String channel) {
		UpdateConfig.channel = channel;
	}

	public static int getUpdateUIStyle() {
		return updateUIStyle;
	}

	public static void setUpdateUIStyle(int updateUIStyle) {
		UpdateConfig.updateUIStyle = updateUIStyle;
	}	

	public static void saveIgnoreMd5(Context context, String md5){
		SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("analysissdk_update_ignore_apk_md5", 0);
		preferences.edit().putString("ignore", md5).commit();
	}
	
	public static String getIgnoreMd5(Context context){
		SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("analysissdk_update_ignore_apk_md5", 0);
		String md5 = preferences.getString("ignore", "");
		return md5;
	}
}
