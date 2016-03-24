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
package cn.sharesdk.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Base64;
import cn.sharesdk.analysis.model.PostResult;
import cn.sharesdk.utils.Ln;

public class NetworkHelper {
	
	static boolean debugable = false;//Ln.DebugMode;
	public static PostResult isUpdateAPK(String url, String appkey, String channel){
		PostResult postResult = new PostResult();
		//test
		if(debugable){
			postResult.setSuccess(true);
			JSONObject object = new JSONObject();
			try{
				JSONObject resultObject =new JSONObject();
				resultObject.put("status", 1);
				resultObject.put("path", "http://192.168.9.250/apk/ShareSDK.apk");
				resultObject.put("content", "1、更高 \n2、更快 \n3、更强");
				resultObject.put("version", 21);
				resultObject.put("size", 23);
				resultObject.put("md5", "test123");

				object.put("status", 200);				
				object.putOpt("res", resultObject);
			}catch (Exception e) {
				e.printStackTrace();
			}
			postResult.setResponseMsg(object.toString());
			return postResult;
		}
		Ln.i("apk update  url ===>>", url);
		Ln.i("appkey === channel >>>", appkey +"====="+ channel);
		Ln.i("platform_id ===>>", "1");
		
		if(TextUtils.isEmpty(appkey) || TextUtils.isEmpty(url)){
			postResult.setSuccess(false);
			postResult.setResponseMsg("The request params in the method of isUpdateAPK is error!");
			return postResult;
		}
		
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("appkey", appkey));
		pairs.add(new BasicNameValuePair("channel", channel));
		pairs.add(new BasicNameValuePair("platform_id", "1"));
		
		return httpPost(url, pairs);		
	}
	
	public static PostResult updateConfig(String url, String appkey){
		Ln.i("updateConfig  url ===>> appkey", url + "=====" + appkey);
		return uploadLog(url, null, appkey);
	}

	public static PostResult uploadLog(String url, String data, String appkey) {
		PostResult postResult = new PostResult();
		if (TextUtils.isEmpty(appkey) || TextUtils.isEmpty(url)) {
			postResult.setSuccess(false);
			postResult.setResponseMsg("The request params in the method of uploadLog is error!");
			return postResult;
		}
		Ln.i("uploadLog  url ===>> appkey", url +"==="+ appkey);
		
		Ln.i("postdata before base64gizp", "client_data:" + data);
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("appkey", appkey));
		if (!TextUtils.isEmpty(data)) {
			data = Base64Gzip(data);
			pairs.add(new BasicNameValuePair("m", data));
		}

		return httpPost(url, pairs);
	}

	/**
	 * 获取用户反馈信息列表
	 * @param url
	 * @param deviceID
	 * @param appkey
	 * @param page
	 * @param size
	 * @return
	 */
	public static PostResult getFBMsg(String url, String deviceID, String appkey, String page, String size) {		
		PostResult postResult = new PostResult();
		if(TextUtils.isEmpty(url) || TextUtils.isEmpty(deviceID) || TextUtils.isEmpty(appkey) || TextUtils.isEmpty(page) || TextUtils.isEmpty(size)){
			postResult.setSuccess(false);
			postResult.setResponseMsg("The request params in the method of getFBMsg is error!");
			return postResult;
		}
		Ln.i("apk getFBMsg  url ===>>", url);
		Ln.i("appkey === device_id >>>", appkey +"====="+ deviceID);
		Ln.i("page === size >>>", page +"====="+ size);
		
		try {			
			ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("appkey", appkey));
			pairs.add(new BasicNameValuePair("device_id", deviceID));
			pairs.add(new BasicNameValuePair("page", page));
			pairs.add(new BasicNameValuePair("size", size));
			
			postResult = httpPost(url, pairs);
		} catch (Exception e) {
			Ln.i("NetworkHelper", "=== post Ln ===", e);
		}
		
		return postResult;
	}
	

	public static PostResult httpPost(String url, ArrayList<NameValuePair> pairs) {
		PostResult postResult = new PostResult();
		if(pairs == null || pairs.size() == 0){
			postResult.setSuccess(false);
			postResult.setResponseMsg("post date of the request params is null !");
			return postResult;
		}
		
		try {			
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("charset", HTTP.UTF_8);
			httppost.setHeader("Accept", "application/json,text/x-json,application/jsonrequest,text/json");	
			
			HttpEntity entity = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
			httppost.setEntity(entity);

			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			HttpResponse response = httpclient.execute(httppost);
			int status = response.getStatusLine().getStatusCode();

			String resString = EntityUtils.toString(response.getEntity());
			postResult = parse(status, resString);
		} catch (Exception e) {
			Ln.i("NetworkHelper", "=== post Ln ===", e);
		} 
		
		return postResult;
	}
	
	private static PostResult parse(int status, String response) {
		PostResult message = new PostResult();
		try{
			String returnContent = URLDecoder.decode(response, "utf-8");
			if(TextUtils.isEmpty(returnContent)){
				returnContent = "{}";
				Ln.i("post result is null", "===>>> post result is null");
			}
			Ln.i("post result status == response content ==>>", status + "===" +returnContent);
			switch (status) {
			case 200:
				message.setSuccess(true);
				message.setResponseMsg(returnContent);
				break;
			default:
				message.setSuccess(false);
				message.setResponseMsg(returnContent);
				break;
			}
		}catch (Exception e) {
			Ln.i("Parse postResult", "===exception===", e);
		}
		return message;
	}

	public static String Base64Gzip(String str) {
		ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		String result = null;
		// gzip
		GZIPOutputStream gos;
		try {
			gos = new GZIPOutputStream(baos);
			int count;
			byte data[] = new byte[1024];
			while ((count = bais.read(data, 0, 1024)) != -1) {
				gos.write(data, 0, count);
			}
			gos.finish();
			gos.close();

			byte[] output = baos.toByteArray();
			baos.flush();
			baos.close();
			bais.close();

			result = Base64.encodeToString(output, Base64.NO_WRAP);
		} catch (IOException e) {
			e.printStackTrace();
			Ln.i("NetworkHelper", "Base64Gzip == >>", e);
		}
		return result;
	}

}
