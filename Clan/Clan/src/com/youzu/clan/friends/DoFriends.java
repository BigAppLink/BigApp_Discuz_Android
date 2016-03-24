package com.youzu.clan.friends;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.utils.ToastUtils;
import com.youzu.android.framework.JsonUtils;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.base.callback.CheckFriendCallback;
import com.youzu.clan.base.callback.HttpCallback;
import com.youzu.clan.base.json.friends.AddFriendsJson;
import com.youzu.clan.base.json.friends.FriendsJson;
import com.youzu.clan.base.net.FriendHttp;
import com.youzu.clan.base.util.ClanUtils;
import com.youzu.clan.base.util.StringUtils;

/**
 * Created by Zhao on 15/7/18.
 */
public class DoFriends {


    public final static  String AGREED_FRIEND="0";
    public final static  String REFUSE_FRIEND="1";
    public final static  int  CHECK_FOR_APPLY_FRIEND=1;
    public final static  int  CHECK_FOR_AGREED_FRIEND=2;
    public final static  int  CHECK_FOR_REFUSE_FRIEND=3;

    /**
     * 获取新的好友申请数
     */
    public static void loadNewFriendCount(final FragmentActivity context,final TextView friendCount) {

        FriendHttp.getFriendCount(context,new HttpCallback<String>() {
            @Override
            public void onSuccess(Context ctx,String s) {
                super.onSuccess(ctx,s);
                FriendsJson json = JsonUtils.parseObject(s, FriendsJson.class);
                if (json != null && json.getVariables() != null
                        && !StringUtils.isEmptyOrNullOrNullStr(json.getVariables().getCount())
                        ) {
                    if (!json.getVariables().getCount().equals("0")) {
                        friendCount.setVisibility(View.VISIBLE);
                        friendCount.setText(json.getVariables().getCount());
                    } else {
                        friendCount.setVisibility(View.GONE);
                        friendCount.setText(json.getVariables().getCount());
                    }
                }
            }

            @Override
            public void onFailed(Context cxt,int errorCode, String errorMsg) {
                super.onFailed(context,errorCode, errorMsg);
            }
        });
    }

    public static void deleteFriend(final FragmentActivity context, final String uid, final DoSomeThing doSomeThing) {

        FriendHttp.removeFriend(context, uid, new CheckFriendCallback(context) {
            @Override
            public void onSuccess(Context ctx,String s) {
                super.onSuccess(ctx,s);
                AddFriendsJson json = JsonUtils.parseObject(s, AddFriendsJson.class);
                if (json != null && json.getVariables() != null) {
                    if ("0".equals(json.getVariables().getStatus())) {
                        doSomeThing.execute(true);
                    } else {
                        doSomeThing.execute(false);
                        com.kit.utils.ToastUtils.mkLongTimeToast(ctx, json.getVariables().getShowMessage());
                    }
                } else {
                    doSomeThing.execute(false);
                }
            }

            @Override
            public void onFailed(Context cxt,int errorCode, String errorMsg) {
                super.onFailed(context,errorCode, errorMsg);
                com.kit.utils.ToastUtils.mkLongTimeToast(cxt, errorMsg);
            }


        });
    }


    public static void checkFriend(final FragmentActivity context, final String uid, final DoSomeThing doSomeThing) {
        checkFriend(context, DoFriends.CHECK_FOR_APPLY_FRIEND, uid, doSomeThing);
    }

    /**
     * type为0时，表示来自申请添加
     * type为1时，表示来自同意添加
     * type为2时，表示来自拒绝添加
     * status为0时，互不为好友，且没法有发送过申请，
     * status为1时，互为好友，
     * status为2时，互不为好友，且对方有发送过申请，
     */
    public static void checkFriend(final FragmentActivity context, final int type, final String uid, final DoSomeThing doSomeThing) {

        FriendHttp.checkFriend(context, uid,type+"", new CheckFriendCallback(context) {
            @Override
            public void onSuccess(Context ctx,String s) {
                super.onSuccess(ctx,s);
                AddFriendsJson json = JsonUtils.parseObject(s, AddFriendsJson.class);
                if (json != null && json.getVariables() != null) {
                    String status = json.getVariables().getStatus();
                    if ("0".equals(status)) {
                        doSomeThing.execute(true, status);
                    } else if ("2".equals(status) && (type ==CHECK_FOR_AGREED_FRIEND||type==CHECK_FOR_REFUSE_FRIEND)) {
                        doSomeThing.execute(true, status);
                    } else {
                        com.kit.utils.ToastUtils.mkShortTimeToast(ctx, json.getVariables().getShowMessage());
                    }
                } else {
                    doSomeThing.execute(false, "");
                }
            }

            @Override
            public void onFailed(Context cxt,int errorCode, String errorMsg) {
                super.onFailed(context,errorCode, errorMsg);
                com.kit.utils.ToastUtils.mkLongTimeToast(cxt, errorMsg);
            }
        });
    }

    /**
     * 好友审核
     * uid: 用户ID
     * gid: 好友分组ID（同意时需要设置好友分组）
     * audit: （0:同意,1:拒绝）
     */
    public static void agreedOrRefuseFriend(final FragmentActivity context, String uid, final String audit, final InjectDo injectDo) {
        FriendHttp.agreedOrRefuseFriend(context, uid, audit, new CheckFriendCallback(context) {

            @Override
            public void onSuccess(Context ctx,String s) {
                super.onSuccess(ctx,s);
                ClanUtils.dealMsg(ctx, s, REFUSE_FRIEND.equals(audit) ? "do_success" : "friends_add", R.string.send_success, R.string.send_fail, this, true, true, injectDo);
            }

            @Override
            public void onFailed(Context cxt,int errorCode, String errorMsg) {
                super.onFailed(context,errorCode, errorMsg);
                ToastUtils.mkLongTimeToast(cxt, errorMsg);
            }
        });
    }
}