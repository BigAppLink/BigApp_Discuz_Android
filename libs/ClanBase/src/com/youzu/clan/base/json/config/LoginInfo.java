package com.youzu.clan.base.json.config;

import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/6/24.
 */
public class LoginInfo {
    private int loginMod;

    private String loginUrl;

    private int regMod;

    private String regUrl;

    private int regSwitch;

    private String allowAvatarChange;

    public int getLoginMod() {
        return loginMod;
    }

    @JSONField(name = "login_mod")
    public void setLoginMod(int loginMod) {
        this.loginMod = loginMod;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    @JSONField(name = "login_url")
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public int getRegMod() {
        return regMod;
    }

    @JSONField(name = "reg_mod")
    public void setRegMod(int regMod) {
        this.regMod = regMod;
    }

    public String getRegUrl() {
        return regUrl;
    }

    @JSONField(name = "reg_url")
    public void setRegUrl(String regUrl) {
        this.regUrl = regUrl;
    }


    public int getRegSwitch() {
        return regSwitch;
    }

    @JSONField(name = "reg_switch")
    public void setRegSwitch(int regSwitch) {
        this.regSwitch = regSwitch;
    }

    public String getAllowAvatarChange() {
        return allowAvatarChange;
    }

    @JSONField(name = "allow_avatar_change")
    public void setAllowAvatarChange(String allowAvatarChange) {
        this.allowAvatarChange = allowAvatarChange;
    }
}
