package com.youzu.clan.base.json.config.content;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/8/18.
 */
public class SearchSettings {

    private String enable;
    private ArrayList<SearchSettingItem> setting;
    private String enableSphinxon;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }


    public ArrayList<SearchSettingItem> getSetting() {
        return setting;
    }

    public void setSetting(ArrayList<SearchSettingItem> setting) {
        this.setting = setting;
    }

    public String getEnableSphinxon() {
        return enableSphinxon;
    }

    @JSONField(name = "enablesphinxon")
    public void setEnableSphinxon(String enableSphinxon) {
        this.enableSphinxon = enableSphinxon;
    }


}

