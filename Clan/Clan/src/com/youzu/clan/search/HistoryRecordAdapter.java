package com.youzu.clan.search;

import java.util.ArrayList;
import java.util.Set;

import com.kit.utils.DensityUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.util.AppSPUtils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryRecordAdapter extends BaseAdapter {
    public Activity act;
    private ArrayList<String> datas;
    private HistoryRecordCallback callback;

    public HistoryRecordAdapter(Activity act, ArrayList<String> datas, HistoryRecordCallback callback) {
        this.act = act;
        this.datas = datas;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        if (datas == null || datas.size() == 0) {
            return 0;
        }
        return datas.size() + 2;
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
       final Horder h;
        if (convertView == null) {
            h = new Horder();
            convertView = LayoutInflater.from(act).inflate(R.layout.item_search_history_record, null);
            h.hint = (TextView) convertView.findViewById(R.id.hint);
            h.record = (TextView) convertView.findViewById(R.id.record);
            h.deleteRecord = (TextView) convertView.findViewById(R.id.deleteRecord);
            h.recordLayout = (RelativeLayout) convertView.findViewById(R.id.recordLayout);
            h.addRecord = (ImageView) convertView.findViewById(R.id.addRecord);
            h.line = convertView.findViewById(R.id.line);
            convertView.setTag(h);
        } else {
            h = (Horder) convertView.getTag();
        }
        h.hint.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        h.deleteRecord.setVisibility(position == getCount() - 1 ? View.VISIBLE : View.GONE);
        h.line.setVisibility(View.VISIBLE);
        if (position != getCount() - 1 && position != 0) {
            h.recordLayout.setVisibility(View.VISIBLE);
            h.record.setText(datas.get(datas.size()-position));
            h.line.setPadding(DensityUtils.dip2px(act, 20f), 0, 0, 0);
        } else {
            h.recordLayout.setVisibility(View.GONE);
            if (position == 0) {
                h.line.setPadding(0, 0, 0, 0);
            } else {
                h.line.setVisibility(View.GONE);
            }
        }
        h.deleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.clear();
                AppSPUtils.setSearchHistoryRecord(act, datas);
                notifyDataSetChanged();
            }
        });
        h.recordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(true, h.record.getText().toString());
            }
        });
        h.addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(false, h.record.getText().toString());
            }
        });
        return convertView;
    }

    class Horder {

        TextView hint, record, deleteRecord;
        RelativeLayout recordLayout;
        ImageView addRecord;
        View line;
    }

    interface HistoryRecordCallback {
        public void callback(boolean isSearch, String key);
    }
}
