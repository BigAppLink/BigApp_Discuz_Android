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
 *
 *	     ┏┓　　　┏┓
 *	┏┛┻━━━┛┻┓
 *	┃　　　　　　　┃ 　
 *	┃　　　━　　　┃
 *	┃　┳┛　┗┳　┃
 *	┃　　　　　　　┃
 *	┃　　　┻　　　┃
 *	┃　　　　　　　┃
 *	┗━┓　　　┏━┛
 *	          ┃　　　┃                        神兽保佑
 *	          ┃　　　┃                       代码无BUG！
 *	          ┃　　　┗━━━┓
 *	          ┃　　　　　　　┣┓
 *	          ┃　　　　　　　┏┛
 *	          ┗┓┓┏━┳┓┏┛
 *	               ┃┫┫　┃┫┫
 *	               ┗┻┛　┗┻┛      
 */

package cn.sharesdk.analysis;

import java.util.HashMap;

import android.content.Context;
import cn.sharesdk.LoggerThread;
import cn.sharesdk.analysis.model.PostEvent;

public class MobclickAgent {

	private static LoggerThread log;
	private static MobclickAgent mobclickAgent = new MobclickAgent();

	private MobclickAgent() {
		log = LoggerThread.getInstance();
	}
	
	 /**
     * set base URL like http://192.168.1.195/statistics.sharesdk.cn/api/index.php
     * 
     * @param url
     */
    public static void setBaseURL(String url) {
    	log.setBaseURL(url);
    }
    
    /**
     * set channel
     * @param channel
     */
    public static void setChannel(String channel){
    	log.setChannel(channel);
    }
    
    /**
     * set appkey
     * @param appKey
     */
    public static void setAppKey(String appKey){
    	log.setAppKey(appKey);
    }
	
	/**set session continue millis ,default value is 30 seconds*/
    public static void setSessionContinueMillis(long interval) {
        if (interval > 0) {
            log.setSessionContinueMillis(interval);
        }

    }
    
	/**
	 * set debug mode 
	 */
	public static void setDebugMode(boolean isdebug) {
		log.setDebugMode(isdebug);
	}
	
	/**
	 * close the activity statistics,
	 * @param activityTrack
	 * true
	 */
	public static void openActivityDurationTrack(boolean activityTrack){
		log.openActivityDurationTrack(activityTrack);
	}
	
	/**
	 * setting the error listener
	 * @param context
	 */
	public static void onError(final Context context) {
		log.onError(context);
	}

	/**
	 * log the error
	 * @param context
	 * @param error
	 */
	public static void onError(final Context context, final String error) {
		log.onError(context, error);
	}
	
	/**
	 * log the error
	 * @param context
	 * @param error
	 */
	public static void reportError(final Context context, final String error){
		log.onError(context, error);
	}
	
	/**
	 * log the error
	 * @param context
	 * @param throwable
	 */
	public static void reportError(final Context context, final Throwable  throwable){
		log.reportError(context, throwable);
	}
	
	/**
	 * log the event
	 * @param context
	 * @param event_id
	 */
	public static void onEvent(final Context context, final String event_id) {
		onEvent(context, event_id, 1);
	}
	
	/**
	 * log the event
	 * @param context
	 * @param event_id
	 * @param acc
	 */
	public static void onEvent(final Context context, final String event_id, final int acc) {
		onEvent(context, event_id, null, acc);
	}
	
	/**
	 * log the event
	 * @param context
	 * @param event_id
	 * @param label
	 */
	public static void onEvent(final Context context, final String event_id, final String label) {
		onEvent(context, event_id, label, 1);
	}
	
	/**
	 * log the event
	 * @param context
	 * @param event_id
	 * @param label
	 * @param acc
	 */
	public static void onEvent(final Context context, final String event_id, final String label, final int acc) {
		onEvent(context, event_id, label, acc, null);
	}

	/**
	 * log the event
	 * @param context
	 * @param event_id
	 * @param stringMap
	 */
	public static void onEvent(final Context context, final String event_id, final HashMap<String, String> stringMap) {
		onEvent(context, event_id, null, 1, stringMap);
	}
	
	/**
	 * log the event
	 * @param context
	 * @param event_id
	 * @param label
	 * @param acc
	 * @param stringMap
	 */
	public static void onEvent(final Context context, final String event_id, final String label, final int acc, final HashMap<String, String> stringMap) {
		log.onEvent(context, new PostEvent(context, event_id, label, acc + "", 0, stringMap));
	}

	/**
	 * Begin to log the duration of event
	 * @param context
	 * @param event_id
	 */
	public static void onEventBegin(final Context context, final String event_id) {
		log.onEventBegin(context, event_id);
	}
	
	/**
	 * End to log the duration of event
	 * @param context
	 * @param event_id
	 */
	public static void onEventEnd(final Context context, final String event_id) {
		onEventDuration(context, event_id, log.onEventEnd(context, event_id));
	}

	/**
	 * Begin to log the duration of event
	 * @param context
	 * @param event_id
	 * @param label
	 */
	public static void onEventBegin(final Context context, final String event_id, final String label) {
		log.onEventBegin(context, event_id, label);
	}
	
	/**
	 * End to log the duration of event
	 * @param context
	 * @param event_id
	 * @param label
	 */
	public static void onEventEnd(final Context context, final String event_id, final String label) {
		onEventDuration(context, event_id, label, log.onEventEnd(context, event_id, label));
	}

	/**
	 * log the duration of event
	 * @param context
	 * @param event_id
	 * @param duration
	 */
	public static void onEventDuration(final Context context, final String event_id, final long duration) {
		onEventDuration(context, event_id, null, duration);
	}
	
	/**
	 * log the duration of event
	 * @param context
	 * @param event_id
	 * @param label
	 * @param duration
	 */
	public static void onEventDuration(final Context context, final String event_id, final String label, final long duration) {
		onEventDuration(context, event_id, label, 1, duration, null);
	}
	/**
	 * log the duration of event
	 * @param context
	 * @param event_id
	 * @param duration
	 * @param stringMap
	 */
	public static void onEventDuration(final Context context, final String event_id, final long duration, final HashMap<String, String> stringMap) {
		onEventDuration(context, event_id, null, 1, duration, stringMap);
	}

	private static void onEventDuration(final Context context, final String event_id, final String label, final int acc, final long duration, final HashMap<String, String> stringMap) {
		log.onEventDuration(context, new PostEvent(context, event_id, label, acc + "", duration, stringMap));
	}

	/**
	 * Start to log the duration of view
	 * @param activityName
	 */
	public static void onPageStart(final String activityName) {
		log.onPageStart(activityName);
	}

	/**
	 * End to log the duration of view
	 * @param activityName
	 */
	public static void onPageEnd(final String activityName) {
		log.onPageEnd(activityName);
	}

	/**
	 * log onPause
	 * @param context
	 */
	public static void onPause(final Context context) {
		log.onPause(context);
	}

	/**
	 * log onResume
	 * @param context
	 */
	public static void onResume(final Context context) {
		log.onResume(context);
	}
	
	/**
	 * Automatic Updates
	 * 
	 * @param context
	 */
//	public static void update(final Context context) {
//		log.isUpdate(context);
//	}
	
	/**
	 * Getting the location of phone or not.
	 * @param autoLocation
	 */
	public static void setAutoLocation(boolean autoLocation) {
	    log.setAutoLocation(autoLocation);
	}
	
	/**
	 * update the config from the server
	 */
	public static void updateOnlineConfig(final Context context){
		log.updateOnlineConfig(context);
	}
	
}
