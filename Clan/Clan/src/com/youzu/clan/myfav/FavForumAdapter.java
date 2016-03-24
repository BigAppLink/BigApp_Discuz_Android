package com.youzu.clan.myfav;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.FavForumJson;
import com.youzu.clan.base.json.favforum.Forum;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.main.base.forumnav.ForumnavFragment;

public class FavForumAdapter extends BaseRefreshAdapter<FavForumJson> {

	private Context context;
	public FavForumAdapter(Context context, ClanHttpParams params) {
		super(params);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_fav_forum, null);
		}

		TextView forumName = ViewHolder.get(convertView, R.id.forum_name);
		TextView tvThreadNum = ViewHolder.get(convertView, R.id.tvThreadNum);
		TextView tvPostNum = ViewHolder.get(convertView, R.id.tvPostNum);
		TextView countText = ViewHolder.get(convertView, R.id.count);

		ImageView iconView = ViewHolder.get(convertView, R.id.icon);
		CheckBox checkbox = ViewHolder.get(convertView, R.id.checkbox);



		Forum forum = (Forum) getItem(position);

		String icon = forum.getIcon();
//                    if (!TextUtils.isEmpty(icon)) {
//                        BitmapUtils.display(iconView, icon, R.drawable.ic_forum_default);
//                    }

		forumName.setText(forum.getName());
		tvThreadNum.setText(forum.getThreads());
		tvPostNum.setText(forum.getPosts());

		ZogUtils.printError(ForumnavFragment.class, "icon::::::::" + icon);

		LoadImageUtils.displayNoHolder(context, iconView, icon, R.drawable.ic_forum_default);
		String numTodayPost = context.getString(R.string.num_today_post, StringUtils.get(forum.getTodayposts()));
		countText.setText(numTodayPost);

		String numThread = context.getString(R.string.num_thread, StringUtils.get(forum.getThreads()));
		tvThreadNum.setText(numThread);

		String numPost = context.getString(R.string.num_post, StringUtils.get(forum.getPosts()));
		tvPostNum.setText(numPost);

		checkbox.setVisibility(isEditable() ? View.VISIBLE : View.GONE);


		return convertView;

	}
}