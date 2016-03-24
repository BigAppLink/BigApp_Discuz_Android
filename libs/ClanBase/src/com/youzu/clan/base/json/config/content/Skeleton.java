package com.youzu.clan.base.json.config.content;

/**
 * Created by Zhao on 15/9/24.
 */


import java.util.ArrayList;

/**
 * 首页的五个按钮
 */
public class Skeleton {

    private String selected;
    private ArrayList<SkeletonSetting>  setting;


    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public ArrayList<SkeletonSetting> getSetting() {
        return setting;
    }

    public void setSetting(ArrayList<SkeletonSetting> setting) {
        this.setting = setting;
    }
}
