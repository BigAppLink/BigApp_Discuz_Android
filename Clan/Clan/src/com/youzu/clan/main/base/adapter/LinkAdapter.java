package com.youzu.clan.main.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youzu.clan.R;
import com.youzu.clan.base.json.homeconfig.HomeConfigItem;
import com.youzu.clan.base.util.LoadImageUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.main.base.listener.OnHomeConfigItemOnClickListener;

import java.util.ArrayList;

/**
 * Created by Zhao on 15/6/30.
 */
public class LinkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeConfigItem> links;

    public LinkAdapter(Context context, ArrayList<HomeConfigItem> links) {
        this.context = context;
        this.links = links;
    }

    @Override
    public int getCount() {
        return links.size();
    }

    @Override
    public HomeConfigItem getItem(int position) {

        return links.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_link, null);
        }

        ImageView image = ViewHolder.get(convertView, R.id.image);
        TextView title = ViewHolder.get(convertView, R.id.title);

        LoadImageUtils.displayInsideCircle(context, image, links.get(position).getPic());
        title.setText(links.get(position).getTitle());
        convertView.setOnClickListener(new OnHomeConfigItemOnClickListener(context, getItem(position)));
        return convertView;
    }
}
