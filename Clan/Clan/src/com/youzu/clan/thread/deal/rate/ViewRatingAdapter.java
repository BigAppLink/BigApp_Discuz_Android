package com.youzu.clan.thread.deal.rate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.json.threadview.rate.ViewRating;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

public class ViewRatingAdapter extends BaseRefreshAdapter<ArrayList<ViewRating>> {

    private Context context;
    ArrayList<ViewRating> itemList = null;


    public ViewRatingAdapter(Context context, ArrayList<ViewRating> list) {
        super(null);
        this.context = context;
        this.itemList = list;
    }


    public void request(final int page, final OnLoadListener listener) {
        loadSuccess(1, itemList);
    }


    @Override
    public ViewRating getItem(int position) {
        if (ListUtils.isNullOrContainEmpty(itemList))
            return null;
        return itemList.get(position);
    }

    @Override
    public void refresh(OnLoadListener listener) {
        ZogUtils.printError(ViewRatingAdapter.class, "refresh");
        super.refresh(listener);
    }


    @Override
    protected List getData(ArrayList<ViewRating> list) {
        return itemList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        convertView = getViewViewRating(position, convertView, parent);
        return convertView;
    }


    private View getViewViewRating(final int position, View convertView, ViewGroup parent) {
        ViewRating rate = getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_view_rating, null);
        }


        TextView tvName = ViewHolder.get(convertView, R.id.tvName);
        TextView tvScore = ViewHolder.get(convertView, R.id.tvScore);
        TextView tvScoreTitle = ViewHolder.get(convertView, R.id.tvScoreTitle);
        TextView tvReason = ViewHolder.get(convertView, R.id.tvReason);
        tvName.setText(rate.getUsername());
        tvScore.setText(rate.getScore());
        tvScoreTitle.setText(rate.getCredit());

        if (!StringUtils.isEmptyOrNullOrNullStr(rate.getReason()))
            tvReason.setText(rate.getReason());
        else
            tvReason.setText(context.getResources().getString(R.string.nothing));

        return convertView;
    }


    @Override
    public int getCount() {
        return itemList.size();
    }
}