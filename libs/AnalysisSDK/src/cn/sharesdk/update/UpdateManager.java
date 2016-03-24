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
package cn.sharesdk.update;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.LoggerThread;
import cn.sharesdk.analysis.R;
import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;
import cn.sharesdk.utils.UIHandler;

public class UpdateManager implements Callback, UpdateListener, DownloadListener{
	
	private final int EVENT_IS_UPDATE = 1;
	private final int EVENT_DOWNLOAD_APK = 2;	
	
	private final int STATUS_DOWN_START = 1;
	private final int STATUS_DOWN_UPDATE = 2;
	private final int STATUS_DOWN_OVER = 3;
	
	private final int SHOW_NOTICE_DIALOG = 5;
	
	private Context context;
	private String filePath;
	private int dialogBtnClick = UpdateStatus.NotNow;//更新对话框时，按钮的监听
	
	private DeviceHelper deviceHelper;
	private UpdateListener updateListener;//更新下载监听
	private DownloadListener downloadListener;
	
	private static UpdateManager updateManager;
	private UpdateManager(Context context){
		this.context = context;
		downloadListener = this;
		deviceHelper = DeviceHelper.getInstance(context);
		filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/"+ "download";
	}
	
	public synchronized static UpdateManager getInstance(Context context){
		//System.out.println("UpdateManager =========="+Process.myPid());
		if(updateManager == null){
			updateManager = new UpdateManager(context);
			//System.out.println("UpdateManager getInstance ========== getInstance");
		}
		return updateManager;
	}
	
	public void isUpdate(Context context, UpdateListener listener) {
		UIHandler.prepare();
		if(context != null){
			this.context = context;
		}
		if(listener != null){
			updateListener = listener;
		}
		// WIFI UPDATE 
		boolean wifi = deviceHelper.isWiFiActive();
		if(!wifi && UpdateConfig.isUpdateOnlyWifi()){
			onUpdateReturned(UpdateStatus.NoneWifi, null);
			return;
		}
		LoggerThread.getInstance().isUpdate(context, this);		
	}
	
	public void showNoticeDialog(final Context context, final UpdateResponse response) {
		if(response != null && TextUtils.isEmpty(response.path)){
			return;
		}
		StringBuilder updateMsg = new StringBuilder();
		updateMsg.append(response.version);
		updateMsg.append(context.getString(R.string.analysissdk_update_new_impress));
		updateMsg.append("\n");
		updateMsg.append(response.content);
		updateMsg.append("\n");
		updateMsg.append(context.getString(R.string.analysissdk_update_apk_size, sizeToString(response.size)));
		
		final Dialog dialog = new Dialog(context, R.style.AnalysisSDK_CommonDialog);
		dialog.setContentView(R.layout.analysissdk_update_notify_dialog);
		TextView tvContent = (TextView) dialog.findViewById(R.id.update_tv_dialog_content);
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		tvContent.setText(updateMsg);

		final CheckBox cBox = (CheckBox) dialog.findViewById(R.id.update_cb_ignore);		
		if(UpdateConfig.isUpdateForce()){
			cBox.setVisibility(View.GONE);
		}else {
			cBox.setVisibility(View.VISIBLE);
		}
		
		android.view.View.OnClickListener ocl = new android.view.View.OnClickListener() {
			public void onClick(View v) {
				if(v.getId() == R.id.update_btn_dialog_ok){
					dialogBtnClick = UpdateStatus.Update;
				}else if(v.getId() == R.id.update_btn_dialog_cancel){
					if(cBox.isChecked()){
						dialogBtnClick = UpdateStatus.Ignore;
					}
				}
				dialog.dismiss();
				UpdateAgent.updateDialogDismiss(context, dialogBtnClick, response);
			}
		};
			
		dialog.findViewById(R.id.update_btn_dialog_ok).setOnClickListener(ocl);
		dialog.findViewById(R.id.update_btn_dialog_cancel).setOnClickListener(ocl);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.show();
		
	}

