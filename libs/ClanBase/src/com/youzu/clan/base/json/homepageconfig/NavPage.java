package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by tangh on 2015/9/22.
 */
public class NavPage implements Serializable{

    private String naviName;

//    private ArrayList<FunctionSetting> setting;

    private  NavSetting navSetting;



    public String getNaviName() {
        return naviName;
    }

    @JSONField(name = "navi_name")
    public void setNaviName(String naviName) {
        this.naviName = naviName;
    }

    public NavSetting getNavSetting() {
        return navSetting;
    }

    @JSONField(name = "navi_setting")
    public void setNavSetting(NavSetting navSetting) {
        this.navSetting = navSetting;
    }


    //    public ArrayList<FunctionSetting> getSetting() {
//        return setting;
//    }
//
//    public void setSetting(ArrayList<FunctionSetting> setting) {
//        this.setting = setting;
//    }
}
