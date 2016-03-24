package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * 界面的详细配置
 * 
 */
public class ViewTabConfig implements Serializable{
    /**
     * 1 : ”单页面” ,
     * 2 : ”导航页面”,
     * 3 : ”WAP页面”,
     */
    private String tabType;
    private String wapPage;
    //配置导航时才有数据
    private ArrayList<NavPage> navPage;
    //单页面是才有数据
    private ArrayList<FunctionSetting> homePage;


    //title按钮 屏幕右上方的按钮
    private ArrayList<TitleButtonConfig> titleCfg;



    //title按钮 屏幕右上方的按钮
    private String useWapName;




    public String getTabType() {
        return tabType;
    }

    @JSONField(name = "tab_type")
    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public String getWapPage() {
        return wapPage;
    }

    @JSONField(name = "wap_page")
    public void setWapPage(String wapPage) {
        this.wapPage = wapPage;
    }

    public ArrayList<NavPage> getNavPage() {
        return navPage;
    }

    @JSONField(name = "navi_page")
    public void setNavPage(ArrayList<NavPage> navPage) {
        this.navPage = navPage;
    }

    public ArrayList<FunctionSetting> getHomePage() {
        return homePage;
    }

    @JSONField(name = "home_page")
    public void setHomePage(ArrayList<FunctionSetting> homePager) {
        this.homePage = homePager;
    }

    public ArrayList<TitleButtonConfig> getTitleCfg() {
        return titleCfg;
    }


    @JSONField(name = "title_cfg")
    public void setTitleCfg(ArrayList<TitleButtonConfig> titleCfg) {
        this.titleCfg = titleCfg;
    }


    public String getUseWapName() {
        return useWapName;
    }

    @JSONField(name = "use_wap_name")
    public void setUseWapName(String useWapName) {
        this.useWapName = useWapName;
    }
}
