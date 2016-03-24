package com.youzu.clan.base.json.update;

import com.youzu.android.framework.json.annotation.JSONField;

public class AutoUpdateInfo {
    private String updateMessage;
    private String apkUrl;
    private String apkName;
    private int apkCode;
    private int flag;


    public String getUpdateMessage() {
        return updateMessage;
    }

    @JSONField(name = "updatemsg")
    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    @JSONField(name = "pkgurl")
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkName() {
        return apkName;
    }

    @JSONField(name = "latest_version")
    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public int getApkCode() {
        return apkCode;
    }

    public void setApkCode(int apkCode) {
        this.apkCode = apkCode;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
