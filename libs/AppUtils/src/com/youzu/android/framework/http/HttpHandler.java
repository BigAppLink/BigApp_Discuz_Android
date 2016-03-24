package com.youzu.android.framework.http;

/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.os.SystemClock;
import android.util.Log;

import com.youzu.android.framework.HttpUtils;
import com.youzu.android.framework.exception.HttpException;
import com.youzu.android.framework.http.callback.DefaultHttpRedirectHandler;
import com.youzu.android.framework.http.callback.FileDownloadHandler;
import com.youzu.android.framework.http.callback.HttpRedirectHandler;
import com.youzu.android.framework.http.callback.RequestCallBack;
import com.youzu.android.framework.http.callback.RequestCallBackHandler;
import com.youzu.android.framework.http.callback.StringDownloadHandler;
import com.youzu.android.framework.task.PriorityAsyncTask;
import com.youzu.android.framework.util.OtherUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;


public class HttpHandler<T> extends PriorityAsyncTask<Object, Object, Void> implements RequestCallBackHandler {

    private final AbstractHttpClient client;
    private final HttpContext context;

    private HttpRedirectHandler httpRedirectHandler;

    public void setHttpRedirectHandler(HttpRedirectHandler httpRedirectHandler) {
        if (httpRedirectHandler != null) {
            this.httpRedirectHandler = httpRedirectHandler;
        }
    }

    private String requestUrl;
    private String requestMethod;
    private HttpRequestBase request;
    private boolean isUploading = true;
    private RequestCallBack<T> callback;

    private int retriedCount = 0;
    private String fileSavePath = null;
    private boolean isDownloadingFile = false;
    private boolean autoResume = false; // Whether the downloading could continue from the point of interruption.
    private boolean autoRename = false; // Whether rename the file by response header info when the download completely.
    private String charset; // The default charset of response header info.

    public HttpHandler(AbstractHttpClient client, HttpContext context, String charset, RequestCallBack<T> callback) {
        this.client = client;
        this.context = context;
        this.callback = callback;
        this.charset = charset;
        this.client.setRedirectHandler(notUseApacheRedirectHandler);
    }

    private State state = State.WAITING;

    public State getState() {
        return state;
    }

    private long expiry = HttpCache.getDefaultExpiryTime();

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public void setRequestCallBack(RequestCallBack<T> callback) {
        this.callback = callback;
    }

    public RequestCallBack<T> getRequestCallBack() {
        return this.callback;
    }

    private ResponseInfo<T> sendRequestForCache(HttpRequestBase request) {
        requestMethod = request.getMethod();

        boolean isEnableCache = com.youzu.android.framework.HttpUtils.sHttpCache.isEnabled(requestMethod);

//        Log.e("APP", "requestMethod：" + requestMethod + " isEnableCache:" + isEnableCache);

        if (isEnableCache) {
            String result = com.youzu.android.framework.HttpUtils.sHttpCache.get(requestUrl + request.toString());
            if (result != null) {
                return new ResponseInfo<T>(null, (T) result, true);
            }
        }
        return null;
    }

