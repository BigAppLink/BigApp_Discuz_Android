package com.youzu.clan.base.net;

import android.content.Context;
import android.util.Log;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.ProfileCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.profile.ProfileVariables;

/**
 * Created by Zhao on 15/8/19.
 */
public class ProfileHttp {


    /**
     * @param context
     * @param uid
     * @param checkType check=0,点击签到 check=1,点击检查是否以签到
     * @param callback
     */
    public static void checkIn(final Context context, String uid, String checkType, final HttpCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "checkin");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("check", checkType);
        params.addQueryStringParameter("uid", uid);

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    public static void profile(final Context context, final HttpCallback callback) {

        Log.e("APP", "login login login login");

//        printCookieStore(cookieStore);

//        webView.loadUrl(AppConfig.LOGIN_URL);


        ClanHttpParams profileParams = new ClanHttpParams(context);
        profileParams.addQueryStringParameter("module", "profile");
        BaseHttp.get(Url.DOMAIN, profileParams, new ProfileCallback() {
            @Override
            public void onstart(Context cxt) {
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, ProfileJson profileJson) {
                super.onSuccess(ctx, profileJson);
                if (profileJson != null) {

                    ProfileVariables profileVariables = profileJson.getVariables();

                    String uid = null;
                    if (profileVariables != null)
                        uid = profileVariables.getMemberUid();

                    ZogUtils.printError(ProfileHttp.class, "uid::::::" + uid + " profileJson:" + profileJson);
                    if (profileJson != null && !StringUtils.isEmptyOrNullOrNullStr(uid) && !uid.equals("0")) {
                        callback.onSuccess(context, profileJson);
                    }
//                    else {
//                        String errorMsg = context.getString(R.string.profile_failed);
//                        ToastUtils.mkLongTimeToast(WebLoginActivity.this, errorMsg);
//                    }
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
            }
        });

    }
}
