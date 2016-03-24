package com.youzu.clan.base.json.config;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/6/16.
 */
public class ClanConfig {
    private String appId;

    private String appKey;

    private String apiUrl;

    private String cfg;

    private AdUrl ad;

    private String apiUrlPath;

    private String apiUrlBase;

    private String apiUrlRealBase;

    private LoginInfo loginInfo;

    private int appStyle;

    public String getAppId() {
        return appId;
    }

    @JSONField(name = "app_id")
    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getAppKey() {
        return appKey;
    }

    @JSONField(name = "app_key")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    @JSONField(name = "api_url")
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getCfg() {
        return cfg;
    }

    public void setCfg(String cfg) {
        this.cfg = cfg;
    }

    public AdUrl getAd() {
        return ad;
    }

    public void setAd(AdUrl ad) {
        this.ad = ad;
    }

    public String getApiUrlPath() {
        return apiUrlPath;
    }

    @JSONField(name = "api_url_path")
    public void setApiUrlPath(String apiUrlPath) {
        this.apiUrlPath = apiUrlPath;
    }

    public String getApiUrlRealBase() {
        return apiUrlRealBase;
    }

    public void setApiUrlRealBase(String apiUrlRealBase) {
        this.apiUrlRealBase = apiUrlRealBase;
    }

    public String getApiUrlBase() {
        return apiUrlBase;
    }

    @JSONField(name = "api_url_base")
    public void setApiUrlBase(String apiUrlBase) {
        this.apiUrlBase = apiUrlBase;
    }


    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    @JSONField(name = "account")
    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }


    public int getAppStyle() {
        return appStyle;
    }

    @JSONField(name = "theme")
    public void setAppStyle(int appStyle) {
        this.appStyle = appStyle;
    }
}
