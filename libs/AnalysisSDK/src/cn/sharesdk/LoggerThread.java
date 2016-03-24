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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.content.Context;
import android.os.HandlerThread;
import cn.sharesdk.analysis.model.PostEvent;
import cn.sharesdk.update.UpdateListener;
import cn.sharesdk.utils.UIHandler;

public class LoggerThread extends HandlerThread{
	
	private static LoggerThread loggerThread;
	
	private LoggerThread() {
		super("ShareSDK Statistics");
		start();
	}

	public static synchronized LoggerThread getInstance() {
		if (loggerThread == null) {
			loggerThread = new LoggerThread();
		}
		return loggerThread;
	}

	public void onError(Context context) {
		EventManager.onError(context);
	}

	public void onEventDuration(Context context, PostEvent event) {
		EventManager.onEventDuration(context, event);
	}

	public void onPause(Context context) {
		UIHandler.prepare();
		EventManager.onPause(context);
	}
	
	public void onResume(Context context) {
		UIHandler.prepare();
		EventManager.onResume(context, null, null);
	}
	
	public void setAppKey(String appkey){
		EventManager.setAppKey(appkey);
	}
	
	public void setChannel(String channel){
		EventManager.setChannel(channel);
	}
	
	public void onResume(Context context,String appkey, String channel) {
		EventManager.onResume(context, appkey, channel);
	}
	
	public void isUpdate(Context context, UpdateListener updateListener) {
		UIHandler.prepare();
		EventManager.isUpdate(context, updateListener);
	}
	
	public void onError(Context context, String error) {
		EventManager.onError(context, error);
	}

	public void onEvent(Context context, PostEvent event) {
		EventManager.onEvent(context, event);
	}

	public void onPageStart(String activityName) {
		EventManager.onPageStart(activityName);
	}

	public void onPageEnd(String activityName) {
		EventManager.onPageEnd(activityName);

	}
	
    public void setSessionContinueMillis(long interval){
    	EventManager.setSessionContinueMillis(interval);
    }

	public void onEventBegin(Context context, String event_id) {
		EventManager.onEventBegin(context, event_id);

	}
	
	public void onEventBegin(Context context, String event_id, String label) {
		EventManager.onEventBegin(context, event_id, label);

	}

	public long onEventEnd(Context context, String event_id) {
		return EventManager.onEventEnd(context, event_id);

	}
	
	public long onEventEnd(Context context, String event_id, String label) {
		return EventManager.onEventEnd(context, event_id, label);

	}

	/**post log delay millis*/
	public void uploadLogDelay(Context context, int delayMillis) {
		EventManager.uploadLog(context);
	}

	public void reportError(Context context, Throwable e) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		e.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		onError(context, error);
	}
	
	public void setBaseURL(String url) {
    	EventManager.setBaseURL(url);
    }

	public void setAutoLocation(boolean autoLocation) {
	    EventManager.setAutoLocation(autoLocation);
	}
	
	public void openActivityDurationTrack(boolean activityTrack){
		EventManager.openActivityDurationTrack(activityTrack);
	}
	
	public void setDebugMode(boolean isdebug) {
		EventManager.setDebugMode(isdebug);
	}
	
	public void updateOnlineConfig(Context context){
		EventManager.updateOnlineConfig(context);
	}
	
	public void sendFeedback(Context context, String content){
		EventManager.sendFeedback(context, content);
	}
	
	public void sendFeedbackContact(Context context, String name, String contact){
		EventManager.sendFeedbackContact(context, name, contact);
	}
	
	public void getFeedbackFromServer(Context context, int page, int size){
		EventManager.getFeedbackFromServer(context , page, size);
	}
	
	public void downloadApk(Context context, String url, String apk){
		EventManager.downloadApk(context, url, apk);
		//TODO
	}
}