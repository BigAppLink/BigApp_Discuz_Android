package com.youzu.clan.search;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kit.imagelib.widget.imageview.circleimageview.CircleImageView;
import com.youzu.clan.R;
import com.youzu.clan.base.json.mypm.Mypm;
import com.youzu.clan.base.json.search.SearchUserJson;
import com.youzu.clan.base.json.search.User;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.AppSPUtils;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.jump.JumpChatUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;

import java.util.ArrayList;

public class SearchUserAdapter extends BaseRefreshAdapter<SearchUserJson> {
	public FragmentActivity act;
	ArrayList<User> users;
	public SearchUserAdapter(FragmentActivity act,ClanHttpParams params) {
        super(params);
		this.act = act;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            convertView = View.inflate(act, R.layout.item_search_user, null);
        }
        TextView userName = ViewHolder.get(convertView, R.id.userName);
        TextView userPosition = ViewHolder.get(convertView, R.id.userPosition);
        TextView sendMessage = ViewHolder.get(convertView, R.id.sendMessage);
        CircleImageView avatar = ViewHolder.get(convertView, R.id.avatar);

		final User user=(User)getItem(position);
		userName.setText(user.getUsername());
		userPosition.setText(user.getGroupname());
        LoadImageUtils.displayNoHolder(act, avatar, user.getAvatar(), R.drawable.ic_profile_nologin_default);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mypm mypm = new Mypm();
                mypm.setMsgfromidAvatar(AppSPUtils.getAvatartUrl(act));
                mypm.setMsgtoidAvatar(user.getAvatar());
                mypm.setTousername(user.getUsername());
                mypm.setTouid(user.getUid());

                JumpChatUtils.gotoChat(act, mypm);

            }
        });
		return convertView;
	}
}
