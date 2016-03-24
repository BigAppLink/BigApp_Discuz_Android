package com.youzu.clan.base.json.homepageconfig;


import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/9/22.
 */
public class HomepageVariables extends Variables implements Serializable {

    private String title;
    private ArrayList<ButtonConfig> buttonConfigs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ButtonConfig> getButtonConfigs() {
        return buttonConfigs;
    }

    @JSONField(name = "button_configs")
    public void setButtonConfigs(ArrayList<ButtonConfig> buttonConfigs) {
        this.buttonConfigs = buttonConfigs;
    }
}
