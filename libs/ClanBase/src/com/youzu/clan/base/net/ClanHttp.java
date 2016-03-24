package com.youzu.clan.base.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.task.PriorityAsyncTask;
import com.youzu.clan.base.R;
import com.youzu.clan.base.callback.FavForumCallback;
import com.youzu.clan.base.callback.FavThreadCallback;
import com.youzu.clan.base.callback.ForumnavCallback;
import com.youzu.clan.base.callback.HotThreadCallback;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.LoginCallback;
import com.youzu.clan.base.callback.MypmCallback;
import com.youzu.clan.base.callback.ProfileSeeCallback;
import com.youzu.clan.base.callback.RequestCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.callback.YZLoginCallback;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.config.AppBaseConfig;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.AddForumJson;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.LoginJson;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.json.YZLoginJson;
import com.youzu.clan.base.json.login.YZLoginParams;
import com.youzu.clan.base.json.model.FileInfo;
import com.youzu.clan.base.util.ClanBaseUtils;

public class ClanHttp {

    /**
     * 登录接口 如果第一次调用返回的不是json格式数据，需要再调用一次 登录成功需要调用一次获取用户信息的接口
     *
     * @param context
     * @param username
     * @param password
     * @param callback
     */
    public static void login(final Context context, String username, String password, String questionId, String answer, final LoginCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
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

        PriorityAsyncTask<ClanHttpParams, Void, LoginJson> task = new PriorityAsyncTask<ClanHttpParams, Void, LoginJson>() {
            private String uid;
            private ProfileJson profileJson;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onstart(context);
            }

            @Override
            protected LoginJson doInBackground(ClanHttpParams... params) {
                LoginJson loginJson = null;

                try {
                    try {
                        loginJson = BaseHttp.getSync(Url.DOMAIN, params[0], LoginJson.class);
                    } catch (Exception e) {
                        Log.e("APP", e.getMessage());

                    }
                    if (loginJson != null && loginJson.getErrorCode() == ErrorCode.REQUEST_OK && loginJson.getVariables() != null) {
                        ClanHttpParams profileParams = new ClanHttpParams(context);
                        profileParams.addQueryStringParameter("module", "profile");
//                        profileParams.addQueryStringParameter("uid", uid);
                        profileJson = BaseHttp.getSync(Url.DOMAIN, profileParams, ProfileJson.class);
                        uid = profileJson.getVariables().getMemberUid();
                        if (StringUtils.isEmptyOrNullOrNullStr(uid) && profileJson.getData() != null) {
                            uid = profileJson.getData().getUid();
                        }
                    }
//				if (loginJson == null) {
//					loginJson = BaseHttp.getSync(Url.DOMAIN, params[0], LoginJson.class);
//				}
//				if (loginJson != null && loginJson.getVariables() != null && TextUtils.isEmpty(uid = loginJson.getVariables().getMemberUid())) {
//					ClanHttpParams profileParams = new ClanHttpParams(context);
//					profileParams.addQueryStringParameter("module", "profile");
//					profileParams.addQueryStringParameter("uid", uid);
//					profileJson = BaseHttp.getSync(Url.DOMAIN, profileParams, ProfileJson.class);
//				}
                } catch (Exception e) {
                    ZogUtils.printError(ClanHttp.class, "login doInBackground error");
                    e.printStackTrace();
                }
                return loginJson;
            }

            @Override
            protected void onPostExecute(LoginJson loginJson) {
                super.onPostExecute(loginJson);
                String requestFailed = context.getString(R.string.request_failed);
                if (loginJson == null) {
                    callback.onFailed(context, ErrorCode.REQUEST_FAILED, requestFailed);
                    return;
                }

                if (loginJson != null && loginJson.getErrorCode() == ErrorCode.REQUEST_OK) {
                    if (profileJson != null && !StringUtils.isEmptyOrNullOrNullStr(uid) && !uid.equals("0"))
                        callback.onSuccess(context,uid, profileJson);
                    else {
                        String errorMsg = context.getString(R.string.profile_failed);
                        callback.onFailed(context, ErrorCode.LOGIN_FAILED, errorMsg);
                    }
                    return;
                } else {
                    String errorMsg = loginJson.getErrorMsg();
                    callback.onFailed(context, ErrorCode.LOGIN_FAILED, !StringUtils.isEmptyOrNullOrNullStr(errorMsg) ? errorMsg : requestFailed);
                }
            }
        };
        task.execute(params);

    }

    /**
     * 游族登录
     *
     * @param context
     * @param username
     * @param password
     * @param callback
     */

    public static void yzLogin(final Context context, String username, String password, final YZLoginCallback callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "login3body");
        params.addQueryStringParameter("username", username);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("is_first", "1");

        PriorityAsyncTask<ClanHttpParams, Void, ProfileJson> task = new PriorityAsyncTask<ClanHttpParams, Void, ProfileJson>() {
            private YZLoginParams loginParams;
            private boolean isSuccess;
            private String errorMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onstart(context);
            }

            @Override
            protected ProfileJson doInBackground(ClanHttpParams... params) {
                YZLoginJson json = BaseHttp.postSync(Url.DOMAIN, params[0], YZLoginJson.class);
                if (json == null) {
                    return null;
                }
                if (json.isSuccess()) {
                    isSuccess = true;
                    ClanHttpParams profileParams = new ClanHttpParams(context);
                    profileParams.addQueryStringParameter("module", "profile");
                    return BaseHttp.getSync(Url.DOMAIN, profileParams, ProfileJson.class);
                } else if (json.needNickname()) {
                    loginParams = json.getParams();
                } else {
                    errorMsg = json.getErrorMsg();
                }

                return null;
            }

            @Override
            protected void onPostExecute(ProfileJson result) {
                super.onPostExecute(result);
                if (isSuccess) {
                    callback.onSuccess(context, result);
                } else if (loginParams != null) {
                    callback.onFailed(loginParams);
                } else {
                    if (TextUtils.isEmpty(errorMsg)) {
                        errorMsg = context.getString(R.string.request_failed);
                    }
                    callback.onFailed(context, -1, errorMsg);
                }
            }

        };
        task.execute(params);
    }

    /**
     * 游族登录
     *
     * @param context
     * @param loginParams
     * @param nickname
     * @param callback
     */
    public static void setNickname(final Context context, YZLoginParams loginParams, String nickname, final YZLoginCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "login3body");
        params.addQueryStringParameter("state", loginParams.getState());
        params.addQueryStringParameter("code", loginParams.getCode());
        params.addQueryStringParameter("nickname", nickname);
        params.addQueryStringParameter("is_first", "0");

        PriorityAsyncTask<ClanHttpParams, Void, ProfileJson> task = new PriorityAsyncTask<ClanHttpParams, Void, ProfileJson>() {
            private YZLoginParams loginParams;
            private boolean isSuccess;
            private String errorMsg;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onstart(context);
            }

            @Override
            protected ProfileJson doInBackground(ClanHttpParams... params) {
                YZLoginJson json = BaseHttp.postSync(Url.DOMAIN, params[0], YZLoginJson.class);
                if (json == null) {
                    return null;
                }
                if (json.isSuccess()) {
                    isSuccess = true;
                    ClanHttpParams profileParams = new ClanHttpParams(context);
                    profileParams.addQueryStringParameter("module", "profile");
                    return BaseHttp.getSync(Url.DOMAIN, profileParams, ProfileJson.class);
                } else if (json.needNickname()) {
                    loginParams = json.getParams();
                } else {
                    errorMsg = json.getErrorMsg();
                }

                return null;
            }

            @Override
            protected void onPostExecute(ProfileJson result) {
                super.onPostExecute(result);
                if (isSuccess) {
                    callback.onSuccess(context, result);
                } else if (loginParams != null) {
                    callback.onFailed(loginParams);
                } else {
                    if (TextUtils.isEmpty(errorMsg)) {
                        errorMsg = context.getString(R.string.request_failed);
                    }
                    callback.onFailed(context, -1, errorMsg);
                }
            }

        };
        task.execute(params);
    }

    public static void register(final Context context,
                                String username, String password,
                                String confirmPassword, String email,
                                final HttpCallback callback) {

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "newuser");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("inajax", "1");
        params.addBodyParameter("regsubmit", "yes");
        params.addBodyParameter("un", username);
        params.addBodyParameter("pd", password);
        params.addBodyParameter("pd2", confirmPassword);
        params.addBodyParameter("em", email);
        BaseHttp.post(Url.DOMAIN, params, callback);

