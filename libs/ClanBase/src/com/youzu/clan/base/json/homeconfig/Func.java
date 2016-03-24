package com.youzu.clan.base.json.homeconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/6/30.
 * <p/>
 * 功能模块
 */
public class Func {
    private ArrayList<HomeConfigItem> link;
    private ArrayList<HomeConfigItem> plate;

    public ArrayList<HomeConfigItem> getLink() {
        return link;
    }

    public void setLink(ArrayList<HomeConfigItem> link) {
        this.link = link;
    }

    public ArrayList<HomeConfigItem> getPlate() {
        return plate;
    }

    @JSONField(name = "forum")
    public void setPlate(ArrayList<HomeConfigItem> plate) {
        this.plate = plate;
    }
}
