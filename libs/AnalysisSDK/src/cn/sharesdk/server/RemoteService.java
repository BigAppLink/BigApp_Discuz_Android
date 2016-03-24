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
package cn.sharesdk.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import cn.sharesdk.analysis.model.PostResult;
import cn.sharesdk.utils.Ln;

/** The service class is used to send data! */
public class RemoteService extends Service {
	
	public final static String SET_APP_KEY = "set_app_key";
	public final static String SET_PREURL = "set_pre_url";
	public final static String SET_CHANNEL = "set_channel";
	public final static String SET_LOCATION = "set_location";
	public final static String SET_ISDEBUG = "set_is_debug";
	public final static int EVENT_GET_FEEDBACK_MSG = 0;
	public final static int EVENT_UPDATE_APK = 1;
	public final static int EVENT_DOWNLOAD_APK = 2;
	public final static int EVENT_EXIT_APK = 3;
	private RemoteCallbackList<AIDLCallback> mCallbackList = new RemoteCallbackList<AIDLCallback>();
	
	private ServiceHelper serviceHelper;
	public void onCreate() {
		super.onCreate();		
		serviceHelper = ServiceHelper.getInstance(this);	    
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onRebind(Intent intent) {
	    Ln.e("RemoteService onRebind ==>>", "onRebind");
		super.onRebind(intent);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
	    Ln.e("RemoteService onBind ==>>", "onBind");
		return bind;
	}
	
	public void onDestroy() {
		Intent localIntent = new Intent();
		localIntent.setClass(this, RemoteService.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}
	
	AIDLService.Stub bind = new AIDLService.Stub() {
		
		@Override
		public void uploadLog() throws RemoteException {
			serviceHelper.sendUploadLogMsg();
		}
		
		@Override
		public void setting(String action, String value) throws RemoteException {
			if(SET_APP_KEY.equals(action)){
				serviceHelper.setAppKey(value);
			}else if(SET_PREURL.equals(action)){
				serviceHelper.setBaseURL(value);
			}else if(SET_CHANNEL.equals(action)){
				serviceHelper.setChannel(value);
			}else if(SET_LOCATION.equals(action)){
				serviceHelper.setAutoLocation(Boolean.parseBoolean(value));
			}else if(SET_ISDEBUG.equals(action)){
				Ln.DebugMode = Boolean.parseBoolean(value);
			}
		}
		
		@Override
		public void saveLog(String action, String jsonString) throws RemoteException {
			Bundle bundle = new Bundle();
			bundle.putString("action", action);
			bundle.putString("value", jsonString);
			serviceHelper.saveAndSendLogMsg(bundle);
		}

		@Override
		public void updateApk() throws RemoteException {
			serviceHelper.sendUpdateApkMsg();
		}

		@Override
		public void updateConfig() throws RemoteException {
			serviceHelper.sendUpdateConfigMsg();
		}

		@Override
		public void registerCallback(AIDLCallback cb) throws RemoteException {
			if(cb != null){
				mCallbackList.register(cb);
			}
		}

		@Override
		public void unregisterCallback(AIDLCallback cb) throws RemoteException {
			if(cb != null){
				mCallbackList.unregister(cb);
			}
		}

		@Override
		public void getfeedback(String page, String size) throws RemoteException {
			Bundle bundle = new Bundle();
			bundle.putString("page", page);
			bundle.putString("size", size);
			serviceHelper.sendGetFeedback(bundle);
		}

		@Override
		public void downloadApk(String apkUrl, String apkFilePath) throws RemoteException {
			Bundle bundle = new Bundle();
			bundle.putString("url", apkUrl);
			bundle.putString("path", apkFilePath);
			serviceHelper.sendDownloadApkMsg(bundle);
		}
		
	};
	
//	public void callback(boolean success, int action, String resultJson) {
//		int n = mCallbackList.beginBroadcast();
//		for (int i = 0; i < n; i++) {
//			try {
//				if (success) {
//					mCallbackList.getBroadcastItem(i).onComplete(action, resultJson);
//				} else {
//					mCallbackList.getBroadcastItem(i).onError(action, resultJson);
//				}
//			} catch (RemoteException e) {
//				Ln.e("callback error == ", e.getMessage());
//			}
//		}
//		mCallbackList.finishBroadcast();
//	}
	
	public void callback(int action, PostResult result) {
		boolean success = false;
		String resultJson = "Exception when get msg from server， and return null";
		if(result != null){
			success = result.isSuccess();
			resultJson = result.getResponseMsg();
		}
		
		int n = mCallbackList.beginBroadcast();
		for (int i = 0; i < n; i++) {
			try {
				if (success) {
					mCallbackList.getBroadcastItem(i).onComplete(action, resultJson);
				} else {
					mCallbackList.getBroadcastItem(i).onError(action, resultJson);
				}
			} catch (RemoteException e) {
				Ln.e("callback error == ", e.getMessage());
			}
		}
		mCallbackList.finishBroadcast();
	}
	
}
