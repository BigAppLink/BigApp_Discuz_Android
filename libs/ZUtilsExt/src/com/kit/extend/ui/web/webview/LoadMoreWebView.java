package com.kit.extend.ui.web.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 重新webview
 *
 * @author paoyx
 */
public class LoadMoreWebView extends VideoEnabledWebView {
    WebView mWebView;
    OnScrollChangedListener scrollInterface;
    WebScrollListener webScrollListener;

    public LoadMoreWebView(Context context) {
        super(context);
// TODO Auto-generated constructor stub   
    }

    public LoadMoreWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWebView = this;
        setOnCustomScrollChangeListener(new OnScrollChangedListener());
    }

    public LoadMoreWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        mWebView = this;
        setOnCustomScrollChangeListener(new OnScrollChangedListener());
// TODO Auto-generated constructor stub
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollInterface.onSrollChanged(l, t, oldl, oldt);
    }

    public void setOnCustomScrollChangeListener(OnScrollChangedListener t) {
        this.scrollInterface = t;
    }

    public void setWebScrollListener(WebScrollListener webScrollListener) {
        this.webScrollListener = webScrollListener;
    }


    /**
     * 定义滑动接口
     */
    public class OnScrollChangedListener {

        public void onSrollChanged(int l, int t, int oldl, int oldt) {
            float webcontent = mWebView.getContentHeight() * mWebView.getScale();//webview的高度
            float webnow = mWebView.getHeight() + mWebView.getScrollY() + 10;//当前webview的高度

//            ZogUtils.printError(LoadMoreWebView.class, "webcontent:" + webcontent + " webnow:" + webnow);
//            Log.e("APP", "" + l + " " + t + " " + oldl + " " + oldt);

            if (webScrollListener != null) {

                if (webcontent - webnow <= 0) {
                    //已经处于底端
                    webScrollListener.onBottom();
                } else if (webnow <= mWebView.getHeight() + 10 + 50) {
                    webScrollListener.onTop();
                } else {
                    webScrollListener.onScroll(l - oldl, t - oldt);
                    webScrollListener.onCenter();
                }
            }
        }


    }


    public interface WebScrollListener {

        public void onTop();

        public void onCenter();

        public void onBottom();

        public void onScroll(int dx, int dy);
    }


}
