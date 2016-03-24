package com.youzu.clan.base.util.jump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.kit.utils.intentutils.IntentUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleItemUtils;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

/**
 * Created by Zhao on 15/9/9.
 */
public class JumpThreadUtils {
    /**
     * 跳转到帖子详情
     *
     * @param context
     */
    public static void gotoThreadDetail(Context context, String tid) {
        if (TextUtils.isEmpty(tid)) {
            return;
        }
        ThreadAndArticleItemUtils.saveReadTid(context, tid);

        Bundle bundle = new Bundle();
        bundle.putString("tid", tid);

        IntentUtils.gotoNextActivity(context, ThreadDetailActivity.class, bundle);
    }


    /**
     * 帖子详情Intent
     *
     * @param context
     */
    public static Intent getThreadDetailIntent(Context context, String tid) {
        Intent intent = new Intent(context, ThreadDetailActivity.class);
        intent.putExtra("tid", tid);
        return intent;
    }




}
