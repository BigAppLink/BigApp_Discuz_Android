package com.youzu.clan.base.json.config.content;

import com.kit.utils.StringUtils;
import com.youzu.android.framework.json.annotation.JSONField;

/**
 * Created by Zhao on 15/9/1.
 */
public class PlatformLogin {

    private String qqLogin;
    private String wechatLogin;
    private String weiboLogin;
    private String qqloginEnd;

    public String getQQLogin() {
        return qqLogin;
    }

    @JSONField(name = "qqlogin")
    public void setQQlogin(String qqLogin) {
        this.qqLogin = qqLogin;
    }


    public String getWechatLogin() {
        return wechatLogin;
    }

    @JSONField(name = "wechat_login")
    public void setWechatLogin(String wechatLogin) {
        this.wechatLogin = wechatLogin;
    }

    public String getWeiboLogin() {
        return weiboLogin;
    }

    @JSONField(name = "weibo_login")
    public void setWeiboLogin(String weiboLogin) {
        this.weiboLogin = weiboLogin;
    }

    public String getQQloginEnd() {
        return qqloginEnd;
    }

    @JSONField(name = "qqlogin_end")
    public void setQQloginEnd(String qqloginEnd) {
        this.qqloginEnd = qqloginEnd;
    }


    public boolean isQQLogin() {
        if (StringUtils.isEmptyOrNullOrNullStr(qqLogin) || qqLogin.equals("0")) {
            return false;
        }
        return true;
    }

    public boolean isWeiboLogin() {
        if (StringUtils.isEmptyOrNullOrNullStr(weiboLogin) || weiboLogin.equals("0")) {
            return false;
        }
        return true;
    }

    public boolean isWeChatLogin() {
        if (StringUtils.isEmptyOrNullOrNullStr(wechatLogin) || wechatLogin.equals("0")) {
            return false;
        }
        return true;
    }

}
