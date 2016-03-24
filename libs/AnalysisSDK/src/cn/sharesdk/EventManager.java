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
package cn.sharesdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import cn.sharesdk.analysis.model.AIDLCacheEvent;
import cn.sharesdk.analysis.model.EventType;
import cn.sharesdk.analysis.model.PostEvent;
import cn.sharesdk.db.MessageUtils;
import cn.sharesdk.feedback.FeedbackAgent;
import cn.sharesdk.feedback.model.Reply;
import cn.sharesdk.server.AIDLCallback;
import cn.sharesdk.server.AIDLService;
import cn.sharesdk.server.RemoteService;
import cn.sharesdk.update.UpdateListener;
import cn.sharesdk.update.UpdateManager;
import cn.sharesdk.update.UpdateStatus;
import cn.sharesdk.utils.CrashHandler;
import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;
import cn.sharesdk.utils.PreferencesHelper;
import cn.sharesdk.utils.UIHandler;

public class EventManager{

	private static Context context;
	private static String start_date = null;// The start time point
	private static long start = 0;
	private static String end_date = null;// The end time point
	private static long end = 0;//
	private static String duration = null;// run time

	private static String session_id = null;
	private static String last_activity = "APP_START";// currnet activity's name
	private static String current_activity = null;// currnet activity's name
	private static String appkey = "";

	// session continue millis
	private static long sessionContinueMillis = 30000L;
	private static boolean activityTrack = true;

	private static PreferencesHelper dbHelper;
	private static DeviceHelper deviceHelper;
	//存储事件时长
	private static HashMap<String, Long> eventDurationMap = new HashMap<String, Long>();
	//匹配事件的label是否一致
	private static HashMap<String, String> eventLabelMap = new HashMap<String, String>();
	//存储页面的时长
	private static HashMap<String, Long> pageDurationMap = new HashMap<String, Long>();
	//存储一些操作事件
	//存储一些事件
	private static ArrayList<AIDLCacheEvent> settingEventList = new ArrayList<AIDLCacheEvent>();
	private static ArrayList<AIDLCacheEvent> cacheEventList = new ArrayList<AIDLCacheEvent>();

	private static AIDLService aidlService;
	private static UpdateManager updateManager;
	
	public static synchronized void init(Context c) {
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> " + Process.myPid() + ", " + updateManager);
		if (context == null && c != null) {
			UIHandler.prepare();
			context = c.getApplicationContext();
			dbHelper = PreferencesHelper.getInstance(context);
			deviceHelper = DeviceHelper.getInstance(context);
			updateManager = UpdateManager.getInstance(context);
			isServiceConnect(context);
			
		} else if (context == null && c == null){
			Ln.e("Context is null", "call setContext to set it");
			return;
		}
	}
	
