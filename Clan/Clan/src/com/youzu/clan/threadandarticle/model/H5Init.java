package com.youzu.clan.threadandarticle.model;

import android.content.Context;

import com.kit.utils.AppUtils;
import com.kit.utils.NetWorkUtils;
import com.youzu.clan.app.config.AppConfig;

import java.io.Serializable;

/**
 * Created by wjwu on 2015/11/5.
 */
public class H5Init implements Serializable {
    private String version;
    private String network;
    private PostData postData;
    private String platform;
    private AppTheme theme;
    private String type;
    private boolean debug;

    public static class AppTheme {
        public String name;
        public String color;
    }


    public static H5Init getInstance(Context context,PostData postData){
        H5Init h5Init = new H5Init();
        h5Init.version = AppUtils.getAppVersion(context);
        switch (NetWorkUtils.getNetworkTypeReabable(context)){
            case "mobile":
                h5Init.network = "1";
                break;
            case "wifi":
                h5Init.network = "2";
                break;
            default:
                h5Init.network = "0";
        }

        h5Init.postData = postData;
        h5Init.theme = new H5Init.AppTheme();
        h5Init.theme.color = "#334223";
        h5Init.theme.name = "init a";
        h5Init.platform = "android";
        h5Init.type = "dz";
        h5Init.debug = AppConfig.NET_DEBUG;
        return h5Init;
    }



}
