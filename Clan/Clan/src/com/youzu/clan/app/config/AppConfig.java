package com.youzu.clan.app.config;

import com.kit.app.enums.AppStatus;

/**
 * Created by Zhao on 15/5/13.
 */
public class AppConfig extends com.kit.config.AppConfig {

    public static String isShowing = AppStatus.STOPPED;


    /**
     * 关于连接到的网址
     */
    public static final String ABOUT = "http://www.youzu.cn/about/";



    /**
     * 登录模式 0表示native方式，1表示web方式；当mod为1时候，登陆注册url才有效
     */
//    public static int LOGIN_MODE = LoginMode.NATIVE;
//    public static String LOGIN_URL = "";

    /**
     * 注册模式 0表示native方式，1表示web方式；当mod为1时候，登陆注册url才有效
     */
//    public static int REG_MODE = RegisterMode.NATIVE;
//    public static String REG_URL = "";
//    public static int REG_SWITCH = 1;
//    public static boolean isUseSearch = true;

    public static boolean isNewLaunch = false;


    public static boolean NET_DEBUG = false;


}