	private static ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			aidlService = AIDLService.Stub.asInterface(service);
			// setting base params after server connect
			try {
				// register the callback
				aidlService.registerCallback(callback);

				for (AIDLCacheEvent cacheEvent : settingEventList) {
					// the setting must be done at first
					if (EventType.SETTING == cacheEvent.eventType) {
						aidlService.setting(cacheEvent.key, cacheEvent.value);
					}
				}

				for (AIDLCacheEvent cacheEvent : cacheEventList) {
					if (EventType.SAVELOG == cacheEvent.eventType) {
						aidlService.saveLog(cacheEvent.key, cacheEvent.value);
					} else if (EventType.UPLOAD_LOG == cacheEvent.eventType) {
						aidlService.uploadLog();
					} else if (EventType.GET_FEEDBACK == cacheEvent.eventType) {
						aidlService.getfeedback(cacheEvent.key, cacheEvent.value);
					}
				}
				
				settingEventList.clear();
				cacheEventList.clear();
			} catch (Exception e) {
				EventManager.onError(context, e.getMessage());
				Ln.e("EventManager == ", "ServiceConnection == ", e);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			aidlService = null;
		}

	};
	
	//the callback from the server in other progress
	private static AIDLCallback callback = new AIDLCallback.Stub() {
		
		@Override
		public void onError(int action, String resultJson) throws RemoteException {
			switch (action) {
			case RemoteService.EVENT_GET_FEEDBACK_MSG:
				FeedbackAgent.onGetFeedbackListener(false, resultJson);
				break;
			case RemoteService.EVENT_UPDATE_APK:		
				updateManager.isUpdateListener(null);
				break;
			case RemoteService.EVENT_DOWNLOAD_APK:
				updateManager.downloadEnd(UpdateStatus.DOWNLOAD_COMPLETE_FAIL, null);
			default:
				break;
		}
		}
		
		@Override
		public void onComplete(int action, String resultJson) throws RemoteException {
			switch (action) {
				case RemoteService.EVENT_GET_FEEDBACK_MSG:
					FeedbackAgent.onGetFeedbackListener(true, resultJson);
					break;
				case RemoteService.EVENT_UPDATE_APK:	
					updateManager.isUpdateListener(resultJson);
					break;
				case RemoteService.EVENT_DOWNLOAD_APK:
					try {						
						JSONObject object = new JSONObject(resultJson);
						int progress = object.optInt("progress");
						String msg = object.optString("msg");
						if(progress == 0){
							updateManager.downloadStart();//OnDownloadStart();
						}else if(progress == 100){
							updateManager.downloadEnd(UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS, msg);//OnDownloadEnd(UpdateStatus.DOWNLOAD_COMPLETE_SUCCESS, resultJson);
						}else{
							updateManager.downloadUpdate(progress);//OnDownloadUpdate(progress);
						}
					} catch (Exception e) {
						EventManager.onError(context, e.getMessage());
						Ln.e("EventManager == ", "AIDLCallback == EVENT_DOWNLOAD_APK", e);
					}
				case RemoteService.EVENT_EXIT_APK:
					String exitDate = resultJson;
					dbHelper.setAppExitDate(exitDate);
					break;
				default:
					break;
			}
		}
	};
	
	public static void openActivityDurationTrack(boolean activityTrack){
		EventManager.activityTrack = activityTrack;
	}
	
	public static void setBaseURL(String url){
		if(!TextUtils.isEmpty(url)){
			startSettingService(RemoteService.SET_PREURL, url);
		}
	}
	
	public static void setAppKey(String appkey){
		if(!TextUtils.isEmpty(appkey)){
			EventManager.appkey = appkey;
			startSettingService(RemoteService.SET_APP_KEY, appkey);
		}
	}
	
	public static void setDebugMode(boolean isdebug) {
		Ln.DebugMode = isdebug;
		Random random = new Random();
		int i = Math.abs(random.nextInt())%2;
		if(i == 0){
			 Ln.d("Analysis SDK DebugMode is Open !", "Dragon be here!");
			 Ln.d("Analysis SDK ==>> ", "     ┏┓　　　┏┓      ");
			 Ln.d("Analysis SDK ==>> ", "   ┏┛┻━━━┛┻┓ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　　　　　　　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　　　━　　　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　┳┛　┗┳　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　　　　　　　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　　　┻　　　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┃　　　　　　　┃ ");
			 Ln.d("Analysis SDK ==>> ", "   ┗━┓　　　┏━┛ ");
			 Ln.d("Analysis SDK ==>> ", "       ┃　　　┃                   神兽保佑,代码无BUG！                         ");
			 Ln.d("Analysis SDK ==>> ", "       ┃　　　┃        Code is far away from bug with the animal protecting   ");           
			 Ln.d("Analysis SDK ==>> ", "       ┃　　　┗━━━┓       ");
			 Ln.d("Analysis SDK ==>> ", "       ┃　　　　　　　┣┓  ");
			 Ln.d("Analysis SDK ==>> ", "       ┃　　　　　　　┏┛  ");
			 Ln.d("Analysis SDK ==>> ", "       ┗┓┓┏━┳┓┏┛       ");
			 Ln.d("Analysis SDK ==>> ", "         ┃┫┫　┃┫┫            ");
			 Ln.d("Analysis SDK ==>> ", "         ┗┻┛　┗┻┛            ");
		}else{
			Ln.d("Analysis SDK", "      ┏┓      ┏┓         ");
			Ln.d("Analysis SDK", "　　┏┛┻━━━┛┻┓       ");
			Ln.d("Analysis SDK", "　　┃　　　　　　　┃ 　    ");
			Ln.d("Analysis SDK", "　　┃　　　━　　　┃       ");
			Ln.d("Analysis SDK", "　  ┃  ████━████  ┃       ");
			Ln.d("Analysis SDK", "　　┃　　　　　　　┃       ");
			Ln.d("Analysis SDK", "　　┃　　　┻　　　┃       ");
			Ln.d("Analysis SDK", "　　┃　　　　　　　┃       ");
			Ln.d("Analysis SDK", "　　┗━┓　　　┏━┛       ");
			Ln.d("Analysis SDK", "　　　　┃　　　┃　　　     ");
			Ln.d("Analysis SDK", "　　　　┃　　　┃           ");
			Ln.d("Analysis SDK", "　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting");
			Ln.d("Analysis SDK", "　　　　┃　　　┃    　　神兽保佑,代码无bug ");
			Ln.d("Analysis SDK", "　　　　┃　　　┃　　　　      ");
			Ln.d("Analysis SDK", "　　　　┃      ┗━━━┓      ");
			Ln.d("Analysis SDK", "　　　　┃              ┣┓ ");
			Ln.d("Analysis SDK", "　　　　┃              ┏┛ ");
			Ln.d("Analysis SDK", "　　　　┗┓┓┏━┳┓┏┛   ");
			Ln.d("Analysis SDK", "　　　　　┃┫┫　┃┫┫     ");
			Ln.d("Analysis SDK", "　　　　　┗┻┛　┗┻┛     ");
		}

		startSettingService(RemoteService.SET_ISDEBUG, String.valueOf(isdebug));
	}
	
	public static String getAppKey(){
		if(TextUtils.isEmpty(appkey)){
			appkey = deviceHelper.getAppKey();
		}
		return appkey;
	}

	public static void setChannel(String channel){
		startSettingService(RemoteService.SET_CHANNEL, channel);
	}
	
	public static void setSessionContinueMillis(long interval){
		sessionContinueMillis = interval;
	}

	/** post errors' log
	 * 
	 * @param context
	 * @param error
	 */
	public static void onError(Context context, String error) {
		init(context);
		startLogService(MessageUtils.ERROR_DATA, getErrorJSONObject(error));
	}
	
	/**
	 * set error listener
	 * @param context
	 */
	public static void onError(Context context) {
		init(context);
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(context);
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}

	public static void onEventBegin(Context context, String event_id) {
		init(context);
		eventDurationMap.put(event_id, System.currentTimeMillis());
	}
	
	public static void onEventBegin(Context context, String event_id, String label) {
		init(context);
		eventLabelMap.put(event_id, label);
		eventDurationMap.put(event_id, System.currentTimeMillis());
	}

	public static long onEventEnd(Context context, String event_id) {
		init(context);
		if(eventDurationMap.containsKey(event_id)){
			long start = eventDurationMap.remove(event_id);
			long duration = System.currentTimeMillis() - start;
			return duration;
		}else{
			Ln.e("error : onEventEnd ===>>> ", "do not call onEventBegin, duration is 0");
			return 0;
		}

	}
	
	public static long onEventEnd(Context context, String event_id, String label) {
		init(context);
		if(eventDurationMap.containsKey(event_id) && eventLabelMap.containsKey(event_id)){
			String mLabel = eventLabelMap.remove(event_id);
			if (!mLabel.equals(label)) {
				Ln.e("error : onEventEnd ===>>> ", "the param of label is not equal");
				return 0;
			}
			long start = eventDurationMap.remove(event_id);
			long duration = System.currentTimeMillis() - start;
			return duration;
		}else{
			Ln.e("error : onEventEnd ===>>> ", "do not call onEventBegin or label is not equal");
			return 0;
		}

	}
	
	public static void setAutoLocation(boolean isLocation) {
		startSettingService(RemoteService.SET_LOCATION, String.valueOf(isLocation));
	}
	
	public static void onPageStart(String pageName) {
		if (context == null){
			Ln.e("Context is null", "call onResume() to initsdk");
			return;
		}
		//TODO umeng 只统计页面（是否应该删除activityTrack）,需验证
		current_activity = pageName;
		pageDurationMap.put(pageName, System.currentTimeMillis());
		if(context != null){
			onResume(context, null, null);
		}
	}

	public static void onPageEnd(String pageName) {
		if (context == null){
			Ln.e("Context is null", "call onResume() to initsdk");
			return;
		}
		if(pageDurationMap.containsKey(pageName)){
			long pageStart = pageDurationMap.remove(pageName);
			if(pageStart == 0){
				Ln.e("error : onPageEnd ===>>> ", "do not call onPageStart or the param of pageName is not equal");
				return;
			}else{
				if(context != null){
					onPause(context);
					current_activity = null;
				}
			}
		}else{
			Ln.e("error : onPageEnd ===>>> ", "do not call onPageStart or the param of pageName is not equal");	
		}
	}

	private static synchronized void isCreateNewSessionID() {
		try {
			if (session_id == null) {
				generateSeesion();
				return;
			}

			long currenttime = System.currentTimeMillis();
			long session_save_time = dbHelper.getSessionTime();

			if (currenttime - session_save_time > sessionContinueMillis) {
				generateSeesion();
			}
		} catch (Exception e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager", "isCreateNewSessionID", e);
		}

	}

	/**
	 * create sessionID
	 */
	private static String generateSeesion() {
		dbHelper.setAppStartDate();
		String sessionId = "";
		String str = getAppKey();
		if (str != null) {
			str = str + deviceHelper.getTime() + deviceHelper.getDeviceID();
			sessionId = deviceHelper.md5(str);
			dbHelper.setSessionTime();
			dbHelper.setSessionID(sessionId);
			session_id = sessionId;
			Ln.i("MobclickAgent: ", "Start new session :"+session_id);
			
			// post device and launch data
			startLogService(MessageUtils.LAUNCH_DATA, getLaunchJSONObject());

			return sessionId;
		} else {
			Ln.e("MobclickAgent", "protocol Header need Appkey or Device ID ,Please check AndroidManifest.xml ");
		}
		return sessionId;
	}

	/**
	 * post app's pause log
	 */
	public static void onPause(Context context) {
		init(context);
		//TODO umeng 自动统计activity页面时长（是否应该删除activityTrack）
		dbHelper.setSessionTime();

		end_date = deviceHelper.getTime();
		end = Long.valueOf(System.currentTimeMillis());
		duration = end - start + "";

		if(!TextUtils.isEmpty(current_activity)){
			startLogService(MessageUtils.PAGE_DATA, getPauseJSONObject());
		}
	}
	
	/**
	 * post app's resume log
	 */
	public static void onResume(Context context,String appkey, String channel) {
		init(context);
		if(!TextUtils.isEmpty(appkey)){
			setAppKey(appkey);
		}
		if(!TextUtils.isEmpty(channel)){
			setChannel(channel);
		}

		start_date = deviceHelper.getTime();
		start = Long.valueOf(System.currentTimeMillis());
		
		isCreateNewSessionID();
		if (activityTrack){
			current_activity = deviceHelper.getActivityName();
		}
	}

	/** post launch data to server */
