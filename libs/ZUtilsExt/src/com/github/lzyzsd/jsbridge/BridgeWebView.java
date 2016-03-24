package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.kit.utils.ZogUtils;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge {

	private final String TAG = "BridgeWebView";

	long uniqueId = 0;
	BridgeWebViewClient bridgeWebViewClient;

	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BridgeWebView(Context context) {
		super(context);
		init();
	}

	/**
	 * 
	 * @param handler
	 *            default handler,handle messages send by js without assigned handler name,
     *            if js message has handler name, it will be handled by named handlers registered by native
	 */
	public void setDefaultHandler(BridgeHandler handler) {
		bridgeWebViewClient.defaultHandler = handler;
	}

    private void init() {
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
	}


	public void setBridgeWebViewClient(BridgeWebViewClient bridgeWebViewClient) {
		this.bridgeWebViewClient = bridgeWebViewClient;
		this.setWebViewClient(bridgeWebViewClient);

	}

	@Override
	public void send(String data) {
		send(data, null);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(null, data, responseCallback);
	}

	private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			bridgeWebViewClient.responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		bridgeWebViewClient.queueMessage(m);
	}



	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		this.loadUrl(jsUrl);
		bridgeWebViewClient.responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);

	}

	/**
	 * register handler,so that javascript can call it
	 * 
	 * @param handlerName
	 * @param handler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
			bridgeWebViewClient.messageHandlers.put(handlerName, handler);
		}
	}

	/**
	 * call javascript registered handler
	 *
     * @param handlerName
	 * @param data
	 * @param callBack
	 */
	public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
	}
}
