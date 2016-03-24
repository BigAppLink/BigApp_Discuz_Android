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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.sharesdk.EventManager;
import cn.sharesdk.db.MessageUtils;

public class CrashHandler implements UncaughtExceptionHandler {
	private static CrashHandler myCrashHandler;
	private Context context;

	private PreferencesHelper dbHelper;
	private DeviceHelper deviceHelper;

	private CrashHandler() {
	}

	public static synchronized CrashHandler getInstance() {
		if (myCrashHandler != null) {
			return myCrashHandler;
		} else {
			myCrashHandler = new CrashHandler();
			return myCrashHandler;
		}
	}

	public void init(Context mContext) {
		this.context = mContext;
		dbHelper = PreferencesHelper.getInstance(context);
		deviceHelper = DeviceHelper.getInstance(context);
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {
		arg1.printStackTrace();
		
		String headstring = "";
		String errorinfo = getErrorInfo(arg1);
		if (errorinfo.contains("Caused by:")) {
			String ssString = errorinfo.substring(errorinfo.indexOf("Caused by:"));
			String[] ss = ssString.split("\n\t");
			if (ss.length >= 1)
				headstring = ss[0];
		}
		// Saving error log, next launch to post log
		JSONObject errorObject = getErrorJSONObject(headstring, errorinfo);
		EventManager.startLogService(MessageUtils.ERROR_DATA, errorObject);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private JSONObject getErrorJSONObject(String errorHead, String errorStack) {
		JSONObject errorInfo = new JSONObject();
		try {
			errorInfo.put("session_id", dbHelper.getSessionID());
			errorInfo.put("create_date", deviceHelper.getTime());
			errorInfo.put("page", deviceHelper.getActivityName());
			errorInfo.put("error_log", errorHead);
			errorInfo.put("stack_trace", errorStack);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errorInfo;
	}

	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

}