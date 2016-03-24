package com.youzu.clan.base.callback;

import android.content.Context;

import com.kit.utils.StringUtils;
import com.kit.utils.ToastUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.common.ErrorTag;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.util.ClanBaseUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HttpCallback<T> {
    private Class mClz;


    public void onstart(Context cxt) {
    }

    public void onSuccess(Context ctx, T t) {
    }


    /**
     * 网络类的异常会走到这里，逻辑类的异常走不到这里。因为逻辑类的异常实际上还是一个成功的网络请求
     *
     * @param cxt
     * @param errorCode
     * @param errorMsg  errorMsg当为正常错误的时候是特定的提示语。
     *                  当为异常情况 如“网络环境”等，这个值传返回的json数据，此处处理这个json，根据用户环境选择是否提示
     */
    public void onFailed(Context cxt, int errorCode, String errorMsg) {

        Message message = ClanBaseUtils.dealFail(cxt, errorMsg);
        if (!StringUtils.isEmptyOrNullOrNullStr(message.getMessagestr())) {
            errorMsg = message.getMessagestr();
        }

        ZogUtils.printError(BaseHttp.class, "HttpCallback errorMsg:" + errorMsg);

        if (ErrorTag.NO_SHOW.equals(message.getMessageval())) {
            ZogUtils.printError(HttpCallback.class, errorMsg);
        } else if (ErrorTag.ZHAO_ERROR_SHOW.equals(message.getMessageval())) {
            ToastUtils.mkShortTimeToast(cxt, errorMsg);
        } else {
            ZogUtils.printError(HttpCallback.class, errorMsg);
        }

    }

    public void onSuccessInThread(Context cxt, T t) {

    }

    public void onProgress(Context cxt, long total, long current) {
    }

    public Type getClz() {
        if (mClz == null) {
            mClz = getClass();
        }
        while (mClz != Object.class) {
            try {
                Type genType = mClz.getGenericSuperclass();
                if (genType instanceof ParameterizedType) {
                    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                    if (params != null && (params[0] instanceof Class)) {
                        return (Class<T>) params[0];
                    }
                }
            } catch (Exception e) {
            }
            mClz = mClz.getSuperclass();
        }

        return null;
    }

    public void setClz(Class clz) {
        mClz = clz;
    }

}
