package com.youzu.clan.friends;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.app.core.task.DoSomeThing;
import com.kit.imagelib.widget.imageview.circleimageview.CircleImageView;
import com.youzu.clan.R;
import com.youzu.clan.app.InjectDo;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.friends.Friends;
import com.youzu.clan.base.json.friends.FriendsJson;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.jump.JumpChatUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;

/**
 * Created by tangh on 2015/7/15.
 */
public class FriendsAdapter extends BaseRefreshAdapter<FriendsJson> {
    private FragmentActivity act;
    private String module;

    public FriendsAdapter(FragmentActivity context, String module, ClanHttpParams params) {
        super(params);
        this.module = module;
        this.act = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.item_friends, null);
        }
        TextView userName = ViewHolder.get(convertView, R.id.userName);
        TextView userPosition = ViewHolder.get(convertView, R.id.userPosition);
        CircleImageView avatar = ViewHolder.get(convertView, R.id.avatar);
        LinearLayout newFriendsAdd = ViewHolder.get(convertView, R.id.newFriendsAdd);

        final Friends user = (Friends) getItem(position);

        if (FriendsModule.FRIENDS.equals(module) || FriendsModule.LOCAL_SEARCH.equals(module)) {
            newFriendsAdd.setVisibility(View.GONE);
            dealMyFriendsView(convertView, user, position);
        } else if (FriendsModule.NEW_FRIENDS.equals(module)) {
            dealNewFriendView(convertView, user);
        } else {
            dealAddFriendView(convertView, user, position);
        }

        userName.setText(user.getUsername());
        userPosition.setText(user.getGroupname());
        LoadImageUtils.displayNoHolder(act, avatar, user.getAvatar()
                , act.getResources().getDrawable(R.drawable.ic_subject_default_image));

        return convertView;
    }

    private void gotoApplyActivity(final Friends user, final int position) {

        DoFriends.checkFriend(act, DoFriends.CHECK_FOR_APPLY_FRIEND,user.getUid(), new DoSomeThing() {
            @Override
            public void execute(Object... objects) {
                boolean isCan = (boolean) objects[0];
                if (isCan) {
                    Intent intent = new Intent(act, ApplyFriendsActivity.class);
                    intent.putExtra(Key.KEY_UID, user.getUid());
                    intent.putExtra("position", position);
                    act.startActivityForResult(intent, AddFriendsActivity.GOTO_APPLY);
                }
            }
        });
    }

    /**
     * 好友的特殊处理
     */
    public void dealMyFriendsView(View convertView, final Friends user, final int position) {
        TextView sendMessage = ViewHolder.get(convertView, R.id.sendMessage);
        sendMessage.setVisibility(View.VISIBLE);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpChatUtils.gotoChat(act, user);

            }
        });
    }

    /**
     * 添加好友的特殊处理
     */
    private void dealAddFriendView(View convertView, final Friends user, final int position) {
        TextView agreeFriend = ViewHolder.get(convertView, R.id.agreeFriend);
        TextView agreedFriend = ViewHolder.get(convertView, R.id.agreedFriend);
        TextView refuseFriend = ViewHolder.get(convertView, R.id.refuseFriend);

        agreeFriend.setVisibility(user.isFriends() ? View.GONE : View.VISIBLE);
        refuseFriend.setVisibility(View.GONE);
        agreedFriend.setVisibility(user.isFriends() ? View.VISIBLE : View.GONE);

        agreeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoApplyActivity(user, position);
            }
        });

    }

    /**
     * 好友申请的特殊处理
     */
    private void dealNewFriendView(View convertView, final Friends user) {

        TextView agreeFriend = ViewHolder.get(convertView, R.id.agreeFriend);
        TextView agreedFriend = ViewHolder.get(convertView, R.id.agreedFriend);
        TextView refuseFriend = ViewHolder.get(convertView, R.id.refuseFriend);

        agreeFriend.setVisibility(user.isFriends() ? View.GONE : View.VISIBLE);
        refuseFriend.setVisibility(user.isFriends() ? View.GONE : View.VISIBLE);
        agreedFriend.setVisibility(user.isFriends() ? View.VISIBLE : View.GONE);

        agreeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFriends( DoFriends.CHECK_FOR_AGREED_FRIEND,user, DoFriends.AGREED_FRIEND);
            }
        });
        refuseFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFriends( DoFriends.CHECK_FOR_REFUSE_FRIEND,user, DoFriends.REFUSE_FRIEND);
            }
        });
    }

    private void checkFriends(int type,final Friends user, final String audit) {
        DoFriends.checkFriend(act,type, user.getUid(), new DoSomeThing() {
            @Override
            public void execute(Object... objects) {
                boolean isCan = (boolean) objects[0];
                if (isCan) {
                    agreeFriend(user, audit);
                }
            }

        });
    }

    /**
     * 好友审核
     * uid: 用户ID
     * gid: 好友分组ID（同意时需要设置好友分组）
     * audit: （0:同意,1:拒绝）
     */
    private void agreeFriend(final Friends user, final String audit) {
        DoFriends.agreedOrRefuseFriend(act, user.getUid(), audit, new InjectDo<BaseJson>() {
                    @Override
                    public boolean doSuccess(BaseJson baseJson) {
                        if ("1".equals(audit)) {
                            getData().remove(user);
                        } else {
                            user.setIsfriends("1");
                        }
                        notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public boolean doFail(BaseJson baseJson, String tag) {
                        return true;
                    }
                }
        );
    }

}
