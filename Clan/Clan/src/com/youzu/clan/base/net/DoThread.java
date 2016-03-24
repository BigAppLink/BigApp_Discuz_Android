package com.youzu.clan.base.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.kit.app.UIHandler;
import com.kit.utils.MessageUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.thread.inner.Post;
import com.youzu.clan.base.json.threadview.ThreadDetailJson;
import com.youzu.clan.base.util.ClanUtils;

import java.util.LinkedHashSet;

/**
 * Created by Zhao on 15/8/20.
 */
public class DoThread {

    public static final int REPLY_POST_OK = 50 + 1;
    public static final int REPLY_MAIN_OK = 50 + 2;
    public static final int SEND_FAIL = 50 + 3;

    /**
     * 回复主题
     *
     * @param activity
     * @ fid：用户在哪个板块回复帖子或主题
     * @ tid：用户回复的是哪个主题
     * @ attachnew[109][description]：本次回复内容携带一个附件，附件ID为109
     * @ noticetrimstr：如果不是回复顶楼，那么需要携带noticetrimstr参数，该参数需要客户端将被回复楼层的信息摘要提取出来，放在[quote] [/quote]中，其中格式和底色建议如示例所示，不做强硬要求。
     * @ eppid/repost：用户目前回复的是哪篇帖子，如果是回复主题（即顶楼）的话，把这两个参数删除即可；
     */
    public static void send(FragmentActivity activity, final Handler.Callback handler,
                            String message, ThreadDetailJson threadDetailJson, final Post post,
                            LinkedHashSet<String> attaches) {
        ThreadHttp.sendThreadReply(activity
                , message, threadDetailJson, post, attaches, new StringCallback(activity) {

            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                if (message.getMessageval().contains(MessageVal.POST_REPLY_SUCCEED)) {
                    if (post != null) {
                        UIHandler.sendMessage(handler, REPLY_POST_OK);
                    } else
                        UIHandler.sendMessage(handler, REPLY_MAIN_OK);
                } else {
                    ZogUtils.printError(DoThread.class, "message.getMessagestr():" + message.getMessagestr());
                    String msgStr = message.getMessagestr();

                    UIHandler.sendMessage(handler, 0, 0, msgStr, SEND_FAIL, null);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
//                MessageUtils.sendMessage(handler, SEND_FAIL);
                UIHandler.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);

            }
        });
    }



    /**
     * 回复主题
     *
     * @param activity
     * @ fid：用户在哪个板块回复帖子或主题
     * @ tid：用户回复的是哪个主题
     * @ attachnew[109][description]：本次回复内容携带一个附件，附件ID为109
     * @ noticetrimstr：如果不是回复顶楼，那么需要携带noticetrimstr参数，该参数需要客户端将被回复楼层的信息摘要提取出来，放在[quote] [/quote]中，其中格式和底色建议如示例所示，不做强硬要求。
     * @ eppid/repost：用户目前回复的是哪篇帖子，如果是回复主题（即顶楼）的话，把这两个参数删除即可；
     */
    public static void send(FragmentActivity activity, final Handler handler,
                            String message, ThreadDetailJson threadDetailJson, final Post post,
                            LinkedHashSet<String> attaches) {
        ThreadHttp.sendThreadReply(activity
                , message, threadDetailJson, post, attaches, new StringCallback(activity) {

            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                if (message.getMessageval().contains(MessageVal.POST_REPLY_SUCCEED)) {
                    if (post != null) {
                        MessageUtils.sendMessage(handler, REPLY_POST_OK);
                    } else
                        MessageUtils.sendMessage(handler, REPLY_MAIN_OK);
                } else {
                    ZogUtils.printError(DoThread.class, "message.getMessagestr():" + message.getMessagestr());
                    String msgStr = message.getMessagestr();

                    MessageUtils.sendMessage(handler, 0, 0, msgStr, SEND_FAIL, null);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
//                MessageUtils.sendMessage(handler, SEND_FAIL);
                MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);

            }
        });
    }


    /**
     * 回复主题
     *
     * @param activity
     * @param fid：用户在哪个板块回复帖子或主题
     * @param subject：主题标题
     * @param attaches：本次回复内容携带一个附件，附件ID为109
     * @param message：主题内容
     */
    public static void newThread(FragmentActivity activity, final Handler handler, String fid, String typeId, String subject,
                                 String message, Object attaches) {


        ThreadHttp.newThread(activity, fid, typeId, subject, message, attaches, new StringCallback(activity) {
                    @Override
                    public void onSuccess(Context ctx, String s) {
                        super.onSuccess(ctx, s);

                        ClanUtils.dealMsg(activity, s, MessageVal.POST_NEWTHREAD_SUCCEED, R.string.new_thread_success, R.string.new_thread_fail, this, false, true, new InjectDo<BaseJson>() {
                            @Override
                            public boolean doSuccess(BaseJson baseJson) {
                                Intent intent = new Intent();
                                activity.setResult(Activity.RESULT_OK, intent);
                                activity.finish();
                                return true;
                            }

                            @Override
                            public boolean doFail(BaseJson baseJson, String tag) {
                                return false;
                            }
                        });
                    }

                    @Override
                    public void onFailed(Context cxt, int errorCode, String msg) {
                        ZogUtils.printError(DoThread.class, "newThread onFailed onFailed onFailed");
                        super.onFailed(activity, errorCode, msg);
//                        msg = ClanBaseUtils.dealFail(activity,msg);
                        MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);
                    }
                }


        );
    }


}
