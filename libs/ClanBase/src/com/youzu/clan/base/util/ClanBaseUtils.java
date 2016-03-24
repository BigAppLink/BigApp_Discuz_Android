package com.youzu.clan.base.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kit.sharedpreferences.SharedPreferencesUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.base.common.ErrorTag;
import com.youzu.clan.base.common.Key;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.VariablesJson;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.net.BaseHttp;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhao on 15/5/11.
 */
public class ClanBaseUtils {


    /**
     * 返回结果出错的时候调用
     *
     * @param responseStr
     */
    public static Message dealFail(Context context, String responseStr) {

        Message message = new Message();
        message.setMessageval(ErrorTag.ZHAO_ERROR_SHOW);

        String msg = "";

        if (StringUtils.isEmptyOrNullOrNullStr(responseStr)) {
            msg = "返回结果为空";
        } else {
            responseStr = responseStr.toLowerCase();
            if (responseStr.contains("java.net")
                    || responseStr.contains("org.apache.http.conn")
                    || responseStr.contains("httphostconnectexception")
                    || responseStr.contains("internal server error")
                    || responseStr.contains("forbidden")) {
                msg = "网络异常，请检查您的网络";
            } else if (responseStr.contains("mobile_is_closed")) {
                msg = "掌上论坛插件关闭，请开启";
            } else if (responseStr.contains("module_not_exists")) {
                msg = "调用接口发生错误，请检查插件是否正确安装";
            } else if (responseStr.contains("timeout")
                    || responseStr.contains("sockettimeout")) {
                msg = "网络请求超时";
            } else if (responseStr.contains("ioexception")) {
                msg = "读取不到数据，或因权限问题";
//                message.setMessageval(ErrorTag.ZHAO_ERROR_NO_SHOW);
            } else if (responseStr.contains("not_json")) {
                msg = "返回数据格式错误";
                message.setMessageval(ErrorTag.ZHAO_ERROR_NO_SHOW);
            } else if (responseStr.contains(ErrorTag.NO_SHOW)) {
                msg = ErrorTag.NO_SHOW;
                message.setMessageval(ErrorTag.NO_SHOW);
            }
        }

        message.setMessagestr(msg);
        return message;

    }

    public static Map<String, String> getAllowUpload(CheckPostJson checkPostJson) {

        if (checkPostJson != null
                && checkPostJson.getVariables() != null
                && checkPostJson.getVariables() != null
                && checkPostJson.getVariables().getAllowperm() != null
                && checkPostJson.getVariables().getAllowperm().getAllowUpload() != null) {

            return checkPostJson.getVariables().getAllowperm().getAllowUpload();
        }
        return null;
    }


    public static void printCookieStore(CookieStore cookieStore) {
        List<Cookie> cookies = cookieStore.getCookies();
        Log.e("APP", "========================================== start cookies.size:" + cookies.size());
        if (!cookies.isEmpty()) {
            for (int i = 0; i < cookies.size(); i++) {
                Cookie ck = cookies.get(i);
                String ckstr = ck.getName() + "=" + ck.getValue() + ";"
                        + "expiry=" + ck.getExpiryDate() + ";"
                        + "domain=" + ck.getDomain() + ";"
                        + "path=/";

                Log.v("APP", "cookieStr:" + ckstr);
            }
        }
        Log.e("APP", "========================================== end cookies.size:" + cookies.size());
    }


    public static VariablesJson getVariablesJson(String response) {
        VariablesJson variables = null;

        try {
            variables = JsonUtils.parseObject(response, VariablesJson.class);
        } catch (Exception e) {
            ZogUtils.printError(BaseHttp.class, "variables Json parse error!");
        }

        return variables;
    }

    public static void saveCommonData(Context context, VariablesJson variables) {
//		ZogUtils.printLog(ClanBaseUtils.class, "response:" + response);
        try {
            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(
                    context, Key.FILE_PREFERENCES,
                    Activity.MODE_PRIVATE);

            if (sharedPreferencesUtils != null && variables != null
                    && variables.getVariables() != null) {
                if (variables
                        .getVariables().getFormhash() != null) {
                    sharedPreferencesUtils.saveSharedPreferences("formhash", variables
                            .getVariables().getFormhash());
                }
                sharedPreferencesUtils.saveSharedPreferences("cookiepre", variables
                        .getVariables().getCookiepre());
            }
        } catch (Exception e) {
            ZogUtils.printError(BaseHttp.class, "WARNING!!!!!!!! saveCommonData() ERROR!");
        }

    }

    public static String getFormhash(Context context) {
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(
                context, Key.FILE_PREFERENCES,
                Activity.MODE_PRIVATE);
        String formhash = sharedPreferencesUtils
                .loadStringSharedPreference("formhash");
        ZogUtils.printLog(ClanBaseUtils.class, "formhash:" + formhash);
        return formhash;
    }


}
