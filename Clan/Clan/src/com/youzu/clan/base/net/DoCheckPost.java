package com.youzu.clan.base.net;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.callback.JSONCallback;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.json.CheckPostJson;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.json.checkpost.Allowperm;
import com.youzu.clan.base.json.checkpost.CheckPostVariables;
import com.youzu.clan.base.json.forumdisplay.ThreadTypes;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.forum.ForumActivity;
import com.youzu.clan.thread.detail.ThreadDetailActivity;
import com.youzu.clan.thread.ThreadNewActivity;
import com.youzu.clan.thread.ThreadReplyActivity;

/**
 * Created by Zhao on 15/6/8.
 */
public class DoCheckPost {


    public static void getCheckPost(final FragmentActivity activity, final String fid, final InjectDo injectDo) {

        ZogUtils.printLog(DoCheckPost.class, "getCheckPost From:" + activity.getClass().getName());

        ThreadHttp.checkPost(activity, fid, new JSONCallback() {
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);
                CheckPostJson checkPostJson = ClanUtils.parseObject(json, CheckPostJson.class);
                if (json != null)
                    injectDo.doSuccess(checkPostJson);
                else
                    injectDo.doFail(checkPostJson, null);

            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(activity, errorCode, errorMsg);

            }
        });
    }


    public static void checkPostBeforeReply(final FragmentActivity activity, final String fid, final InjectDo injectDo) {

        final int NO_LOGIN = 0;
        ZogUtils.printLog(DoCheckPost.class, "checkPostBeforeReply From:" + activity.getClass().getName());

        ThreadHttp.checkPost(activity, fid, new StringCallback(activity) {
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);

                CheckPostJson checkPostJson = ClanUtils.parseObject(json, CheckPostJson.class);
                CheckPostVariables variables = checkPostJson.getVariables();
                String auth = variables.getAuth();
                if (StringUtils.isEmptyOrNullOrNullStr(auth)) {
                    onFailed(ctx, NO_LOGIN, null);
                    return;
                }
                Allowperm allowperm = variables.getAllowperm();
                if (allowperm.getAllowReply().equals("1")) {
                    injectDo.doSuccess(checkPostJson);
                } else {
                    String errorMsg = activity.getResources().getString(R.string.not_allow_reply);
                    onFailed(ctx, 1, errorMsg);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(activity, errorCode, errorMsg);
                switch (errorCode) {
                    case NO_LOGIN:
                        ClanUtils.gotoLogin(activity, null, Activity.RESULT_OK
                                , false);
                        break;
                }
            }
        });
    }


    public static void checkPostBeforeNewThread(final FragmentActivity activity, final String fid, final ThreadTypes threadTypes) {

        final int NO_LOGIN = 0;
        ZogUtils.printLog(DoCheckPost.class, "checkPostBeforeNewThread From:" + activity.getClass().getName());

        ThreadHttp.checkPost(activity, fid, new StringCallback(activity) {
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);

                CheckPostJson checkPostJson = ClanUtils.parseObject(json, CheckPostJson.class);
                CheckPostVariables variables = checkPostJson.getVariables();
                String auth = variables.getAuth();
                if (StringUtils.isEmptyOrNullOrNullStr(auth)) {
                    onFailed(ctx, NO_LOGIN, null);
                    return;
                }
                Allowperm allowperm = variables.getAllowperm();

                if (allowperm.getAllowPost().equals("1")) {
                    //发新帖
                    BundleData bundleData = new BundleData();

                    ZogUtils.printError(ThreadHttp.class, "checkPost fid:" + fid);
                    bundleData.put("CheckPostJson", checkPostJson);
                    bundleData.put("fid", fid);
                    bundleData.put("threadTypes", threadTypes);
                    IntentUtils.gotoSingleNextActivity(activity
                            , ThreadNewActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
                } else {
                    String errorMsg = activity.getResources().getString(R.string.not_allow_new_thread);
                    onFailed(ctx, 1, errorMsg);
                }

            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(activity, errorCode, errorMsg);
                switch (errorCode) {
                    case NO_LOGIN:
                        ClanUtils.gotoLogin(activity, null, Activity.RESULT_OK
                                , false);
                        break;
                }
            }
        });
    }


    public static void checkPost(final FragmentActivity activity, final String fid, final ThreadTypes threadTypes, final ThreadDetailJson threadDetailJson, final Post post) {

        final int NO_LOGIN = 0;
        ZogUtils.printLog(DoCheckPost.class, "checkPost From:" + activity.getClass().getName());

        ThreadHttp.checkPost(activity, fid, new StringCallback(activity) {
            public void onSuccess(Context ctx, String json) {
                super.onSuccess(ctx, json);

                CheckPostJson checkPostJson = ClanUtils.parseObject(json, CheckPostJson.class);
                CheckPostVariables variables = checkPostJson.getVariables();
                String auth = variables.getAuth();
                if (StringUtils.isEmptyOrNullOrNullStr(auth)) {
                    onFailed(ctx, NO_LOGIN, null);
                    return;
                }
                Allowperm allowperm = variables.getAllowperm();
                if ((activity instanceof ThreadDetailActivity)) {
                    if (allowperm.getAllowReply().equals("1")) {

                        if (threadDetailJson != null) {
                            //回帖
                            BundleData bundleData = new BundleData();
                            bundleData.put("HotThreadDetailJson", threadDetailJson);
                            bundleData.put("CheckPostJson", checkPostJson);
                            bundleData.put("fid", threadDetailJson.getVariables().getFid());
//                            bundleData.put("position", position);
                            bundleData.put("post", post);

                            IntentUtils.gotoSingleNextActivity(activity
                                    , ThreadReplyActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
                        }
                    } else {
                        String errorMsg = activity.getResources().getString(R.string.not_allow_reply);
                        onFailed(ctx, 1, errorMsg);
                    }
                } else if ((activity instanceof ForumActivity)) {
                    if (allowperm.getAllowPost().equals("1")) {
                        //发新帖
                        BundleData bundleData = new BundleData();

                        ZogUtils.printError(ThreadHttp.class, "checkPost fid:" + fid);
                        bundleData.put("CheckPostJson", checkPostJson);
                        bundleData.put("fid", fid);
                        bundleData.put("threadTypes", threadTypes);
                        IntentUtils.gotoSingleNextActivity(activity
                                , ThreadNewActivity.class, bundleData, ThreadDetailActivity.REQUEST_CODE);
                    } else {
                        String errorMsg = activity.getResources().getString(R.string.not_allow_new_thread);
                        onFailed(ctx, 1, errorMsg);
                    }
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String errorMsg) {
                super.onFailed(activity, errorCode, errorMsg);
                switch (errorCode) {
                    case NO_LOGIN:
                        ClanUtils.gotoLogin(activity, null, Activity.RESULT_OK
                                , false);
                        break;
                }
            }
        });
    }
}
