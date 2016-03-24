package com.youzu.clan.base.net;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.kit.utils.MessageUtils;
import com.youzu.clan.base.callback.StringCallback;
import com.youzu.clan.base.enums.MessageVal;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.act.ActPlayer;
import com.youzu.clan.base.json.act.ActPublishInfo;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.util.ClanUtils;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/8/20.
 */
public class DoAct {

    public static final int SEND_PUBLISH_OK = 111 + 1;
    public static final int SEND_FAIL = 111 + 2;
    public static final int SEND_PASS_APPLY_OK = 111 + 3;
    public static final int SEND_REFUSE_APPLY_ZJ_OK = 111 + 4;
    public static final int SEND_REFUSE_APPLY_ZL_OK = 111 + 5;
    public static final int SEND_ACT_APPLY_OK = 111 + 6;
    public static final int SEND_ACT_CANCEL_APPLY_OK = 111 + 7;

    public static void send(FragmentActivity activity, final Handler handler,
                            ActPublishInfo info, Object attaches) {
        ActHttp.sendActPublish(activity, info, attaches, new StringCallback(activity) {
            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                try {
                    BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                    com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                    if (message.getMessageval().contains(MessageVal.POST_NEWTHREAD_SUCCEED)) {
                        MessageUtils.sendMessage(handler, SEND_PUBLISH_OK);
                    } else {
                        String msgStr = message.getMessagestr();
                        MessageUtils.sendMessage(handler, 0, 0, msgStr, SEND_FAIL, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MessageUtils.sendMessage(handler, 0, 0, "请求失败了", SEND_FAIL, null);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
                MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);
            }
        });
    }

    public static void sendActApply(FragmentActivity activity, final Handler handler,
                                    String fid, String tid, String pid, SpecialActivity act, String message) {
        ActHttp.sendActApply(activity, fid, tid, pid, act, message, new StringCallback(activity) {
            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                Log.e("", "wenjun t = " + t);
                String msgStr = "";
                int send_code = SEND_FAIL;
                try {
                    BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                    com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                    msgStr = message.getMessagestr();
                    if (message.getMessageval().contains(MessageVal.ACTIVITY_COMPLETION)) {
                        send_code = SEND_ACT_APPLY_OK;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    send_code = SEND_FAIL;
                    msgStr = "请求失败了";
                }
                Bundle extras = new Bundle();
                extras.putString("apply_result", t);
                MessageUtils.sendMessage(handler, 0, 0, msgStr, send_code, extras);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
                MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);
            }
        });
    }

    public static void cancelApply(FragmentActivity activity, final Handler handler,
                                   String fid, String tid, String pid, String message) {
        ActHttp.cancelApply(activity, fid, tid, pid, message, new StringCallback(activity) {
            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                Log.e("", "wenjun t = " + t);
                String msgStr = "";
                int send_code = SEND_FAIL;
                try {
                    BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                    com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                    msgStr = message.getMessagestr();
                    if (message.getMessageval().contains(MessageVal.ACTIVITY_CANCEL_SUCCESS)) {
                        send_code = SEND_ACT_CANCEL_APPLY_OK;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    send_code = SEND_FAIL;
                    msgStr = "请求失败了";
                }
                Bundle extras = new Bundle();
                extras.putString("cancel_result", t);
                MessageUtils.sendMessage(handler, 0, 0, msgStr, send_code, extras);
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
                MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);
            }
        });
    }

    public static void passApply(FragmentActivity activity, final Handler handler,
                                 String tid, String reason, ArrayList<ActPlayer> applyidarray) {
        sendActApplyReceipt(activity, handler, SEND_PASS_APPLY_OK, MessageVal.ACTIVITY_AUDITING_COMPLETION, "", tid, reason, applyidarray);
    }

    public static void refuseApplyZl(FragmentActivity activity, final Handler handler,
                                     String tid, String reason, ArrayList<ActPlayer> applyidarray) {
        sendActApplyReceipt(activity, handler, SEND_REFUSE_APPLY_ZL_OK, MessageVal.ACTIVITY_AUDITING_COMPLETION, "replenish", tid, reason, applyidarray);
    }

    public static void refuseApplyZj(FragmentActivity activity, final Handler handler,
                                     String tid, String reason, ArrayList<ActPlayer> applyidarray) {
        sendActApplyReceipt(activity, handler, SEND_REFUSE_APPLY_ZJ_OK, MessageVal.ACTIVITY_DELETE_COMPLETION, "delete", tid, reason, applyidarray);
    }

    private static void sendActApplyReceipt(FragmentActivity activity, final Handler handler, final int send_code_ok,
                                            final String success_tag,
                                            String operation,
                                            String tid, String reason, ArrayList<ActPlayer> applyidarray) {
        ActHttp.sendActApplyReceipt(activity, operation, tid, reason, applyidarray, new StringCallback(activity) {
            @Override
            public void onstart(Context cxt) {
                setShowLoading(false);
                super.onstart(cxt);
            }

            @Override
            public void onSuccess(Context ctx, String t) {
                Log.e("", "wenjun t = " + t + ", send_code_ok= " + send_code_ok);
                try {
                    BaseJson baseJson = ClanUtils.parseObject(t, BaseJson.class);
                    com.youzu.clan.base.json.model.Message message = baseJson.getMessage();
                    if (message.getMessageval().contains(success_tag)) {
                        MessageUtils.sendMessage(handler, 0, 0, message.getMessagestr(), send_code_ok, null);
                    } else {
                        String msgStr = message.getMessagestr();
                        MessageUtils.sendMessage(handler, 0, 0, msgStr, SEND_FAIL, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MessageUtils.sendMessage(handler, 0, 0, "请求失败了", SEND_FAIL, null);
                }
            }

            @Override
            public void onFailed(Context cxt, int errorCode, String msg) {
                super.onFailed(activity, errorCode, msg);
                MessageUtils.sendMessage(handler, 0, 0, msg, SEND_FAIL, null);
            }
        });
    }
}