//        PriorityAsyncTask<ClanHttpParams, Void, ProfileJson> task = new PriorityAsyncTask<ClanHttpParams, Void, ProfileJson>() {
//            private boolean isSuccess;
//            private String errorMsg;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                callback.onstart();
//            }
//
//            @Override
//            protected ProfileJson doInBackground(ClanHttpParams... params) {
//                BaseJson json = BaseHttp.postSync(Url.DOMAIN, params[0], BaseJson.class);
//                if (json == null) {
//                    return null;
//                }
//                final Message message = json.getMessage();
//                if (message == null) {
//                    return null;
//                }
//
//                if (MessageVal.REGISTER_SUCCEED.equals(message.getMessageval())) {
//                    isSuccess = true;
//                    ClanHttpParams profileParams = new ClanHttpParams(context);
//                    profileParams.addQueryStringParameter("module", "profile");
//                    return BaseHttp.getSync(Url.DOMAIN, profileParams, ProfileJson.class);
//                } else {
//                    errorMsg = message.getMessagestr();
//                }
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(ProfileJson result) {
//                super.onPostExecute(result);
//                if (isSuccess && result != null) {
//                    callback.onSuccess(result);
//                } else if (isSuccess && result == null) {
//                    callback.onFailed(Error.PROFILE_FAILED, context.getString(R.string.profile_failed));
//                } else if (!TextUtils.isEmpty(errorMsg)) {
//                    callback.onFailed(Error.REGISTER_FAILED, errorMsg);
//                } else {
//                    callback.onFailed(Error.REQUEST_FAILED, context.getString(R.string.request_failed));
//                }
//            }
//        };
//        task.execute(params);

    }

    /**
     * 热门板块
     *
     * @param callback
     */
    public static void getHotForum(RequestCallback callback) {

    }

    /**
     * 所有版块
     *
     * @param callback
     */
    public static void getForumnav(Context context, ForumnavCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "forumnav");
        BaseHttp.get(Url.DOMAIN, params, callback);

    }

    /**
     * 热点 热门主题
     *
     * @param callback
     */
    public static void getHotThread(Context context, HotThreadCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "hotthread");
        BaseHttp.get(Url.DOMAIN, params, callback);

    }

    /**
     * 收藏帖子
     *
     * @param context
     * @param callback
     */
    public static void getFavThread(Context context, int page, FavThreadCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "myfavthread");
        params.addQueryStringParameter("page", String.valueOf(page));
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 收藏版块
     *
     * @param context
     * @param callback
     */
    public static void getFavForum(Context context, int page, FavForumCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "myfavforum");
        params.addQueryStringParameter("page", String.valueOf(page));
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 消息
     *
     * @param context
     * @param callback
     */
    public static void getMypm(Context context, MypmCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "mypm");
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 用户信息
     *
     * @param context
     * @param callback
     */
    public static void getProfile(Context context, JSONCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "profile");

        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 获取指定用户用户信息
     *
     * @param context
     * @param userId
     * @param callback
     */
    public static void getProfile(Context context, String userId, JSONCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "profile");
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("uid", userId);
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 用户信息
     *
     * @param context
     * @param userId
     * @param callback
     */
    public static void getProfile(Context context, String userId, StringCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "profile");
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("uid", userId);
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 用户信息
     *
     * @param context
     * @param userId
     * @param callback
     */
    public static void getProfile(Context context, String userId, HttpCallback<ProfileJson> callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "profile");
        if (!TextUtils.isEmpty(userId)) {
            params.addQueryStringParameter("uid", userId);
        }
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 查看用户信息
     *
     * @param context
     * @param userId
     * @param callback
     */
    public static void getProfile(Context context, String userId, ProfileSeeCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "profile");
        params.addQueryStringParameter("uid", userId);
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 用户信息
     *
     * @param context
     * @param file
     * @param callback
     */
    public static void uploadAvatar(Context context, FileInfo file, JSONCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "uploadavatar");
        params.addQueryStringParameter("ac", "avatar");