	/**
	 * download apk
	 * 
	 * @param url
	 */
	public void downloadApk(final Context context, final UpdateResponse updateResponse, final DownloadListener downloadListener) {
		if(downloadListener != null){
			this.downloadListener = downloadListener;
		}
		String url = updateResponse.path;
		String apk = null;
		String fileName = updateResponse.md5 + ".apk";
		File parentPath = getDownloadAPKParent();
		if(parentPath != null && parentPath.exists()){
			//judge the apk file exist,check the apk's md5 if it exists
			File file = new File(parentPath, fileName);
			apk = file.getAbsolutePath();
		}		
		LoggerThread.getInstance().downloadApk(context, url, apk);		
	}
	
//	private Runnable mdownApkRunnable = new Runnable() {
//		@Override
//		public void run() {
//			try {
//				URL url = new URL(apkUrl);
//
//				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//				conn.connect();
//				int length = conn.getContentLength();
//				InputStream is = conn.getInputStream();
//
//				File parent = getDownloadAPKParent();
//				if(parent == null){
//					Ln.e("lost permission or do not sdcard =====>>>>", "lost permission or do not sdcard");
//					return;
//				}
//				String apkFile = updateResponse.md5 + ".apk";
//				File ApkFile = new File(parent, apkFile);
//				FileOutputStream fos = new FileOutputStream(ApkFile);
//
//				int count = 0;
//				byte buf[] = new byte[1024];
//
//				do {
//					int numread = is.read(buf);
//					count += numread;
//					progress = (int) (((float) count / length) * 100);
//					UIHandler.sendEmptyMessage(DOWN_UPDATE, UpdateManager.this);
//					if (numread <= 0) {
//						progressDialog.dismiss();
//						UIHandler.sendEmptyMessage(DOWN_OVER, UpdateManager.this);
//						break;
//					}
//					fos.write(buf, 0, numread);
//				} while (!interceptFlag);
//
//				fos.close();
//				is.close();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//	};

//	/**
//	 * install apk
//	 * 
//	 * @param url
//	 */
//	private void installApk(String filePath) {
//		File apkfile = new File(filePath);
//		if (!apkfile.exists()) {
//			return;
//		}
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//		context.startActivity(i);
//
//	}

	//start download apk
	public void downloadStart(){
		Bundle bundle = new Bundle();
		bundle.putInt("status", STATUS_DOWN_START);
		
		Message msg = new Message();
		msg.what = EVENT_DOWNLOAD_APK;
		msg.setData(bundle);
		UIHandler.sendMessage(msg, this);
	}
	
	//update the progress when download a apk 
	public void downloadUpdate(int progress){
		Bundle bundle = new Bundle();
		bundle.putInt("status", STATUS_DOWN_UPDATE);
		bundle.putInt("progress", progress);
		
		Message msg = new Message();
		msg.what = EVENT_DOWNLOAD_APK;
		msg.setData(bundle);
		UIHandler.sendMessage(msg, this);
	}
	
	//download apk complete
	public void downloadEnd(int result, String file){
		Bundle bundle = new Bundle();
		bundle.putInt("status", STATUS_DOWN_OVER);
		bundle.putInt("result", result);
		bundle.putString("filePath", file);
		
		Message msg = new Message();
		msg.what = EVENT_DOWNLOAD_APK;
		msg.setData(bundle);
		UIHandler.sendMessage(msg, this);
	}
			
	//update check complete
	public void isUpdateListener(String result){
		sendMsg(EVENT_IS_UPDATE, result);
	}
	
