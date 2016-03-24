package com.youzu.clan.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.kit.extend.ui.web.client.DefaultWebViewClient;
import com.kit.utils.UrlUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.app.ClanApplication;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.WebActivity;
import com.youzu.clan.base.json.ProfileJson;
import com.youzu.clan.base.net.DoLogin;
import com.youzu.clan.base.net.DoProfile;
import com.youzu.clan.base.util.ClanUtils;

import java.util.Map;
import java.util.TimerTask;

/**
 * Created by Zhao on 15/6/23.
 */
public class CallBackWebActivity extends WebActivity {

    public Context context;
    private ClanApplication mApplication;
    int i = 0;
    private String cookieStr;

    private TimerTask task = new TimerTask() {
        public void run() {
            // TODOAuto-generated method stub
            //需要执行的代码
            DoProfile.getProfile(CallBackWebActivity.this, null, null);


        }
    };

    private InjectDo injectDo = new InjectDo() {
        @Override
        public boolean doSuccess(Object baseJson) {
            ZogUtils.printError(CallBackWebActivity.class, "doSuccess doSuccess doSuccess doSuccess doSuccess");
            ProfileJson profileJson = (ProfileJson) baseJson;
            if (profileJson != null && task != null) {
                task.cancel();
            }

            return false;
        }

        @Override
        public boolean doFail(Object baseJson, String tag) {
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mApplication = (ClanApplication) getApplication();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        task.run();
    }

    @Override
    public void initWebView() {
        super.initWebView();
        webFragment.getWebView().setWebViewClient(new DefaultWebViewClient(webFragment) {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                ZogUtils.printError(CallBackWebActivity.class, "onPageFinished url::::" + url);

                CookieManager cookieManager = CookieManager.getInstance();
                cookieStr = cookieManager.getCookie(url);
                ClanUtils.pushCookie(CallBackWebActivity.this, cookieStr);

                Log.e("APP", "cookieStr:" + cookieStr + " i::::" + i);


                if (url.contains("platform=qq") && url.contains("oauth_token") && url.contains("openid")) {
                    Map<String, String> map = UrlUtils.getUrlParameters(url);

                    final String token = map.get("oauth_token");
                    final String openid = map.get("openid");
                    final String platform = map.get("platform");

                    DoLogin.platformLoginCheck(CallBackWebActivity.this, openid, token, platform, new InjectDo() {
                        @Override
                        public boolean doSuccess(Object baseJson) {
                            DoProfile.getProfile(CallBackWebActivity.this, null, injectDo);
                            return false;
                        }

                        @Override
                        public boolean doFail(Object baseJson, String tag) {
                            return false;
                        }
                    });

                    task.run();


                } else {
                    DoProfile.getProfile(CallBackWebActivity.this, cookieStr, injectDo);
                }

            }

        });


    }


}