    // 执行请求
    @SuppressWarnings("unchecked")
    private ResponseInfo<T> sendRequest(HttpRequestBase request) throws HttpException {

        HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
        while (true) {

            if (autoResume && isDownloadingFile) {
                File downloadFile = new File(fileSavePath);
                long fileLen = 0;
                if (downloadFile.isFile() && downloadFile.exists()) {
                    fileLen = downloadFile.length();
                }
                if (fileLen > 0) {
                    request.setHeader("RANGE", "bytes=" + fileLen + "-");
                }
            }

            boolean retry = true;
            IOException exception = null;
            try {
//                ResponseInfo<T> responseInfo = null;
//                if (!isCancelled()) {
//                    HttpResponse response = client.execute(request, context);
//                    responseInfo = handleResponse(response);
////                    CookieStore store = client.getCookieStore();
//                }
//                return responseInfo;

                requestMethod = request.getMethod();
                if (HttpUtils.sHttpCache.isEnabled(requestMethod)) {
                    String result = HttpUtils.sHttpCache.get(requestUrl);
                    if (result != null) {
                        return new ResponseInfo<T>(null, (T) result, true);
                    }
                }
                ResponseInfo<T> responseInfo = null;
                if (!isCancelled()) {
                    HttpResponse response = client.execute(request, context);
                    responseInfo = handleResponse(response);
                }
                return responseInfo;
            } catch (UnknownHostException e) {
                Log.e("APP", "HttpHandler sendRequest UnknownHostException:" + e.getMessage());

                exception = e;
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (IOException e) {
                Log.e("APP", "HttpHandler sendRequest IOException: " + e.toString());

                exception = e;
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (NullPointerException e) {
                Log.e("APP", "HttpHandler sendRequest NullPointerException:" + e.getMessage());

                exception = new IOException(e.getMessage());
                exception.initCause(e);
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            } catch (HttpException e) {
                Log.e("APP", "HttpHandler sendRequest HttpException:" + e.getMessage());

                throw e;
            } catch (Throwable e) {
                Log.e("APP", "HttpHandler sendRequest Throwable:" + e.getMessage());

                exception = new IOException(e.getMessage());
                exception.initCause(e);
                retry = retryHandler.retryRequest(exception, ++retriedCount, context);
            }

            Log.e("APP", "retry:" + retry);

            if (!retry) {
                HttpException httpException = new HttpException(exception);
                Log.e("APP", "HttpHandler sendRequest HttpException:" + httpException.getMessage());
//                callback.onFailure(httpException,httpException.getMessage());
                throw httpException;
            }
        }
    }

    @Override
    protected Void doInBackground(Object... params) {
        if (this.state == State.CANCELLED || params == null || params.length == 0) return null;

        if (params.length > 3) {
            fileSavePath = String.valueOf(params[1]);
            isDownloadingFile = fileSavePath != null;
            autoResume = (Boolean) params[2];
            autoRename = (Boolean) params[3];
        }

        try {
            if (this.state == State.CANCELLED) return null;
            // init request & requestUrl
            request = (HttpRequestBase) params[0];
            requestUrl = request.getURI().toString();
            if (callback != null) {
                callback.setRequestUrl(requestUrl);
            }

            this.publishProgress(UPDATE_START);

            lastUpdateTime = SystemClock.uptimeMillis();

            ResponseInfo<T> cacheInfo = sendRequestForCache(request);
            if (cacheInfo != null && (cacheInfo.result instanceof String) && callback.doInThread(cacheInfo)) {
                this.publishProgress(UPDATE_SUCCESS, cacheInfo);
                if (!HttpUtils.sHttpCache.needRefresh()) {
                    return null;
                }
            }

            ResponseInfo<T> netInfo = sendRequest(request);
            if (netInfo != null && (netInfo.result instanceof String)) {
                boolean isSuccess = callback.doInThread(netInfo);
                if (isSuccess) {
                    HttpUtils.sHttpCache.put(requestUrl + request.toString(), (String) netInfo.result, expiry);
                    publishProgress(UPDATE_SUCCESS, netInfo);
                } else {
                    publishProgress(UPDATE_FAILURE, null, "");
                }
                return null;
            }

            this.publishProgress(UPDATE_SUCCESS, netInfo);
            return null;
        } catch (HttpException e) {
            this.publishProgress(UPDATE_FAILURE, e, e.getMessage());
        }

        return null;
    }

    private final static int UPDATE_START = 1;
    private final static int UPDATE_LOADING = 2;
    private final static int UPDATE_FAILURE = 3;
    private final static int UPDATE_SUCCESS = 4;

    @Override
    @SuppressWarnings("unchecked")
    protected void onProgressUpdate(Object... values) {
        if (this.state == State.CANCELLED || values == null || values.length == 0 || callback == null)
            return;
        switch ((Integer) values[0]) {
            case UPDATE_START:
                this.state = State.STARTED;
                callback.onStart();
                break;
            case UPDATE_LOADING:
                if (values.length != 3) return;
                this.state = State.LOADING;
                callback.onLoading(
                        Long.valueOf(String.valueOf(values[1])),
                        Long.valueOf(String.valueOf(values[2])),
                        isUploading);
                break;
            case UPDATE_FAILURE:
                if (values.length != 3) return;
                this.state = State.FAILURE;
                try {
                    callback.onFailure((HttpException) values[1], (String) values[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new HttpException(), "onProgressUpdate error");
                }
                break;
            case UPDATE_SUCCESS:
                if (values.length != 2) return;
                this.state = State.SUCCESS;
                callback.onSuccess((ResponseInfo<T>) values[1]);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private ResponseInfo<T> handleResponse(HttpResponse response) throws HttpException, IOException {
        if (response == null) {
            throw new HttpException("response is null");
        }
        if (isCancelled()) return null;

        StatusLine status = response.getStatusLine();
        int statusCode = status.getStatusCode();
        if (statusCode < 300) {
            Object result = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                isUploading = false;
                if (isDownloadingFile) {
                    autoResume = autoResume && OtherUtils.isSupportRange(response);
                    String responseFileName = autoRename ? OtherUtils.getFileNameFromHttpResponse(response) : null;
                    FileDownloadHandler downloadHandler = new FileDownloadHandler();
                    result = downloadHandler.handleEntity(entity, this, fileSavePath, autoResume, responseFileName);
                } else {
                    StringDownloadHandler downloadHandler = new StringDownloadHandler();
                    result = downloadHandler.handleEntity(entity, this, charset);
                }
            }
            return new ResponseInfo<T>(response, (T) result, false);
        } else if (statusCode == 301 || statusCode == 302) {
            if (httpRedirectHandler == null) {
                httpRedirectHandler = new DefaultHttpRedirectHandler();
            }
            HttpRequestBase request = httpRedirectHandler.getDirectRequest(response);
            if (request != null) {
                return this.sendRequest(request);
            }
        } else if (statusCode == 416) {
            throw new HttpException(statusCode, "maybe the file has downloaded completely");
        } else {
            throw new HttpException(statusCode, status.getReasonPhrase());
        }
        return null;
    }

    /**
     * cancel request task.
     */
    @Override
    public void cancel() {
        this.state = State.CANCELLED;

        if (request != null && !request.isAborted()) {
            try {
                request.abort();
            } catch (Throwable e) {
            }
        }
        if (!this.isCancelled()) {
            try {
                this.cancel(true);
            } catch (Throwable e) {
            }
        }
        client.getConnectionManager().shutdown();
        if (callback != null) {
            callback.onCancelled();
        }
    }

    private long lastUpdateTime;

    @Override
    public boolean updateProgress(long total, long current, boolean forceUpdateUI) {
        if (callback != null && this.state != State.CANCELLED) {
            if (forceUpdateUI) {
                this.publishProgress(UPDATE_LOADING, total, current);
            } else {
                long currTime = SystemClock.uptimeMillis();
                if (currTime - lastUpdateTime >= callback.getRate()) {
                    lastUpdateTime = currTime;
                    this.publishProgress(UPDATE_LOADING, total, current);
                }
            }
        }
        return this.state != State.CANCELLED;
    }

    public enum State {
        WAITING(0), STARTED(1), LOADING(2), FAILURE(3), CANCELLED(4), SUCCESS(5);
        private int value = 0;

        State(int value) {
            this.value = value;
        }

        public static State valueOf(int value) {
            switch (value) {
                case 0:
                    return WAITING;
                case 1:
                    return STARTED;
                case 2:
                    return LOADING;
                case 3:
                    return FAILURE;
                case 4:
                    return CANCELLED;
                case 5:
                    return SUCCESS;
                default:
                    return FAILURE;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private static final NotUseApacheRedirectHandler notUseApacheRedirectHandler = new NotUseApacheRedirectHandler();

    private static final class NotUseApacheRedirectHandler implements RedirectHandler {
        @Override
        public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
            return false;
        }

        @Override
        public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
            return null;
        }
    }
}
