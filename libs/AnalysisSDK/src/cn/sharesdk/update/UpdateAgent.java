package cn.sharesdk.update;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import cn.sharesdk.LoggerThread;
import cn.sharesdk.utils.Ln;

public class UpdateAgent {
	
	private static UpdateListener updateListener = null;
	private static DialogButtonListener dialogButtonListener = null;
	private static DownloadListener downloadListener = null;
	
	private static Context context;
	private static UpdateManager updateManager;

	private static void setContext(Context c){
		if(context == null && c != null){
			context = c;
			updateManager = UpdateManager.getInstance(context);
		}else if(c == null){
			Ln.e("context is null ==>>", "the param of context should not be null");
		}
	}
	
	public static void setDefault() {
//		setDeltaUpdate(true);
		setAppkey(null);
		setChannel(null);
		setUpdateAutoPopup(true);
		setUpdateOnlyWifi(true);
		setDialogListener(null);
		setDownloadListener(null);
		setUpdateListener(null);
	}
	
	//自动更新
	public static void update(Context context) {
		setContext(context);
		UpdateConfig.setUpdateForce(false);
		UpdateConfig.setSilentDownload(false);
		if(updateManager != null){
			updateManager.isUpdate(context, updateListener);
		}
	}
	
	//自动更新
	public static void update(Context context, String appkey, String channel) {
		setAppkey(appkey);
		setChannel(channel);
		update(context);
	}

	//静默更新
	public static void silentUpdate(Context context) {
		UpdateConfig.setUpdateForce(false);
		UpdateConfig.setSilentDownload(true);
		update(context);
	}

	//手动更新
	public static void forceUpdate(Context context) {
		UpdateConfig.setUpdateForce(true);
		UpdateConfig.setSilentDownload(false);
		update(context);
	}

	// 检测配置是否正确，否则给提示Toast
//	public static void setUpdateCheckConfig(boolean checkConfig) {
//		UpdateConfig.setUpdateCheckConfig(checkConfig);
//	}
	
	//在wifi下更新
	public static void setUpdateOnlyWifi(boolean wifiUpdate) {
		UpdateConfig.setUpdateOnlyWifi(wifiUpdate);
	}

	//更新提示开关，是否弹出对话框
	public static void setUpdateAutoPopup(boolean popupUpdate) {
		UpdateConfig.setUpdateAutoPopup(popupUpdate);
	}
	
	//增量更新
//	public static void setDeltaUpdate(boolean deltaUpdate) {
//		UpdateConfig.setDeltaUpdate(deltaUpdate);
//	}

	//设置APPKEY
	public static void setAppkey(String appkey) {
		LoggerThread.getInstance().setAppKey(appkey);
		UpdateConfig.setAppkey(appkey);
	}
	
	//设置渠道
	public static void setChannel(String channel) {
		LoggerThread.getInstance().setChannel(channel);
		UpdateConfig.setChannel(channel);
	}

	//设置更新提示方式:STYLE_DIALOG,STYLE_NOTIFICATION
//	public static void setUpdateUIStyle(int style) {
//		UpdateConfig.setUpdateUIStyle(style);
//	}

	//设置更新下载通知栏高级样式，是否有暂停按钮（4.1版本以上）
//	public static void setRichNotification(Boolean richNotification) {
//		UpdateConfig.setRichNotification(richNotification);
//	}
	
	//设置监听检测更新的结果
	public static void setUpdateListener(UpdateListener listener) {
		updateListener = listener;
	}

	//设置监听对话框按键的操作
	public static void setDialogListener(DialogButtonListener listener) {
		dialogButtonListener = listener;
	}

	//设置监听下载进度
	public static void setDownloadListener(DownloadListener listener) {
		downloadListener = listener;
	}

	//弹出更新提示对话框
	public static void showUpdateDialog(Context context, UpdateResponse updateResponse) {
		updateManager.showUpdateDialog(context, updateResponse);
	}
	
	//弹出更新提示通知栏
//	public static void showUpdateNotification(Context context, UpdateResponse updateResponse) {
//		setContext(context);
//		// TODO
//	}

	/**检测是否已经下载完成
	 * 
	 * @param context
	 * @param updateResponse
	 * @return
	 */
	public static File downloadedFile(Context context, UpdateResponse updateResponse) {
		setContext(context);
		return updateManager.isAPKExist(context, updateResponse);
	}
	
	//下载最新版本
	public static void startDownload(Context context, UpdateResponse updateResponse) {
		setContext(context);
		updateManager.downloadApk(context, updateResponse, downloadListener);
	}
	
	//保存忽略该版本
	public static void ignoreUpdate(Context context, UpdateResponse updateResponse) {
		UpdateConfig.saveIgnoreMd5(context, updateResponse.md5);
	}
	
	//判断当前版本是否被忽略
	public static boolean isIgnore(Context context, UpdateResponse updateResponse) {
		String md5 = updateResponse.md5;
		if(!TextUtils.isEmpty(md5)){
			if(md5.equalsIgnoreCase(UpdateConfig.getIgnoreMd5(context)) && !UpdateConfig.isUpdateForce()){
				return true;
			}
		}
		return false;
	}

	//安装以下载文件
	public static void startInstall(Context context, File file) {
		Intent localIntent = new Intent(Intent.ACTION_VIEW);
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		localIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(localIntent);
	}
		
	static void updateDialogDismiss(Context context, int clickStatus, UpdateResponse response) {
		switch (clickStatus) {
		case UpdateStatus.Update:
			// TODO 立即更新，下载
			File file = downloadedFile(context, response);
			if(file == null){
				startDownload(context, response);
			} else {
				startInstall(context, file);
			}
			break;
		case UpdateStatus.NotNow:
			//稍后更新，不执行
			break;
		case UpdateStatus.Ignore:
			//忽略更新，保存起来
			ignoreUpdate(context, response);
			break;

		default:
			break;
		}
		
		if(dialogButtonListener != null){
			dialogButtonListener.onClick(clickStatus);
		}
	}
	
}
