package com.kit.utils;

import android.text.TextUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtils {
    // public static final Log logger = LogFactory.getLog("httpclient");
    public static final String CODE = "UTF-8";

    public static String httpReader(String url, String code) {
        ZogUtils.printLog(HttpClientUtils.class, "GetPage:" + url);

        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        String result = null;
        try {
            client.executeMethod(method);
            int status = method.getStatusCode();
            if (status == HttpStatus.SC_OK) {
                result = method.getResponseBodyAsString();
            } else {
                ZogUtils.printLog(HttpClientUtils.class, "Method failed: "
                        + method.getStatusLine());
            }
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            ZogUtils.printLog(HttpClientUtils.class,
                    "Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            // 发生网络异常
            ZogUtils.printLog(HttpClientUtils.class, "发生网络异常！");
            e.printStackTrace();
        } finally {
            // 释放连接
            if (method != null)
                method.releaseConnection();
            method = null;
            client = null;
        }
        return result;
    }

    public static String httpGet(String url, JSONObject params) {
        String responseStr = null;
        try {

            // HttpRequest httpRequest=new

            DefaultHttpClient client = new DefaultHttpClient();
            // 设置超时
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    10000);
            // HttpPost request = new HttpPost(url);
            //
            // request.setEntity(new StringEntity(params.toString(), CODE));
            //
            // request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            // request.setHeader("Accept", "json");
            // HttpResponse response = client.execute(request);

            HttpGet request = new HttpGet(url);

            if (params != null) {
                System.out.println("toGetString(params):" + toGetString(params));
                request = new HttpGet(url + "?" + toGetString(params));
            }
            request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            request.setHeader("Accept", "json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpGet(String serverUrl, String method,
                                 JSONObject params) {
        String responseStr = null;
        try {

            // HttpRequest httpRequest=new
            String url = serverUrl + "/" + method;
            DefaultHttpClient client = new DefaultHttpClient();

            // HttpPost request = new HttpPost(url);
            //
            // request.setEntity(new StringEntity(params.toString(), CODE));
            //
            // request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            // request.setHeader("Accept", "json");
            // HttpResponse response = client.execute(request);

            System.out.println("toGetString(params):" + toGetString(params));
            HttpGet request = new HttpGet(url + "?" + toGetString(params));

            request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            request.setHeader("Accept", "json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpPost(String serverUrl, String method,
                                  Map paramMap, String code) {
        String url = serverUrl + "/" + method;
        ZogUtils.printLog(HttpClientUtils.class, "request url: " + url);
        String content = null;
        if (url == null || url.trim().length() == 0 || paramMap == null
                || paramMap.isEmpty())
            return null;
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        postMethod.setRequestHeader("Content-Type", "text/json;charset=utf-8");

        Iterator it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next() + "";
            Object o = paramMap.get(key);
            if (o != null && o instanceof String) {
                postMethod.addParameter(new NameValuePair(key, o.toString()));
            }

            System.out.println(key + ":" + o);
            postMethod.addParameter(new NameValuePair(key, o.toString()));

        }
        try {
            httpClient.executeMethod(postMethod);
            ZogUtils.printLog(HttpClientUtils.class, postMethod.getStatusLine()
                    + "");
            content = new String(postMethod.getResponseBody(), code);

        } catch (Exception e) {
            ZogUtils.printLog(HttpClientUtils.class, "time out");
            e.printStackTrace();
        } finally {
            if (postMethod != null)
                postMethod.releaseConnection();
            postMethod = null;
            httpClient = null;
        }
        return content;

    }

    public static String httpPost(String serverUrl, String method,
                                  JSONObject params, String code) {
        String url = serverUrl + "/" + method;
        ZogUtils.printLog(HttpClientUtils.class, "request url: " + url);
        String content = null;
        if (url == null || url.trim().length() == 0)
            return null;
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        postMethod.setRequestHeader("Content-Type",
                "application/json;charset=utf-8");
        if (params != null) {
            Iterator it = params.keys();
            while (it.hasNext()) {
                String key = it.next() + "";
                try {
                    Object o = params.get(key);
                    if (o != null && o instanceof String) {
                        System.out.println(key + ":" + o);
                        postMethod.addParameter(new NameValuePair(key, o
                                .toString()));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        try {
            httpClient.executeMethod(postMethod);
            ZogUtils.printLog(HttpClientUtils.class, postMethod.getStatusLine()
                    + "");
            content = new String(postMethod.getResponseBody(), code);

        } catch (Exception e) {
            ZogUtils.printLog(HttpClientUtils.class, "time out");
            e.printStackTrace();
        } finally {
            if (postMethod != null)
                postMethod.releaseConnection();
            postMethod = null;
            httpClient = null;
        }
        return content;

    }

    public static String httpPostWithJsonParams(String serverUrl,
                                                String method, JSONObject params, String code) {
        String responseStr = null;

        try {
            String url = "";
            if (TextUtils.isEmpty(method)) {
                url = serverUrl;
            } else {
                url = serverUrl + "/" + method + "/"
                        + URLEncoder.encode(params.toString(), code);
            }

            System.out.println("url:" + url + " params.toString():"
                    + params.toString());
            DefaultHttpClient httpclient = new DefaultHttpClient();

            // url with the post data
            HttpPost httpost = new HttpPost(url);
            // StringEntity se;
            //
            // if (params == null) {
            // se = new StringEntity("");
            // } else {
            // se = new StringEntity();
            // }
            // // sets the post request as the resulting string
            // httpost.setEntity(se);

            httpost.setHeader("Accept", "application/json");
            httpost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpost);

            responseStr = EntityUtils.toString(response.getEntity(), code);
            // System.out.println("responseStr:" + responseStr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseStr;

    }

    public static String httpPostWithJsonParams(String serverUrl,
                                                String method, JSONObject params) {
        return HttpClientUtils.httpPostWithJsonParams(serverUrl, method,
                params, CODE);
    }

    public static String httpPost(String serverUrl, String method,
                                  JSONObject params) {
        return HttpClientUtils.httpPost(serverUrl, method, params, CODE);
    }

    public static String httpPost(String url, Map paramMap) {
        return HttpClientUtils.httpPost(url, paramMap, CODE);
    }

    public static String httpPost(String serverUrl, String method, Map paramMap) {
        return HttpClientUtils.httpPost(serverUrl, method, paramMap, CODE);
    }

    public static String httpPost(String url, Map paramMap, String code) {
        ZogUtils.printLog(HttpClientUtils.class, "request url: " + url);
        String content = null;
        if (url == null || url.trim().length() == 0 || paramMap == null
                || paramMap.isEmpty())
            return null;
        HttpClient httpClient = new HttpClient();
        PostMethod method = new PostMethod(url);

        method.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=utf-8");

        Iterator it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next() + "";
            Object o = paramMap.get(key);
            if (o != null && o instanceof String) {
                method.addParameter(new NameValuePair(key, o.toString()));
            }

            System.out.println(key + ":" + o);
            method.addParameter(new NameValuePair(key, o.toString()));

        }
        try {
            httpClient.executeMethod(method);
            ZogUtils.printLog(HttpClientUtils.class, method.getStatusLine()
                    + "");
            content = new String(method.getResponseBody(), code);

        } catch (Exception e) {
            ZogUtils.printLog(HttpClientUtils.class, "time out");
            e.printStackTrace();
        } finally {
            if (method != null)
                method.releaseConnection();
            method = null;
            httpClient = null;
        }
        return content;

    }

    public static String httpGetWithWCF4Url(String url, JSONObject params) {
        String responseStr = null;
        try {

            // HttpRequest httpRequest=new

            DefaultHttpClient client = new DefaultHttpClient();

            // HttpPost request = new HttpPost(url);
            //
            // request.setEntity(new StringEntity(params.toString(), CODE));
            //
            // request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            // request.setHeader("Accept", "json");
            // HttpResponse response = client.execute(request);

            System.out.println("toGetString(params):" + toGetString(params));
            HttpGet request = new HttpGet(url + "?" + toGetString(params));

            request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            request.setHeader("Accept", "json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpPostWithWCF4Url(String serverUrl, JSONObject params) {
        String responseStr = null;
        try {
            String url = serverUrl;
            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost request = new HttpPost(url);

            System.out.println("toGetString(params):" + toGetString(params));
            request.setEntity(new StringEntity(params.toString(), CODE));

            request.setHeader(HTTP.CONTENT_TYPE, "text/json");
            request.setHeader("Accept", "json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpPostWithWCF(String serverUrl, String method,
                                         JSONObject params) {
        String responseStr = null;
        try {
            String url = serverUrl + method;
            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost request = new HttpPost(url);

            request.setEntity(new StringEntity(params.toString(), CODE));
            request.setHeader(HTTP.CONTENT_TYPE, "text/json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpPostWithWCF4ObjectParams(String serverUrl,
                                                      String method, JSONObject params) {

        // 此方法为了参数中有对象的方法

        // String result =
        // "{\"userID\":\"t000521\",\"data\":{\"UMailMessageID\":0,\"SendDate\":\"/Date(1354646352187+0800)/\",\"MailByte\":0,\"ParentMailId\":0,\"Priority\":10}}";
        String responseStr = null;
        try {
            String url = serverUrl + method;
            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost request = new HttpPost(url);

            String paramsStr = params.toString();

            paramsStr = paramsStr.replaceAll("\\\\", "");
            // System.out.println("UMailMessageData1: " + paramsStr);
            paramsStr = paramsStr.replaceAll("\"\\{", "{");
            // System.out.println("UMailMessageData2: " + paramsStr);
            paramsStr = paramsStr.replaceAll("\\}\"", "}");
            // System.out.println("UMailMessageData3: " + paramsStr);

            System.out.println(paramsStr);
            request.setEntity(new StringEntity(paramsStr, CODE));
            request.setHeader(HTTP.CONTENT_TYPE, "text/json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String httpPostWithWCF(String serverUrl, String method,
                                         String params) {
        String responseStr = null;
        try {
            String url = serverUrl + method;
            DefaultHttpClient client = new DefaultHttpClient();

            HttpPost request = new HttpPost(url);

            request.setEntity(new StringEntity(params));
            request.setHeader(HTTP.CONTENT_TYPE, "text/json");

            HttpResponse response = client.execute(request);

            responseStr = EntityUtils.toString(response.getEntity(), CODE);
            // System.out.println("responseStr:" + responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String toGetString(JSONObject params) {

        String dataStr = params.toString();

        dataStr = dataStr.replaceAll("\"", "");
        dataStr = dataStr.replaceAll("\\{", "");
        dataStr = dataStr.replaceAll("\\}", "");
        dataStr = dataStr.replaceAll("\\:", "=");
        dataStr = dataStr.replaceAll("\\,", "&");

        System.out.println(dataStr);

        return dataStr;

    }
}
