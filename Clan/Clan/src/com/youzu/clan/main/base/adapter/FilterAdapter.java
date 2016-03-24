package com.youzu.clan.main.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youzu.clan.R;
import com.youzu.clan.base.json.config.content.ThreadConfigItem;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.util.theme.ThemeUtils;
import com.youzu.clan.base.widget.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tangh on 2015/8/27.
 */
public class FilterAdapter extends BaseAdapter {
    private ArrayList<ThreadConfigItem> threadConfigItems;
    private int forumFilterSelectIndex = 0;
    private Context context;
    private HashMap<String, String> maps = new HashMap<>();
    private String iyzVersion;

    public void setThreadConfigItems(ArrayList<ThreadConfigItem> threadConfigItems) {
        this.threadConfigItems = threadConfigItems;
    }

    public void setForumFilterSelectIndex(int forumFilterSelectIndex) {
        this.forumFilterSelectIndex = forumFilterSelectIndex;
    }

    public FilterAdapter(Context context) {
        this.context = context;
    }

    public void setMaps(HashMap<String, String> maps) {
        this.maps = maps;
    }

    public void setIyzVersion(String iyzVersion) {
        this.iyzVersion = iyzVersion;
    }

    @Override
    public int getCount() {
        if (threadConfigItems == null)
            return 0;
        return threadConfigItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_forum_horizontal, null);
        }
        View colorBar = ViewHolder.get(convertView, R.id.colorBar);
        colorBar.setBackgroundColor(ThemeUtils.getThemeColor(context));
        TextView title = ViewHolder.get(convertView, R.id.title);
        ThreadConfigItem item=threadConfigItems.get(position);
        title.setText(getFilterItemTitle(item));

        if (position == forumFilterSelectIndex) {
            colorBar.setVisibility(View.GONE);
            title.setTextColor(context.getResources().getColor(R.color.text_black_ta_title));
        } else {
            colorBar.setVisibility(View.GONE);
            title.setTextColor(context.getResources().getColor(R.color.text_black_selected));
        }
        return convertView;
    }

    private String getFilterItemTitle(ThreadConfigItem threadConfigItem) {
        if (StringUtils.isEmptyOrNullOrNullStr(iyzVersion)) {
            //兼容旧的插件
            if (IndexPageAdapter.TYPE_ARTICLE_FILTER.equals(threadConfigItem.getType())) {
                return threadConfigItem.getTitle();
            }else{
                return maps.get(threadConfigItem.getTitle());
            }
        } else {
            return threadConfigItem.getTitle();
        }
    }
}