//	public static void postLaunchDatas(Context context) {
//		init(context);
//		startLogService(MessageUtils.LAUNCH_DATA, getLaunchJSONObject());
//	}

	/** post event info */
	public static void onEvent(Context context, PostEvent event) {
		init(context);
		//TODO 
//		if (!event.verification()) {
//			Log.w("MobclickAgent", "Illegal value of acc in postEventInfo");
//			return;
//		}
		if(event.getStringMap() != null){
			startLogService(MessageUtils.HASH_EVENT_DATA, event.eventToJOSNObj());
		}else{
			startLogService(MessageUtils.EVENT_DATA, event.eventToJOSNObj());
		}

	}
	
	/** post event info */
	public static void onEventDuration(Context context, PostEvent event) {
		init(context);
		if (event.getDuration() == 0) {
			Ln.e("onEventDuration", "onEventDuration the duration is 0");
			return;
		}
		if(event.getStringMap() != null){
			startLogService(MessageUtils.HASH_EVENT_DATA, event.eventToJOSNObj());
		}else{
			startLogService(MessageUtils.EVENT_DATA, event.eventToJOSNObj());
		}

	}

	private static synchronized void isServiceConnect(Context context) {
		Ln.e("isServiceConnect ==>>", "bindService");
		if(context != null && aidlService == null){
			Intent service = new Intent(context, RemoteService.class);
			service.setAction("cn.sharesdk.server.AIDLService");
			context.startService(service);
			context.bindService(service, connection, Context.BIND_AUTO_CREATE);
		}
	}
	
	/** Dose update the application or not , the method is kept to use in the future
	 * install app need this permission - <android.permission.INSTALL_PACKAGES/>
	 * */
	public static void isUpdate(Context context, UpdateListener listener) {
		init(context);	
		try {
			if(aidlService != null) {
				aidlService.updateApk();
			}else{
				cacheEventList.add(new AIDLCacheEvent(EventType.UPDATE_APK));
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "isUpdate == ", e);
		}
	}
	
	public static void downloadApk(Context context, String url, String apk){
		init(context);
		//TODO
		try {
			if (aidlService != null) {
				aidlService.downloadApk(url, apk);
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "downloadApk == ", e);
		}
	}
	
	/** upload all log */
	public static void uploadLog(Context context) {
		init(context);
		try {
			if(aidlService != null) {
				aidlService.uploadLog();
			}else{
				cacheEventList.add(new AIDLCacheEvent(EventType.UPLOAD_LOG));
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "uploadLog == ", e);
		}
	}
	
	/**
	 * send feedback to server
	 * @param context
	 * @param jsonString
	 */
	public static void sendFeedback(Context context, String feedback) {
		init(context);
		if (TextUtils.isEmpty(feedback)) {
			Ln.e("sendFeedback", "the content of feedback is empty");
			return;
		}
		startLogService(MessageUtils.CONTENT_DATA, getFBJSON(feedback));
	}

	public static void sendFeedbackContact(Context context, String name, String phone){
		init(context);
		if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone)) {
			Ln.e("sendFeedback", "the phone and name in the feedback is empty");
			return;
		}
		startLogService(MessageUtils.CONTACT_DATA, getFBContackJSON(name, phone));
	}
	
	/**Save and send log to server on the service*/
	public static void startLogService(String action, JSONObject jsonObject){	
		try {
			if(jsonObject == null){
				return;
			} 
			String json = jsonObject.toString();
			if(TextUtils.isEmpty(json)){
				return;
			}
			if (aidlService != null) {
				aidlService.saveLog(action, json);
			}else{
				cacheEventList.add(new AIDLCacheEvent(EventType.SAVELOG, action, json));
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "startLogService == ", e);
		}
	}
	
	/**Save and send log to server on the service*/
	private static void startSettingService(String action, String extraValue){		
		try {
			if(aidlService != null){
				aidlService.setting(action, extraValue);
			}else{
				settingEventList.add(new AIDLCacheEvent(EventType.SETTING, action, extraValue));
				isServiceConnect(context);
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "startSettingService == ", e);
		}
	}
	
	/**
	 * update the config from the server
	 */
	public static void updateOnlineConfig(final Context context){
		init(context);
		try {
			if (aidlService != null) {
				aidlService.updateConfig();
			}else{
				cacheEventList.add(new AIDLCacheEvent(EventType.UPDATE_ONLINE_CONFIG));			
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "updateOnlineConfig == ", e);
		}
	}
	
	public static void getFeedbackFromServer(Context context, int page, int size){
		init(context);
		if(size == 0){
			Ln.e("error when get feedback message from server == ", "params' value of page or size is 0");
			return;
		}		
		try {
			if (aidlService != null) {
				aidlService.getfeedback(String.valueOf(page), String.valueOf(size));
			} else{
				cacheEventList.add(new AIDLCacheEvent(EventType.GET_FEEDBACK, String.valueOf(page), String.valueOf(size)));			
			}
		} catch (RemoteException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getFeedbackFromServer == ", e);
		}
	}

	/** get json of launch msg */
	private static JSONObject getLaunchJSONObject() {
		JSONObject launchData = new JSONObject();
		try {
			String lastExitDate = dbHelper.getAppExitDate();
			String startDate = dbHelper.getAppStartDate();
			Ln.e("getLaunchJSONObject startDate ==>> lastExitDate", startDate+"=="+lastExitDate);
			if(lastExitDate.equals("0")){
				lastExitDate = startDate;
			}
			launchData.put("create_date", startDate);
			launchData.put("last_end_date", lastExitDate);
			launchData.put("session_id", session_id);
		} catch (JSONException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getLaunchJSONObject == ", e);
		}
		return launchData;
	}

	/** get json of error msg */
	private static JSONObject getErrorJSONObject(String error) {
		if(TextUtils.isEmpty(error)){
			return null;
		}
		JSONObject errorData = new JSONObject();
		try {
			String headstring = error;
			if (error.contains("Caused by:")) {
				String ssString = error.substring(error.indexOf("Caused by:"));
				String[] ss = ssString.split("\n\t");
				if (ss.length >= 1)
					headstring = ss[0];
			}

			errorData.put("session_id", session_id);
			errorData.put("create_date", deviceHelper.getTime());
			errorData.put("page", deviceHelper.getActivityName());
			errorData.put("error_log", headstring);
			errorData.put("stack_trace", error);
		} catch (JSONException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getErrorJSONObject == ", e);
		}
		return errorData;
	}

	/** get json of activity onPause msg */
	private static JSONObject getPauseJSONObject() {
		JSONObject pauseData = new JSONObject();
		try {
			pauseData.put("session_id", session_id);
			pauseData.put("start_date", start_date);
			pauseData.put("end_date", end_date);
			pauseData.put("page", current_activity);
			pauseData.put("from_page", last_activity);
			pauseData.put("duration", duration);
			//save the last activity name
			last_activity = current_activity;
		} catch (JSONException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getPauseJSONObject == ", e);
		}
		return pauseData;
	}
	
	@SuppressLint("DefaultLocale")
	public static JSONObject getFBJSON(String content) {
		JSONObject object = new JSONObject();
		try {
			object.put("content", content);
			object.put("content_id", getReplyID());
			String type = String.valueOf(Reply.TYPE.FEEDBACK);
			object.put("type", type.toLowerCase());
			object.put("create_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
		} catch (JSONException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getFBJSON == ", e);
		}
		return object;
	}
	
	public static JSONObject getFBContackJSON(String name, String phone) {
		JSONObject object = new JSONObject();
		try {
			object.put("username", name);
			object.put("contact", phone);
		} catch (JSONException e) {
			EventManager.onError(context, e.getMessage());
			Ln.e("EventManager == ", "getFBContackJSON == ", e);
		}
		return object;
	}
	
	private static String getReplyID() {
		StringBuilder builder = new StringBuilder();
		builder.append("FB");
		builder.append(System.currentTimeMillis());
		builder.append(String.valueOf((int)(1000.0D + Math.random() * 9000.0D)));
		return builder.toString();
	}

}
