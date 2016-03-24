package com.youzu.clan.base.callback;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import com.kit.utils.ToastUtils;
import com.youzu.clan.base.json.ProfileJson;

public abstract class LoginCallback extends ProgressCallback<ProfileJson> {

    public LoginCallback(ActionBarActivity activity) {
        super(activity);
    }

    public  void onSuccess(Context context,String uid, ProfileJson t){
        super.onSuccess(context,t);
    }

    @Override
    public void onFailed(Context cxt, int errorCode, String msg) {
        super.onFailed(cxt, errorCode, msg);
        ToastUtils.mkLongTimeToast(activity, msg);
    }

}
