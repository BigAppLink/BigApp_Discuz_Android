package com.youzu.clan.share;

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
public class SharePlatformActionListener implements PlatformActionListener {
    public static final int ON_CANCEL = 666 + 0;
    public static final int ON_ERROR = 666 + 1;
    public static final int ON_COMPLETE = 666 + 2;

    private Context context;
    private Handler.Callback callback;

    public SharePlatformActionListener(Context context, Handler.Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        Message msg = new Message();
        msg.what = ON_CANCEL;
        UIHandler.sendMessage(callback,msg);
    }

    @Override
    public void onComplete(Platform plat, int action,
                           HashMap<String, Object> res) {

        ZogUtils.printError(SharePlatformActionListener.class, "onComplete:" + JsonUtils.toJSON(res).toString());

        Message msg = new Message();
        msg.what = ON_COMPLETE;
        msg.arg1 = 1;
        msg.arg2 = action;
//        msg.obj = getString(R.string.share_completed);
        UIHandler.sendMessage(callback, msg);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        Message msg = new Message();
        msg.what = ON_ERROR;

        if (arg2.toString().contains("upload_url_text")) {
            msg.obj = "您无权限分享链接，请去新浪开放平台提升权限";
        } else if (arg2.toString().contains("repeat content")) {
            msg.obj = "您不能重复分享";
        } else if (arg2.toString().contains("unsupport mediatype")) {
            msg.obj = "不支持的媒体类型";
        }


        UIHandler.sendMessage(callback, msg);

        ZogUtils.printError(SharePlatformActionListener.class, "onError:" + arg2.toString());
    }

}
