package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;

/**
 * 首页底部tab按钮的样式
 * Created by tangh on 2015/9/22.
 */
public class ButtonConfig implements Serializable {
    private String id;
    /**
     * 1 : ”自定义tab” ,
     * 2 : ”首页发帖”,
     * 3 : ”我的”,
     * 4 : ”消息”,
     * 5 : ”版块”,
     */
    private String buttonType;
    private String buttonName;
    //    private String iconType;
    private ViewTabConfig viewTabConfig;
    private String iconId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getButtonType() {
        return buttonType;
    }


    @JSONField(name = "button_type")
    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getButtonName() {
        return buttonName;
    }

    @JSONField(name = "button_name")
    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

//    public String getIconType() {
//        return iconType;
//    }
//
//    public void setIconType(String iconType) {
//        this.iconType = iconType;
//    }


    public String getIconId() {
        return iconId;
    }

    @JSONField(name = "icon_type")
    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public ViewTabConfig getViewTabConfig() {
        return viewTabConfig;
    }

    @JSONField(name = "tab_cfg")
    public void setViewTabConfig(ViewTabConfig viewTabConfig) {
        this.viewTabConfig = viewTabConfig;
    }


}
