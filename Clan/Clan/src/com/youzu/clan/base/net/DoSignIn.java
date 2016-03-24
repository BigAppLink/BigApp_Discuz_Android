package com.youzu.clan.base.net;

import android.content.Context;
import android.widget.ImageButton;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.json.signin.SignInJson;
import com.youzu.clan.base.json.signin.SignInVariables2;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.ToastUtils;

/**
 * Created by Zhao on 15/8/19.
 */
public class DoSignIn {


    public static final String CHECK = "1";
    public static final String SIGN_IN = "0";

    /**
     * @param context
     * @param signIn
     * @param checkType check=0,点击签到 check=1,点击检查是否以签到
     */
    public static void checkSignIn(final Context context, final ImageButton signIn, final String checkType, final DoSomeThing doSomeThing) {

        if (signIn == null) {
            return;
        }

        ClanHttpParams params = new ClanHttpParams(context);
        params.addQueryStringParameter("module", "checkin");
        params.addQueryStringParameter("iyzmobile", "1");
        params.addQueryStringParameter("check", checkType);
        params.addQueryStringParameter("uid", AppSPUtils.getUid(context));

        ProfileHttp.checkIn(context, AppSPUtils.getUid(context), checkType, new HttpCallback<String>() {
            @Override
            public void onSuccess(final Context ctx, final String s) {
                super.onSuccess(ctx, s);
                SignInJson json = ClanUtils.parseObject(s, SignInJson.class);
                if (json != null && json.getVariables() != null) {
                    SignInVariables2 signInVariables = json.getVariables();

                    switch (checkType) {
                        case SIGN_IN:
                            if (json.getMessage() != null && "checked in success".equals(json.getMessage().getMessagestr())) {
                                signIn.setEnabled(false);
                                ToastUtils.mkLongTimeToast(ctx, ctx.getString(R.string.sign_in_toast, signInVariables.getTitle(), signInVariables.getCredit()));
                                if (doSomeThing != null) {
                                    doSomeThing.execute();
                                }
                            }
                            break;
                        case CHECK:
                            String checked = signInVariables.getChecked();

                            if (!StringUtils.isEmptyOrNullOrNullStr(checked)
                                    && checked.equals("1")) {
                                ZogUtils.printError(DoSignIn.class, "checked:" + checked);
//                                signIn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_haved_sign_in));
                                signIn.setEnabled(false);
                            } else {
                                signIn.setEnabled(true);
                            }
                            break;
                    }
                } else {
                    if (checkType.equals(SIGN_IN)) {
                        ToastUtils.mkShortTimeToast(ctx, ctx.getString(R.string.sign_in_fail));
                    }
                }



            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(context, errorCode, errorMsg);
                if (checkType.equals(SIGN_IN)) {
                    ToastUtils.mkShortTimeToast(cxt, cxt.getString(R.string.sign_in_fail));
                }
                signIn.setEnabled(false);

            }
        });
    }

}
