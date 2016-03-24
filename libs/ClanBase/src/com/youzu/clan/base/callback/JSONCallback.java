package com.youzu.clan.base.callback;

import android.content.Context;

import com.kit.utils.ToastUtils;
import com.youzu.clan.base.util.ClanBaseUtils;

public class JSONCallback extends HttpCallback<String> {

    @Override
    public void onSuccess(Context ctx, String jsonStr) {
        super.onSuccess(ctx, jsonStr);
    }

    @Override
    public void onFailed(Context cxt, int errorCode, java.lang.String errorMsg) {
        super.onFailed(cxt, errorCode, errorMsg);

    }
}
