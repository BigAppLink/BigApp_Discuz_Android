package com.kit.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonUtils {

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

    // 解析处理过的json字符串，需调用getJosnStr()，以取得处理过的json字符串
    public static JSONArray str2JSONArray(String str) {
        if (StringUtils.isNullOrEmpty(str))
            return null;

        String jsonStr = "{jsonStr:" + str + "}";

        System.out.println(jsonStr);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            jsonArray = jsonObject.getJSONArray("jsonStr");
            // jsonObject = jsonObject.getJSONObject("jsonStr");

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e("error", "JsonUtils.str2JSONArray() have something wrong...");

        }
        return jsonArray;
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

    public static String formatJsonStr(String inputString) {

        if (TextUtils.isEmpty(inputString))
            return "";
        // String dataStr = inputString;
        // System.out.println(dataStr);
        String paramsStr = inputString;

        paramsStr = paramsStr.replaceAll("\\\\", "");
        // System.out.println("UMailMessageData1: " + paramsStr);
        paramsStr = paramsStr.replaceAll("\"\\{", "{");
        // System.out.println("UMailMessageData2: " + paramsStr);
        paramsStr = paramsStr.replaceAll("\\}\"", "}");

        paramsStr = paramsStr.replaceAll("\\\"", "\"");

        String jsonStr = paramsStr;

        return jsonStr;

    }

    public static void replaceStringValue(JSONObject jobj, String keyName, String replaceValue) throws JSONException {
        String value;
        Iterator it = jobj.keys();

        while (it.hasNext()) {

            String key = it.next().toString();

            // 将所有的空串去掉
            if (jobj.getString(key) == null) {

                continue;
            }

            if (keyName.equals(key)) {      //试剂类型
                ZogUtils.printLog(JsonUtils.class, "img_url got!!!!!!!!!!!!!!");
                jobj.put(keyName, replaceValue);
            }
        }
//        try {
//            params.put(SDK.IMG_URL_TAG, URLEncoder.encode(params.getString(SDK.IMG_URL_TAG), SDK.DEFAULT_CODE));
//        } catch (Exception e) {
//        }


    }

    /**
     * 把对象按照Json格式输出
     *
     * @param obj
     */
    public static void printAsJson(Object obj) {
        Gson gson = new Gson();
        ZogUtils.printLog(JsonUtils.class, gson.toJson(obj));
    }


    public static boolean isJSON(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.getJSONObject(jsonString);
        } catch (Exception e) {

            return false;
        }
        return true;
    }


}
