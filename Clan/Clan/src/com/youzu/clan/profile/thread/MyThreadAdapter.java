package com.youzu.clan.profile.thread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzu.clan.R;
import com.youzu.clan.base.json.MyThreadJson;
import com.youzu.clan.base.json.mythread2.MyThread;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.view.threadandarticle.ThreadAndArticleItemUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;

import java.util.Collections;
import java.util.List;

public class MyThreadAdapter extends BaseRefreshAdapter<MyThreadJson> {

    private Context context;
    private String iconUrl;

    public MyThreadAdapter(Context context, ClanHttpParams params) {
        super(params);
        this.context = context;
    }

    @Override
    protected List getData(MyThreadJson t) {
        if (t == null) {
            return null;
        }
        iconUrl = t.getAvatar();
        if (t.getData() == null) {
        	return Collections.EMPTY_LIST;
        }
        return t.getData();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_thread, null);
        }
        MyThread thread = (MyThread) getItem(position);

        TextView nameText = ViewHolder.get(convertView, R.id.name);
        TextView dateText = ViewHolder.get(convertView, R.id.date);
        TextView contentText = ViewHolder.get(convertView, R.id.content);
        TextView viewText = ViewHolder.get(convertView, R.id.view);
        TextView replyText = ViewHolder.get(convertView, R.id.reply);
        TextView forumText = ViewHolder.get(convertView, R.id.forum);
        TextView lastPostText = ViewHolder.get(convertView, R.id.last_post);

        nameText.setText(thread.getAuthor());
        dateText.setText(StringUtils.get(thread.getDateline()));
        contentText.setText(StringUtils.get(thread.getSubject()));
        viewText.setText(StringUtils.get(thread.getViews()));
        replyText.setText(StringUtils.get(thread.getReplies()));
        forumText.setText(StringUtils.get(thread.getForumName()));
        lastPostText.setText(StringUtils.get(thread.getForumName()));

        String lastPoster = context.getString(R.string.last_poster, StringUtils.get(thread.getLastposter()), StringUtils.get(thread.getLastpost()));
        lastPostText.setText(lastPoster);

        ImageView iconImage = ViewHolder.get(convertView, R.id.icon);
        LoadImageUtils.displayMineAvatar(context, iconImage, iconUrl);
        
        String tid = thread.getTid();
		boolean hasRead = ThreadAndArticleItemUtils.hasRead(context, tid);
		int colorRes = context.getResources().getColor(hasRead?R.color.text_black_selected:R.color.text_black_ta_title);
		nameText.setTextColor(colorRes);

        return convertView;

    }

}