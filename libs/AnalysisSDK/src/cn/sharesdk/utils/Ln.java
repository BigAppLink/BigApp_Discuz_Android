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

public class Ln {
	public static boolean DebugMode = false;

	public static void i(String paramString1, String paramString2) {
		if (DebugMode)
			android.util.Log.i(paramString1, paramString2);
	}

	public static void i(String paramString1, String paramString2, Exception paramException) {
		if (DebugMode)
			android.util.Log.i(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
	}

	public static void e(String paramString1, String paramString2) {
		if (DebugMode)
			android.util.Log.e(paramString1, paramString2);
	}

	public static void e(String paramString1, String paramString2, Exception paramException) {
		if (DebugMode) {
			android.util.Log.e(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
			StackTraceElement[] arrayOfStackTraceElement1 = paramException.getStackTrace();
			for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1)
				android.util.Log.e(paramString1, "        at\t " + localStackTraceElement.toString());
		}
	}

	public static void d(String paramString1, String paramString2) {
		if (DebugMode)
			android.util.Log.d(paramString1, paramString2);
	}

	public static void d(String paramString1, String paramString2, Exception paramException) {
		if (DebugMode)
			android.util.Log.d(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
	}

	public static void v(String paramString1, String paramString2) {
		if (DebugMode)
			android.util.Log.v(paramString1, paramString2);
	}

	public static void v(String paramString1, String paramString2, Exception paramException) {
		if (DebugMode)
			android.util.Log.v(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
	}

	public static void w(String paramString1, String paramString2) {
		if (DebugMode)
			android.util.Log.w(paramString1, paramString2);
	}

	public static void w(String paramString1, String paramString2, Exception paramException) {
		if (DebugMode) {
			android.util.Log.w(paramString1, paramException.toString() + ":  [" + paramString2 + "]");
			StackTraceElement[] arrayOfStackTraceElement1 = paramException.getStackTrace();
			for (StackTraceElement localStackTraceElement : arrayOfStackTraceElement1)
				android.util.Log.w(paramString1, "        at\t " + localStackTraceElement.toString());
		}
	}
}
