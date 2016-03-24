package com.youzu.clan.base.json.config;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/6/16.
 */
public class AdUrl {
    private String urlSplashAd;

    private String urlListAd;

    private String urlLogAd;


    public String getUrlSplashAd() {
        return urlSplashAd;
    }

    @JSONField(name = "url_splash_ad")
    public void setUrlSplashAd(String urlSplashAd) {
        this.urlSplashAd = urlSplashAd;
    }

    public String getUrlListAd() {
        return urlListAd;
    }

    @JSONField(name = "url_list_ad")
    public void setUrlListAd(String urlListAd) {
        this.urlListAd = urlListAd;
    }

    public String getUrlLogAd() {
        return urlLogAd;
    }

    @JSONField(name = "url_log_ad")
    public void setUrlLogAd(String urlLogAd) {
        this.urlLogAd = urlLogAd;
    }
}
