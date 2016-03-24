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
package cn.sharesdk.analysis;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MobclickAgentJSInterface {
	private Context context;

	@SuppressLint("SetJavaScriptEnabled")
	public MobclickAgentJSInterface(Context context, WebView webView) {
		this.context = context;
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new StatisticsWebClient(null));
	}

	@SuppressLint("SetJavaScriptEnabled")
	public MobclickAgentJSInterface(Context context, WebView webView, WebChromeClient client) {
		this.context = context;
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new StatisticsWebClient(client));
	}

	final class StatisticsWebClient extends WebChromeClient {
		
		WebChromeClient webClient = null;
		public StatisticsWebClient(WebChromeClient client) {
			if (client == null){
				this.webClient = new WebChromeClient();
			} else{
				this.webClient = client;
			}
		}

		@SuppressWarnings("rawtypes")
		public boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
			JSONObject jsonObject = null;
			if ("ekv".equals(message)) {
				try {
					jsonObject = new JSONObject(defaultValue);
					String eventID = String.valueOf(jsonObject.remove("tag"));
					int acc = jsonObject.has("acc") ? Integer.parseInt(jsonObject.remove("acc").toString()) : 1;

					String key = null;
					HashMap<String, String> localHashMap = new HashMap<String, String>();
					Iterator iterator = jsonObject.keys();
					while (iterator.hasNext()) {
						key = String.valueOf(iterator.next());
						localHashMap.put(key, jsonObject.getString(key));
					}
					
					MobclickAgent.onEvent(context, eventID, "", acc, localHashMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ("event".equals(message)) {
				try {
					jsonObject = new JSONObject(defaultValue);
					String label = jsonObject.optString("label");
					String eventID = jsonObject.optString("tag");
					int acc = jsonObject.optInt("acc");
					
					if(TextUtils.isEmpty(eventID)){
						return false;
					}
					if(TextUtils.isEmpty(label) && acc ==0){
						MobclickAgent.onEvent(context, eventID);
					}else if(acc == 0){
						MobclickAgent.onEvent(context, eventID, label);
					}else{
						MobclickAgent.onEvent(context, eventID, label, acc);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				return this.webClient.onJsPrompt(webView, url, message, defaultValue, result);
			}
			result.confirm();
			return true;
		}

		public void onCloseWindow(WebView webView) {
			this.webClient.onCloseWindow(webView);
		}

		public boolean onCreateWindow(WebView webView, boolean isDialog, boolean isUserGesture, Message resultMsg) {
			return this.webClient.onCreateWindow(webView, isDialog, isUserGesture, resultMsg);
		}

		public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
			return this.webClient.onJsAlert(webView, url, message, result);
		}

		public boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
			return this.webClient.onJsBeforeUnload(webView, url, message, result);
		}

		public boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
			return this.webClient.onJsConfirm(webView, url, message, result);
		}

		public void onProgressChanged(WebView webView, int newProgress) {
			this.webClient.onProgressChanged(webView, newProgress);
		}

		public void onReceivedIcon(WebView webView, Bitmap icon) {
			this.webClient.onReceivedIcon(webView, icon);
		}

		public void onReceivedTitle(WebView webView, String title) {
			this.webClient.onReceivedTitle(webView, title);
		}

		public void onRequestFocus(WebView webView) {
			this.webClient.onRequestFocus(webView);
		}
	}
}