	private void sendMsg(int what, String result){
		Message msg = new Message();
		msg.what = what;
		msg.obj = result;
		UIHandler.sendMessage(msg, this);
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case EVENT_IS_UPDATE:
			try{
				if(msg.obj == null){
					onUpdateReturned(UpdateStatus.Timeout, null);
					break;
				}
				String resultJson = String.valueOf(msg.obj);
				JSONObject response = new JSONObject(resultJson);
				response = response.optJSONObject("res");
				if(response == null){
					onUpdateReturned(UpdateStatus.Timeout, null);
					break;
				}
				UpdateResponse update = new UpdateResponse(response);				
				int status = update.getStatus(Integer.parseInt(deviceHelper.getVersionCode()));
				onUpdateReturned(status, update);
			}catch (Exception e) {
				e.printStackTrace();
				onUpdateReturned(UpdateStatus.Timeout, null);
			}
			break;
		case EVENT_DOWNLOAD_APK:
			Bundle bundle = msg.getData();
			int status = bundle.getInt("status");
			if(STATUS_DOWN_START == status){
				downloadListener.OnDownloadStart();
			}else if(STATUS_DOWN_UPDATE == status){
				int progress = bundle.getInt("progress");
				downloadListener.OnDownloadUpdate(progress);
			}else{
				int result = bundle.getInt("result");
				String filePath = bundle.getString("filePath");
				downloadListener.OnDownloadEnd(result, filePath);
			}
			break;
		case SHOW_NOTICE_DIALOG:
			//updateAPK(String.valueOf(msg.obj));
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onUpdateReturned(int status, UpdateResponse response) {
		System.out.println("onUpdateReturned ==============" + hashCode());
		if(updateListener != null){
			updateListener.onUpdateReturned(status, response);
			return;
		}
		switch (status) {
		case UpdateStatus.Yes:			
			//showUpdateDialog(context, response);
			boolean ignore = UpdateAgent.isIgnore(context, response); 
			if(UpdateConfig.isSilentDownload() && !ignore){
				//静默更新，不忽略
				UpdateAgent.updateDialogDismiss(context, UpdateStatus.Update, response);
			}else if(UpdateConfig.isUpdateForce()){
				//手动更新
				if(UpdateConfig.isUpdateAutoPopup()){
					showNoticeDialog(context, response);
				}
			}else if(!ignore){
				//自动更新
				if(UpdateConfig.isUpdateAutoPopup()){
					showNoticeDialog(context, response);
				}
			}
			break;
		case UpdateStatus.No:
			if(Ln.DebugMode){
				Toast.makeText(context, "呵呵，木有更新......", Toast.LENGTH_SHORT).show();
			}
			break;
		case UpdateStatus.NoneWifi:
			if(Ln.DebugMode){
				Toast.makeText(context, "没有WIFI链接，只在WIFI下更新！", Toast.LENGTH_SHORT).show();
			}
			break;
		case UpdateStatus.Timeout:
			if(Ln.DebugMode){
				Toast.makeText(context, "网络超时！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void OnDownloadStart() {
		Ln.i("OnDownloadStart ==>>", "OnDownloadStart");
		if(Ln.DebugMode){
			Toast.makeText(context, "OnDownloadStart", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void OnDownloadUpdate(int progress) {
		Ln.i("OnDownloadUpdate progress ==>>", String.valueOf(progress));
	}

	@Override
	public void OnDownloadEnd(int result, String file) {
		Ln.i("OnDownloadEnd ==>> file path", file);
		if(!TextUtils.isEmpty(file) && result == UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS){
			UpdateAgent.startInstall(context, new File(file));
		}
	}
	
	/**show update dialog notify
	 * 
	 * @param context
	 * @param updateResponse
	 */
	public void showUpdateDialog(Context context, UpdateResponse updateResponse){
		Intent intent = new Intent(context, UpdateDialogActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("response", updateResponse);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	/**judge the apk exist or not
	 * 
	 * @param context
	 * @param updateResponse
	 * @return
	 */
	public File isAPKExist(Context context, UpdateResponse updateResponse) {
		String fileName = updateResponse.md5 + ".apk";
		File parentPath = null;
		try {			
			parentPath = getDownloadAPKParent();
			if(parentPath == null){
				return null;
			}
			//judge the apk file exist,check the apk's md5 if it exists
			File apk = new File(parentPath, fileName);
			if(apk.exists()){
				if(updateResponse.md5.equalsIgnoreCase(getFileMD5(apk))){
					return apk;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	private File getDownloadAPKParent(){
		File parentPath = null;
		try {			
			if(deviceHelper.getSdcardState()){
				//judge the parent file exist, create it if not
				parentPath = new File(filePath);
				if(!parentPath.exists()){
					parentPath.mkdirs();
				}			
			}else{
				// there is not sdcard
				String path = context.getCacheDir().getAbsolutePath();
				new File(path).mkdir();
				setPermissions(path, 505);
				path = path + "/download";
				new File(path).mkdir();
				setPermissions(path, 505);			
				parentPath = new File(path);
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return parentPath;
		
	}
	
	private static String getFileMD5(File paramFile) {
		MessageDigest localMessageDigest = null;
		FileInputStream localFileInputStream = null;
		byte[] arrayOfByte = new byte[1024];
		try {
			if (!paramFile.isFile()) {
				return "";
			}
			localMessageDigest = MessageDigest.getInstance("MD5");
			localFileInputStream = new FileInputStream(paramFile);
			int i;
			while ((i = localFileInputStream.read(arrayOfByte, 0, 1024)) != -1) {
				localMessageDigest.update(arrayOfByte, 0, i);
			}
			localFileInputStream.close();
		} catch (Exception localException) {
			localException.printStackTrace();
			return null;
		}
		BigInteger localBigInteger = new BigInteger(1,
				localMessageDigest.digest());
		return String.format("%1$032x", new Object[] { localBigInteger });
	}
	
	/**
	 * 用反射方法，来调用android 隐藏方法
	 * @param path
	 * @param mode
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static boolean setPermissions(String path, int mode) {
		String className = UpdateManager.class.getName();
		try {
			Class localClass = Class.forName("android.os.FileUtils");
			Method localMethod = localClass.getMethod("setPermissions", new Class[] { String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE });
			localMethod.invoke(null, new Object[] { path, Integer.valueOf(mode), Integer.valueOf(-1), Integer.valueOf(-1) });
			return true;
		} catch (ClassNotFoundException localClassNotFoundException) {
			Ln.e(className, "error when set permissions:", localClassNotFoundException);
		} catch (NoSuchMethodException localNoSuchMethodException) {
			Ln.e(className, "error when set permissions:", localNoSuchMethodException);
		} catch (IllegalArgumentException localIllegalArgumentException) {
			Ln.e(className, "error when set permissions:", localIllegalArgumentException);
		} catch (IllegalAccessException localIllegalAccessException) {
			Ln.e(className, "error when set permissions:", localIllegalAccessException);
		} catch (InvocationTargetException localInvocationTargetException) {
			Ln.e(className, "error when set permissions:", localInvocationTargetException);
		}
		return false;
	}

	/**
	 * transfer the size of K to M
	 * @param size
	 * @return
	 */
	private String sizeToString(int size){
		StringBuilder builder = new StringBuilder();;
		if(size < 1024){
			builder.append(size);
			builder.append("K");
		} else {
			double m = size/1024.00;
			builder.append(m);
			builder.append("M");
		}
		return builder.toString();
	}
	
}
