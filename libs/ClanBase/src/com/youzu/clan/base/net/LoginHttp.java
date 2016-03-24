package com.youzu.clan.base.net;

import android.content.Context;

import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.util.ClanBaseUtils;

public class LoginHttp {
//    iyzmobile=1&version=4&module=platform_login&mod=check&platform=wechat&openid=69C35535576E3709BBD62A7B1222984E&token=766DE35509E49C1E5B66BD503798E087


    /**
     * 登录接口 如果第一次调用返回的不是json格式数据，需要再调用一次 登录成功需要调用一次获取用户信息的接口
     *
     * @param context
     * @param username
     * @param password
     * @param callback
     */
    public static void login(final Context context, String username, String password, String questionId, String answer, final HttpCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.setTimeout(60 * 1000);
        params.addQueryStringParameter("module", "login");
        params.addQueryStringParameter("loginsubmit", "yes");
        params.addQueryStringParameter("infloat", "yes");
        params.addQueryStringParameter("lssubmit", "yes");
        params.addQueryStringParameter("inajax", "1");
        params.addQueryStringParameter("fastloginfield", "username");
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("cookietime", "2592000");
        params.addQueryStringParameter("quickforward", "yes");
        params.addQueryStringParameter("handlekey", "ls");

        params.addQueryStringParameter("questionid", questionId);
        params.addQueryStringParameter("answer", answer);

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 第三方登录
     *
     * @param context
     * @param token
     * @param openid
     * @param platform
     * @param callback
     */
    public static void platformLoginCheck(Context context, String token, String openid, String platform, HttpCallback callback) {

        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("hash", formhash);

        params.addQueryStringParameter("openid", openid);
        params.addQueryStringParameter("token", token);
        params.addQueryStringParameter("platform", platform);

        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "platform_login");
        params.addQueryStringParameter("mod", "check");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 绑定老用户
     *
     * @param context
     * @param token
     * @param openid
     * @param platform
     * @param callback
     */
    public static void platformLoginBind(Context context, String token, String openid, String platform, String username, String password, String questionid, String answer, HttpCallback callback) {

        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("hash", formhash);

        params.addQueryStringParameter("openid", openid);
        params.addQueryStringParameter("token", token);
        params.addQueryStringParameter("platform", platform);

        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("questionid", questionid);
        params.addQueryStringParameter("answer", answer);

        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "platform_login");
        params.addQueryStringParameter("mod", "login");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 绑定新用户
     *
     * @param context
     * @param token
     * @param openid
     * @param platform
     * @param username
     * @param password
     * @param email
     * @param callback
     */
    public static void platformNewUser(Context context, String token, String openid, String platform, String username, String password, String email, HttpCallback callback) {

        String formhash = ClanBaseUtils.getFormhash(context);

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("hash", formhash);

        params.addQueryStringParameter("openid", openid);
        params.addQueryStringParameter("token", token);
        params.addQueryStringParameter("platform", platform);

        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("email", email);


        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "platform_login");
        params.addQueryStringParameter("mod", "register");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


}
