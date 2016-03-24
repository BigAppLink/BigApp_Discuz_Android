package com.youzu.clan.base.json.homepageconfig;

import com.youzu.clan.base.json.homeconfig.HomeConfigItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangh on 2015/9/25.
 */
public class FunctionSetting implements Serializable {
    private String type;

    private ArrayList<HomeConfigItem> setting;

    //只有是Type==recom的时候才有数据
    private Recommend recommend;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<HomeConfigItem> getSetting() {
        return setting;
    }

    public void setSetting(ArrayList<HomeConfigItem> setting) {
        this.setting = setting;
    }

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }
}
