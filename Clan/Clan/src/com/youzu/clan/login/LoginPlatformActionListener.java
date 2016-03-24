package com.youzu.clan.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.kit.app.UIHandler;
import com.kit.utils.ZogUtils;
import com.youzu.android.framework.JsonUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by Zhao on 15/8/18.
 */
public class LoginPlatformActionListener implements PlatformActionListener {

    public static final int MSG_USERID_FOUND = 888 + 1;
    public static final int MSG_LOGIN = 888 + 2;
    public static final int MSG_AUTH_CANCEL = 888 + 3;
    public static final int MSG_AUTH_ERROR = 888 + 4;
    public static final int MSG_AUTH_COMPLETE = 888 + 5;


    private Context context;
    private Handler.Callback callback;

    public LoginPlatformActionListener(Context context, Handler.Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        ZogUtils.printError(LoginPlatformActionListener.class, "onCancel");

        Message msg = new Message();
        msg.what = MSG_AUTH_CANCEL;
        UIHandler.sendMessage(callback,msg );
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {

        ZogUtils.printError(LoginPlatformActionListener.class, "onComplete:" + JsonUtils.toJSON(res).toString());

        Message msg = new Message();
        msg.what = MSG_AUTH_COMPLETE;
        msg.arg1 = 1;
        msg.arg2 = action;
//        msg.obj = getString(R.string.share_completed);
        UIHandler.sendMessage(callback,msg);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        ZogUtils.printError(LoginPlatformActionListener.class, "onError:" + arg2.toString());

        Message msg = new Message();
        msg.what = MSG_AUTH_ERROR;

        if (arg2.toString().contains("upload_url_text")) {
            msg.obj = "您无权限分享链接，请去新浪开放平台提升权限";
        } else if (arg2.toString().contains("repeat content")) {
            msg.obj = "您不能重复分享";
        } else if (arg2.toString().contains("unsupport mediatype")) {
            msg.obj = "不支持的媒体类型";
        } else if (arg2.toString().contains("WechatClientNotExistException")) {
            msg.obj = "未安装微信";
        }


        UIHandler.sendMessage(callback,msg);

    }

}
