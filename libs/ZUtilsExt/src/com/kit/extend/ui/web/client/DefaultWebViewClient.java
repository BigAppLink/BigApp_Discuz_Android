package com.kit.extend.ui.web.client;

import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.kit.extend.R;
import com.kit.extend.ui.web.WebFragment;
import com.kit.utils.ToastUtils;
import com.kit.utils.ZogUtils;

public class DefaultWebViewClient extends BridgeWebViewClient {
    private WebFragment webFragment;

    public DefaultWebViewClient( WebFragment webFragment) {
        super(webFragment.getWebView());
        this.webFragment = webFragment;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//        ZogUtils.printError(DefaultWebViewClient.class, "shouldOverrideUrlLoading:" + url);

        if(super.shouldOverrideUrlLoading(view, url)){
            return true;
        }

        webFragment.loadingUrl = url;
        loadingUrl(view, url);

        return true;
    }






    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                // Handle the error
        super.onReceivedError(view,errorCode,description,failingUrl);

        ToastUtils.mkShortTimeToast(view.getContext(), view.getContext().getString(R.string.load_error));
    }

    @Override
    public void onReceivedSslError(WebView view,
                                   SslErrorHandler handler,
                                   android.net.http.SslError error) {
        super.onReceivedSslError(view, handler, error);

        // 重写此方法可以让webview处理https请求 
        handler.proceed();
    }


    /**
     * 页面点击url或自动重定向时候，所做的动作
     *
     * @param view
     * @param url 页面点击url或自动重定向时候的链接
     * @return
     */
    public void loadingUrl(WebView view, String url) {

        view.loadUrl(url);
    }
}