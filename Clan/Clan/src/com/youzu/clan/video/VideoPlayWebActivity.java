package com.youzu.clan.video;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.WebView;

import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;

public class VideoPlayWebActivity extends com.kit.extend.ui.web.video.VideoPlayWebActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    public void initWebView() {
        super.initWebView();

        webFragment.getWebView().setWebViewClient(new DefaultWebViewClient(webFragment) {

            @Override
            public void onPageFinished(WebView view, String url) {
                //开始
                super.onPageFinished(view, url);
//                输出渲染完成后的html页面
//                webFragment.webView.loadUrl("javascript:window.android.showSource(document.body.innerHTML);");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ZogUtils.printError(VideoPlayWebActivity.class, "shouldOverrideUrlLoading:" + url);
                super.shouldOverrideUrlLoading(view, url);
//                JumpWebUtils.gotoWeb(VideoPlayWebActivity.this,"",url);
//                String cookie = CookieUtils.getCookie(VideoPlayWebActivity.this, url);
//                BundleData bundleData = new BundleData();
//                bundleData.put("content", url);
//                bundleData.put("cookie", cookie);
//
//                IntentUtils.gotoNextActivity(VideoPlayWebActivity.this, WebActivity.class, bundleData);

//                JumpWebUtils.gotoWeb(VideoPlayWebActivity.this, "", url);

                //如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayWebActivity.this.finish();
            }
        });

    }


}
