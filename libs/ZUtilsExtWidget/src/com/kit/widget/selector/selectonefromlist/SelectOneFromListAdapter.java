package com.kit.widget.selector.selectonefromlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kit.app.adapter.ViewHolder;
import com.kit.extend.widget.R;
import com.kit.utils.ListUtils;

import java.util.ArrayList;

public class SelectOneFromListAdapter extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;

    ArrayList<String> str;

    private int selectedPosition;

    public SelectOneFromListAdapter(Context mContext, int selectPosition,
                                    ArrayList<String> str) {
        this.mContext = mContext;
        this.selectedPosition = selectPosition;
        this.str = str;

    }

    @Override
    public int getCount() {

        return str.size();

    }

    @Override
    public Object getItem(int position) {
        return str;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 去掉缓存数据 防止视图重复使用产生多选的情况
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.select_one_from_list_item, null);
        }

        TextView tv = ViewHolder.get(convertView, R.id.tv);
        ImageView iv = ViewHolder.get(convertView, R.id.iv);

        if (!ListUtils.isNullOrEmpty(str))
            tv.setText(str.get(position));

        if (selectedPosition >= 0) {
            if (position == selectedPosition) {
                iv.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }


}
