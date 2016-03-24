package com.kit.utils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.kit.config.AppConfig;

public class CopyOfJsonRpcUtils {

	public static URL url;

	// 解析处理过的json字符串，需调用getJosnStr()，以取得处理过的json字符串
	public static JSONObject str2JSONObj(String str) {

		String jsonStr = "{jsonStr:" + str + "}";

		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(jsonStr);

			jsonObject = jsonObject.getJSONObject("jsonStr");

		} catch (JSONException e) {

			Log.e("error", "JsonUtils.resolveJson() have something wrong...");

		}
		return jsonObject;
	}

	// 解析JsonArray,返回ArrayList<JSONObject>
	public static ArrayList<JSONObject> resolveJsonArray(JSONArray jsonArray) {
		ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tempJsonObject = null;
			try {
				tempJsonObject = jsonArray.getJSONObject(i);
				jsonObjectList.add(tempJsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return jsonObjectList;
	}

}
