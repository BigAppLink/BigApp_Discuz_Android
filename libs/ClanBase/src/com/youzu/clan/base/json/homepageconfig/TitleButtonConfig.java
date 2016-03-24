package com.youzu.clan.base.json.homepageconfig;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;

/**
 * 首页底部tab按钮的样式
 * Created by tangh on 2015/9/22.
 */
public class TitleButtonConfig implements Serializable {
    /**
     * 1 : ”自定义tab” ,
     * 2 : ”首页发帖”,
     * 3 : ”我的”,
     * 4 : ”消息”,
     * 5 : ”版块”,
     */


    //icon的名字
    private String iconId;

    private String titleButtonType;
    private String pageType;

    private String buttonName;
    private String viewLink;


    public String getPageType() {
        return pageType;
    }

    @JSONField(name = "tab_type")
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getTitleButtonType() {
        return titleButtonType;
    }

    @JSONField(name = "title_button_type")
    public void setTitleButtonType(String buttonType) {
        this.titleButtonType = buttonType;
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


    public String getViewLink() {
        return viewLink;
    }

    @JSONField(name = "view_link")
    public void setViewLink(String viewLink) {
        this.viewLink = viewLink;
    }
}
