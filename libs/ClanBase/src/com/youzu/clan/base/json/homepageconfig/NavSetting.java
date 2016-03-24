package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/9/28.
 */
public class NavSetting {
    private String tabType;


    //当tabType==HomeTabType.TAB_WAP_TYPE时，才有意义
    private String wapPage;


    //当tabType==HomeTabType.TAB_SINGLE_PAGE_TYPE时，才有意义
    private ArrayList<FunctionSetting> homePage;


    public String getTabType() {
        return tabType;
    }

    public String getWapPage() {
        return wapPage;
    }

    @JSONField(name = "wap_page")
    public void setWapPage(String wapPage) {
        this.wapPage = wapPage;
    }

    @JSONField(name = "tab_type")
    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public ArrayList<FunctionSetting> getHomePage() {
        return homePage;
    }


    @JSONField(name = "home_page")
    public void setHomePage(ArrayList<FunctionSetting> homePage) {
        this.homePage = homePage;
    }
}
