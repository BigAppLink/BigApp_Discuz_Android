package com.youzu.clan.base.json.config.content;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.config.LoginInfo;
import com.youzu.clan.base.json.config.content.smiley.SmileyInfo;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/7/22.
 */
public class ContentConfig {
    private String displayStyle;

    /**
     * 签到
     */
    private String checkinEnabled;

    /**
     * 表情配置
     */
    private SmileyInfo smileyInfo;
    /**
     * 版本号
     */
    private String iyzversion;


    private String replyButtonType;


    /**
     * 应用描述
     */
    private String appDesc;

    private SearchSettings searchSetting;
    /**
     * 首页帖子的配置
     */
    private ArrayList<ThreadConfigItem> threadConfig;
    /**
     * 首页文章的配置
     */
    private ArrayList<ThreadConfigItem> portalconfig;

    private PlatformLogin platformLogin;

    private int pushEnabled;

//    private Skeleton skeleton;

    /**
     * 登录、注册、web登录注册的相关信息
     */
    private LoginInfo loginInfo;

    private ArrayList<SettingRewrite> settingRewrite;

    private BlogConf blogconf;

    public String getDisplayStyle() {
        return displayStyle;
    }

    @JSONField(name = "display_style")
    public void setDisplayStyle(String displayStyle) {
        this.displayStyle = displayStyle;
    }


    public String getCheckinEnabled() {
        return checkinEnabled;
    }

    @JSONField(name = "checkin_enabled")
    public void setCheckinEnabled(String checkinEnabled) {
        this.checkinEnabled = checkinEnabled;
    }


    public SmileyInfo getSmileyInfo() {
        return smileyInfo;
    }

    @JSONField(name = "smiley_info")
    public void setSmileyInfo(SmileyInfo smileyInfo) {
        this.smileyInfo = smileyInfo;
    }


    public String getAppDesc() {
        return appDesc;
    }

    @JSONField(name = "appdesc")
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public SearchSettings getSearchSetting() {
        return searchSetting;
    }

    @JSONField(name = "searchsetting")
    public void setSearchSetting(SearchSettings searchSetting) {
        this.searchSetting = searchSetting;
    }

    public ArrayList<ThreadConfigItem> getThreadConfig() {
        return threadConfig;
    }

    @JSONField(name = "threadconfig")
    public void setThreadConfig(ArrayList<ThreadConfigItem> threadConfig) {
        this.threadConfig = threadConfig;
    }


    public PlatformLogin getPlatformLogin() {
        return platformLogin;
    }


    @JSONField(name = "platform_login")
    public void setPlatformLogin(PlatformLogin platformLogin) {
        this.platformLogin = platformLogin;
    }

    public String getIyzversion() {
        return iyzversion;
    }

    public void setIyzversion(String iyzversion) {
        this.iyzversion = iyzversion;
    }

    public String getReplyButtonType() {
        return replyButtonType;
    }

    @JSONField(name = "reply_button_type")
    public void setReplyButtonType(String replyButtonType) {
        this.replyButtonType = replyButtonType;
    }

    public ArrayList<ThreadConfigItem> getPortalconfig() {
        return portalconfig;
    }

    public void setPortalconfig(ArrayList<ThreadConfigItem> portalconfig) {
        this.portalconfig = portalconfig;
    }


    public int getPushEnabled() {
        return pushEnabled;
    }

    @JSONField(name = "push_enabled")
    public void setPushEnabled(int pushEnabled) {
        this.pushEnabled = pushEnabled;
    }



    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    @JSONField(name = "login_info")
    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public ArrayList<SettingRewrite> getSettingRewrite() {
        return settingRewrite;
    }

    @JSONField(name = "setting_rewrite")
    public void setSettingRewrite(ArrayList<SettingRewrite> settingRewrite) {
        this.settingRewrite = settingRewrite;
    }

    public BlogConf getBlogconf() {
        return blogconf;
    }

    public void setBlogconf(BlogConf blogconf) {
        this.blogconf = blogconf;
    }

    //    public Skeleton getSkeleton() {
//        return skeleton;
//    }
//
//    public void setSkeleton(Skeleton skeleton) {
//        this.skeleton = skeleton;
//    }
}
