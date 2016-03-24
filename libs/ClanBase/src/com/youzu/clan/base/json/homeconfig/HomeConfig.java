package com.youzu.clan.base.json.homeconfig;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/6/30.
 */
public class HomeConfig {
    private ArrayList<HomeConfigItem> banner;
    private Func func;

    public ArrayList<HomeConfigItem> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<HomeConfigItem> banner) {
        this.banner = banner;
    }

    public Func getFunc() {
        return func;
    }

    public void setFunc(Func func) {
        this.func = func;
    }
}
