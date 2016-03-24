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
public class PlateAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeConfigItem> plates;

    public PlateAdapter(Context context, ArrayList<HomeConfigItem> plates) {
        this.context = context;
        this.plates = plates;
    }

    @Override
    public int getCount() {
        return plates.size();
    }

    @Override
    public HomeConfigItem getItem(int position) {

        return plates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_plate, null);
        }

        ImageView ivIcon = ViewHolder.get(convertView, R.id.ivIcon);
        TextView tvTitle = ViewHolder.get(convertView, R.id.tvTitle);
        TextView tvDescription = ViewHolder.get(convertView, R.id.tvDescription);

        LoadImageUtils.displayCropCirlce(context, ivIcon, plates.get(position).getPic());
        tvTitle.setText(plates.get(position).getTitle());
        tvDescription.setText(plates.get(position).getDesc());

        convertView.setOnClickListener(new OnHomeConfigItemOnClickListener(context,getItem(position)));
        return convertView;
    }
}
