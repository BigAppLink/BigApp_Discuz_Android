package com.youzu.clan.base.json.config.content.smiley;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/8/13.
 */
public class SmileyInfo {
    private String MD5;
    private String zipUrl;
    private String lastMD5;
    private ArrayList<ZipInfo> zipInfo;

    public String getMD5() {
        return MD5;
    }

    @JSONField(name = "md5")
    public void setMD5(String md5) {
        this.MD5 = md5;
    }

    public String getZipUrl() {
        return zipUrl;
    }

    @JSONField(name = "zip_url")
    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

//    public String getLastMD5() {
//        return lastMD5;
//    }
//
//    public void setLastMD5(String lastMD5) {
//        this.lastMD5 = lastMD5;
//    }


    public ArrayList<ZipInfo> getZipInfo() {
        return zipInfo;
    }

    @JSONField(name = "zip_info")
    public void setZipInfo(ArrayList<ZipInfo> zipInfo) {
        this.zipInfo = zipInfo;
    }
}
