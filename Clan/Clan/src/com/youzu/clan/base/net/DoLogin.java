package com.youzu.clan.base.net;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.kit.app.UIHandler;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.json.QQLoginJson;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.login.BindActivity;
import com.youzu.clan.login.LoginActivity;
import com.youzu.clan.login.LoginPlatformActionListener;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Zhao on 15/9/1.
 */
public class DoLogin {

    /**
     * 第三方登录 发起
     *
     * @param context
     * @param platStr
     * @param callback
     */
    public static void authorize(final FragmentActivity context, String platStr, Handler.Callback callback, InjectDo injectDo) {
        Platform plat = ShareSDK.getPlatform(context, platStr);
        ZogUtils.printError(LoginActivity.class, "authorize plat.getName():" + plat.getName());
        ZogUtils.printError(LoginActivity.class, "authorize plat.isValid():" + plat.isValid());

        if (plat.isValid()) {
            platformLogin(context, platStr, callback, injectDo);
        } else {
            LoginPlatformActionListener loginPlatformActionListener = new LoginPlatformActionListener(context, callback);
            plat.setPlatformActionListener(loginPlatformActionListener);
            plat.SSOSetting(true);
            plat.showUser(null);
        }
    }


    public static void platformLogin(final FragmentActivity context, String platStr, Handler.Callback callback, InjectDo injectDo){
        Platform plat = ShareSDK.getPlatform(context, platStr);

        String userId = plat.getDb().getUserId();
        String token = plat.getDb().getToken();
        String openid = userId;

        ZogUtils.printError(DoLogin.class, "token:" + token + " openid:::::" + openid);

        ZogUtils.printError(DoLogin.class, "authorize userId:::::" + userId);
        if (!TextUtils.isEmpty(userId)) {
            UIHandler.sendEmptyMessage(LoginPlatformActionListener.MSG_USERID_FOUND, callback);

            ZogUtils.printError(LoginActivity.class, "MSG_USERID_FOUND");
            DoLogin.platformLoginCheck(context, userId, token, platStr, injectDo);
            return;
        }
    }

    public static void platformLoginCheck(final FragmentActivity context, final String openid, final String token, final String platform, final InjectDo injectDo) {
        LoginHttp.platformLoginCheck(context, token, openid, platform, new StringCallback(context) {
            @Override
            public void onSuccess(Context ctx, String s) {
                super.onSuccess(ctx, s);
                QQLoginJson qqLoginJson = ClanUtils.parseObject(s, QQLoginJson.class);

                if (qqLoginJson != null && qqLoginJson.getVariables() != null
                        && !StringUtils.isEmptyOrNullOrNullStr(qqLoginJson.getVariables().getHasbind())
                        && qqLoginJson.getVariables().getHasbind().equals("1")) {
                    injectDo.doSuccess(s);
                } else {
                    Bundle b = new Bundle();
                    b.putString(Key.KEY_QQ_LOGIN_RESULT, s);
                    b.putString(Key.KEY_QQ_LOGIN_OPENID, openid);
                    b.putString(Key.KEY_QQ_LOGIN_TOKEN, token);
                    b.putString(Key.KEY_THIRD_LOGIN_PLATFORM, platform);
                    IntentUtils.gotoNextActivity(context, BindActivity.class, b, true);
                }

            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(context, errorCode, msg);
                injectDo.doFail(msg,errorCode+"");
                ToastUtils.mkLongTimeToast(cxt, context.getString(R.string.login_failed));
            }
        });
    }


}