//        params.addQueryStringParameter("iyzmobile", "1");

        params.addBodyParameter("Filedata", file.getFile());

        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 添加收藏
     *
     * @param context
     * @param callback
     */
    public static void addFavForum(Context context, String forumId, HttpCallback<AddForumJson> callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "favforum");
        params.addQueryStringParameter("infloat", "yes");
        params.addQueryStringParameter("handlekey", "a_favorite");
        params.addQueryStringParameter("inajax", "1");
        params.addQueryStringParameter("ajaxtarget", "fwin_content_a_favorite");
        params.addQueryStringParameter("id", forumId);

        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addQueryStringParameter("formhash", ClanBaseUtils.getFormhash(context));
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

    /**
     * 删除收藏
     *
     * @param context
     * @param callback
     */
    public static void deleteFavForum(Context context, String favId, HttpCallback<BaseJson> callback) {
        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("module", "delfav");


        params.addBodyParameter("delfavorite", "true");
        params.addBodyParameter("favorite[]", favId);


        if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(context)))
            params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(context));

        BaseHttp.post(Url.DOMAIN, params, callback);
    }

    /**
     * 注销
     *
     * @param context
     * @param callback
     */
    public static void logout(Context context, StringCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "login");
        params.addQueryStringParameter("action", "logout");
        BaseHttp.get(Url.DOMAIN, params, callback);
    }


    /**
     * 获取首页
     *
     * @param context
     * @param cacheMode
     * @param callback
     */
    public static void getHomeConfig(Context context, int cacheMode, JSONCallback callback) {
        final ClanHttpParams params = new ClanHttpParams(context);

        params.setCacheMode(cacheMode);
        params.setCacheTime(AppBaseConfig.CACHE_NET_TIME);

        params.addQueryStringParameter("module", "myhome");
        params.addQueryStringParameter("iyzmobile", "1");
        BaseHttp.get(Url.DOMAIN, params, callback);
    }

}
