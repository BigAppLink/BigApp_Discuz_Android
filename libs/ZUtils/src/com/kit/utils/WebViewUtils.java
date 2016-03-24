package com.kit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;

public class WebViewUtils {

    public static String getStartWith(String url) {

        int end = url.indexOf("://");

        ZogUtils.printError(WebViewUtils.class, "end:" + end);

        if (end == -1)
            end = url.length();
        return url.substring(0, end);
    }


    /**
     * webview 显示本地图片，自适应布局大小，图片可缩放
     */
    public static void showNetImage(Context mContext, final WebView webview,
                                    final String imageLocalUrl, int width, int height) {
        final String fileName = "image.png";

        String data = "<HTML><IMG src=\"" + imageLocalUrl + "\"" + "width=" + width + "height=" + height + "/>";
        webview.loadDataWithBaseURL(imageLocalUrl, data, "text/html", "utf-8", null);

        //webview.loadUrl(imageUrl);//直接显示网上图片
        webview.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        webview.getSettings().setSupportZoom(true); //可以缩放
        webview.setSaveEnabled(true);


    }

    /**
     * webview 显示本地图片，自适应布局大小，图片可缩放
     *
     * @param mContext
     * @param webview
     * @param imageLocalUrl
     * @param isAdapterScreenWidth 是否自适应屏幕宽度
     * @param color                0-255
     */
    public static void showLocalImage(Context mContext, final WebView webview,
                                      final String imageLocalUrl, int color, boolean isAdapterScreenWidth, boolean doubleClickEabled) {

        boolean fileExist = FileUtils.isExists(imageLocalUrl);

        if (fileExist) {
            String bgcolor = ColorUtils.toBrowserColor(color);

            ZogUtils.printLog(WebViewUtils.class, "bgcolor:" + bgcolor);

            String adapterScreenWidth = "";
            if (isAdapterScreenWidth) {
                adapterScreenWidth = " width:99.9%;";
            }


            String style =
                    "<style>" +
                            "* { margin:0; padding:0; background-color:" +
                            bgcolor +
                            ";  }" +

                            "img { " + adapterScreenWidth + " margin:0; padding:0; }" +

                            "div{" +
                            adapterScreenWidth +
//                            "     border: thin solid #F00;" +
                            "    margin:0; padding:0;" +
                            " }/*这里的width height 大于图片的宽高*/" +

                            "table{ height:100%; width:100%; text-align:center;}" +

                            " </style>";


            String body = "    <body>" +
                    "        <div>" +
                    "            <table>" +
                    "                <tr>" +
                    "                    <td>" +
                    "                       <img src=\"file://" + imageLocalUrl + "\"" +
//                            "                                width=" + width +
                    "                               margin=" + 0 +
                    "                               padding=" + 0 +
//                    "                               height="+height+
                    "                           />" +
                    "                    </td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </div>" +
                    "    </body>" +
                    "";

            String data = style + body;

            webview.loadDataWithBaseURL("file://" + imageLocalUrl, data, "text/html", "utf-8", null);

            //webview.loadUrl(imageUrl);//直接显示网上图片

            webview.setVerticalScrollBarEnabled(false); // 取消Vertical ScrollBar显示
            webview.setHorizontalScrollBarEnabled(false); //取消Horizontal ScrollBar显示
            webview.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
            webview.getSettings().setSupportZoom(true); //可以缩放
            setZoomControlGone(webview);//去掉zoom按钮

            if (doubleClickEabled) {//双击缩放
                webview.getSettings().setUseWideViewPort(true);
                webview.getSettings().setLoadWithOverviewMode(true);
            }
            webview.setSaveEnabled(true);


        }
    }


    public static void loadContentAdaptiveScreen(Context mContext,
                                                 WebView webview, String content) {
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        webview.loadDataWithBaseURL("", content, mimeType, encoding, "");
    }

    public static void loadContent(Context mContext, WebView webview,
                                   String content) {
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        webview.loadDataWithBaseURL("", content, mimeType, encoding, "");
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void loadUrl(Context mContext, WebView webview, String url,
                               boolean javaScriptEnabled) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(javaScriptEnabled);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public static void loadUrlAdaptiveScreen(Context mContext, WebView webview,
                                             String url, boolean javaScriptEnabled) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(javaScriptEnabled);
        // 自适应屏幕
        // 第一种：
        // WebSetting settings = webView.getSettings();
        // settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // 把所有内容放在webview等宽的一列中。（可能会出现页面中链接失效）
        // 第二种：
        // settings.setUseWideViewPort(true);
        // settings.setLoadWithOverviewMode(true);
        // 第三种：
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity <= 120) {
            webSettings.setDefaultZoom(ZoomDensity.CLOSE);
        } else if (mDensity > 120 && mDensity < 240) {
            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
        } else if (mDensity >= 240) {
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        }

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);

    }


    /**
     * 实现放大缩小控件隐藏
     */
    public static void setZoomControlGone(WebView view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {//用于判断是否为Android 3.0系统, 然后隐藏缩放控件
            view.getSettings().setDisplayZoomControls(false);
        } else {

            //Android 3.0(11) 以下使用以下方法:
            //利用java的反射机制

            Class classType;
            Field field;
            try {
                classType = WebView.class;
                field = classType.getDeclaredField("mZoomButtonsController");
                field.setAccessible(true);
                ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
                mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
                try {
                    field.set(view, mZoomButtonsController);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

}
