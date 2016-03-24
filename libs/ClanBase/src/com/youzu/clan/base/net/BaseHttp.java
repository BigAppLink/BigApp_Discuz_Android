package com.youzu.clan.base.net;

import android.content.Context;
import android.text.TextUtils;

import com.kit.interfaces.IEqual;
import com.kit.utils.ListUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.HttpUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.exception.HttpException;
import com.youzu.android.framework.http.ResponseInfo;
import com.youzu.android.framework.http.callback.RequestCallBack;
import com.youzu.android.framework.http.client.HttpRequest.HttpMethod;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.common.ErrorCode;
import com.youzu.clan.base.json.VariablesJson;
import com.youzu.clan.base.util.ClanBaseUtils;

import org.apache.http.NameValuePair;

public class BaseHttp {

    /**
     * Http Get
     *
     * @param url
     * @param params
     * @param callback
     */
    public static <T> void get(String url, ClanHttpParams params, final HttpCallback<T> callback) {
        async(HttpMethod.GET, url, params, callback);
    }

    /**
     * Http Post
     *
     * @param url
     * @param params
     * @param callback
     */
    public static <T> void post(String url, ClanHttpParams params, final HttpCallback<T> callback) {
        async(HttpMethod.POST, url, params, callback);
    }

    public static <T> T getSync(String url, ClanHttpParams params, Class<T> clz) {
        return sync(HttpMethod.GET, url, params, clz);
    }

    public static <T> T postSync(String url, ClanHttpParams params, Class<T> clz) {
        return sync(HttpMethod.POST, url, params, clz);
    }

    /**
     * 同步Http请求
     *
     * @param method
     * @param url
     * @param params
     * @param clz
     * @return
     */
    public static <T> T sync(HttpMethod method, String url, ClanHttpParams params, final Class<T> clz) {
        try {
            //            ZogUtils.printLogg(BaseHttp.class, "params.getTimeout():" + params.getTimeout());

            HttpUtils httpUtils = new HttpUtils(params.getTimeout());
            httpUtils.configCurrentHttpCacheExpiry(params.getCacheTime());
            httpUtils.configCacheMode(params.getCacheMode());


//            httpUtils.configTimeout(params.getTimeout());
            Context context = params.getContext();
            if (context != null) {
                httpUtils.configCookieStore(CookieManager.getInstance().getCookieStore(context));
//                ClanBaseUtils.printCookieStore(CookieManager.getInstance().getCookieStore(context));
            }

            ZogUtils.printError(BaseHttp.class, "BaseHttp sync " + url + params);

            String response = httpUtils.sendSync(method, url, params);
            if (!TextUtils.isEmpty(response) && clz != String.class) {
//                ZogUtils.printLog(BaseHttp.class, response);
                VariablesJson variables = null;

                if (params.getContext() != null && response != null && !StringUtils.isEmptyOrNullOrNullStr(response)) {
                    variables = ClanBaseUtils.getVariablesJson(response);
                    if (variables != null) {
                        ClanBaseUtils.saveCommonData(params.getContext(), variables);

//                        Message message = variables.getMessage();
//                        if (message != null) {
//                            ToastUtils.mkLongTimeToast(params.getContext(), message.getMessagestr());
//                        }
                    }
                }

                T t = JsonUtils.parseObject(response, clz);

                if (t != null) {
                    return t;
                }
            } else if (clz == String.class) {
                return (T) response;
            }

            return null;
        } catch (Exception e) {
            ZogUtils.showException(e);
//            ToastUtils.mkToast(params.getContext(), "网络异常，请检查你的网络", 1000);
            ZogUtils.printError(BaseHttp.class, "BaseHttp sync 网络异常，请检查你的网络");
//            ZogUtils.printError(BaseHttp.class, "BaseHttp sync:" + e.getMessage());

        }
        return null;
    }


    /**
     * 异步Http请求
     *
     * @param method
     * @param url
     * @param params
     * @param callback
     */
    public static <T> void async(HttpMethod method, String url, final ClanHttpParams params, final HttpCallback<T> callback) {
//        ZogUtils.printLogg(BaseHttp.class, "params.getTimeout():" + params.getTimeout());

        HttpUtils httpUtils = new HttpUtils(params.getTimeout());
        httpUtils.configCurrentHttpCacheExpiry(params.getCacheTime());
        httpUtils.configCacheMode(params.getCacheMode());

//        httpUtils.configTimeout(params.getTimeout());
        Context context = params.getContext();
        if (context != null) {
            httpUtils.configCookieStore(CookieManager.getInstance().getCookieStore(context));
//            ClanBaseUtils.printCookieStore(CookieManager.getInstance().getCookieStore(context));

        }

        ZogUtils.printError(BaseHttp.class, "BaseHttp async " + url + params);

        httpUtils.send(method, url, params, new RequestCallBack<String>() {
            private T mResult;
            VariablesJson variables;

            @Override
            public void onStart() {
                super.onStart();
                callback.onstart(params.getContext());
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (callback == null)
                    return;

                if (params.getContext() != null && responseInfo != null && !StringUtils.isEmptyOrNullOrNullStr(responseInfo.result)) {
                    variables = ClanBaseUtils.getVariablesJson(responseInfo.result);
                    if (variables != null) {
                        ClanBaseUtils.saveCommonData(params.getContext(), variables);
                    }
                }

                if (mResult != null) {
                    callback.onSuccess(params.getContext(), mResult);
                } else {
                    callback.onFailed(params.getContext(), ErrorCode.ERROR_NOT_JSON, "not_json");
                }
            }

            @SuppressWarnings("rawtypes")
            @Override
            public boolean doInThread(final ResponseInfo<String> responseInfo) {

                ZogUtils.printLog(BaseHttp.class,"#module:" + getModule(params) +"# responseInfo.result:" + responseInfo.result);

                Class clz = (Class) callback.getClz();
                if (clz != Void.class) {
                    try {
                        mResult = JsonUtils.parseObject(responseInfo.result, callback.getClz());
                    } catch (Exception e) {
                        ZogUtils.printError(BaseHttp.class, "#ERROR module:"+getModule(params) + "# BaseHttp async doInThread jsonParseError");
                        ZogUtils.showException(e);

                    }
                } else {
                    mResult = (T) new Void();
                }

                if (callback != null) {
                    callback.onSuccessInThread(params.getContext(), mResult);
                }

                return true;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                super.onFailure(error, msg);

                ZogUtils.printError(BaseHttp.class, JsonUtils.toJSONString(error));

                ZogUtils.printError(BaseHttp.class, "module:" + getModule(params) + " BaseHttp async onFailure:" + error.getMessage());
                callback.onFailed(params.getContext(), error.getExceptionCode(), error.getMessage());
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                callback.onProgress(params.getContext(), total, current);
            }

        });
    }


    public static String getModule(ClanHttpParams params) {
        NameValuePair module = ListUtils.find(params.getQueryStringParams(), new IEqual<NameValuePair>() {
            @Override
            public boolean equal(NameValuePair o) {
                if ("module".equals(o.getName()))
                    return true;

                return false;
            }
        });

        if (module != null) {
            return module.getValue();
        }

        return "";
    }


}
