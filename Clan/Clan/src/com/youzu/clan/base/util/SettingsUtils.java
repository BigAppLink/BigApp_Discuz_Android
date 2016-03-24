package com.youzu.clan.base.util;

import android.content.Context;

import com.kit.utils.ZogUtils;

/**
 * Created by Zhao on 15/9/28.
 */
public class SettingsUtils {


    /**
     * 是否开启极光推送
     * <p/>
     * <p/>
     * 如果服务器开启了推送、用户开启了推送、用户已经登录，则开启推送
     *
     * @param context
     * @return
     */
    public static boolean isEnableJPush(Context context) {

        ZogUtils.printError(SettingsUtils.class, "AppSPUtils.getContentConfig(context).getPushEnabled():" + AppSPUtils.getContentConfig(context).getPushEnabled());

        return (AppSPUtils.getContentConfig(context).getPushEnabled() != 0)
                && AppUSPUtils.isUUsePush(context) && (AppSPUtils.isLogined(context));
    }
}
