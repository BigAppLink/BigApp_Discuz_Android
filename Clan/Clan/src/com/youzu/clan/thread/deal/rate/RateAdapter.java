package com.youzu.clan.thread.deal.rate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.widget.textview.WithSpinnerTextView;
import com.youzu.clan.R;
import com.youzu.clan.base.json.threadview.rate.Rate;
import com.youzu.clan.base.util.StringUtils;
import com.youzu.clan.base.widget.ViewHolder;
import com.youzu.clan.base.widget.list.BaseRefreshAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateAdapter extends BaseRefreshAdapter<ArrayList<Rate>> {

    private Context context;
    ArrayList<Rate> itemList = null;
    List<Rate> itemSelectedList = null;


    private EditText editText;


    public RateAdapter(Context context, ArrayList<Rate> list) {
        super(null);
        this.context = context;
        this.itemList = list;
        itemSelectedList = ListUtils.deepCopy(itemList);
    }


    public void request(final int page, final OnLoadListener listener) {
        loadSuccess(1, itemList);
    }


    @Override
    public Rate getItem(int position) {
        if (ListUtils.isNullOrContainEmpty(itemList))
            return null;
        return itemList.get(position);
    }

    @Override
    public void refresh(OnLoadListener listener) {
        ZogUtils.printError(RateAdapter.class, "refresh");
        super.refresh(listener);
    }


    @Override
    protected List getData(ArrayList<Rate> list) {
        return itemList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        convertView = getViewRate(position, convertView, parent);
        return convertView;
    }


    private View getViewRate(final int position, View convertView, ViewGroup parent) {
        Rate rate = getItem(position);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_rate_post, null);
        }


        WithSpinnerTextView withSpinnerTextView = ViewHolder.get(convertView, R.id.wstv);
        withSpinnerTextView.setTitle(rate.getTitle());
        final ArrayList<String> name = new ArrayList<String>();
        for (int i = Integer.parseInt(rate.getMin()); i <= Integer.parseInt(rate.getMax()); i++) {
            name.add(i + "");
        }
        ArrayAdapter<String> typeMainAdapter = new ArrayAdapter<String>(context, R.layout.item_spinner, name);

        String defalutValue = getDefualtValue(rate.getMin(), rate.getMax());

        withSpinnerTextView.setSpinnerHint(defalutValue);
        withSpinnerTextView.setAdapter(typeMainAdapter);

        ZogUtils.printError(RateAdapter.class, "rate.getTitle():" + rate.getTitle());
        withSpinnerTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                itemList.get(position).setValue(name.get(index));
            }
        });

        TextView tvRateIntervalle = ViewHolder.get(convertView, R.id.tvRateIntervalle);
        TextView tvTodayRemain = ViewHolder.get(convertView, R.id.tvTodayRemain);

        tvRateIntervalle.setText(rate.getMin() + "-" + rate.getMax());
        tvTodayRemain.setText(rate.getTodayleft());

        return convertView;
    }


    public HashMap<String, String> getCommit() {
        HashMap<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < itemList.size(); i++) {
            Rate rate = getItem(i);
            if (rate != null
                    && !StringUtils.isEmptyOrNullOrNullStr(rate.getExtcredits()))
                map.put(rate.getExtcredits()
                        , rate.getValue() == null
                        ? getDefualtValue(rate.getMin(), rate.getMax()) : rate.getValue());
        }

        return map;
    }

    public String getMessage() {
        if (editText == null)
            return null;

        return editText.getText() + "";
    }


    @Override
    public int getCount() {
        return itemList.size();
    }


    /**
     * 得到默认值
     *
     * @param min
     * @param max
     * @return
     */
    private String getDefualtValue(String min, String max) {

        final ArrayList<String> name = new ArrayList<String>();
        for (int i = Integer.parseInt(min); i <= Integer.parseInt(max); i++) {
            name.add(i + "");
        }

        String last = ListUtils.getLast(name);
        if (StringUtils.isEmptyOrNullOrNullStr(last))
            return 1 + "";

        long value = Long.parseLong(last);
        if (value >= 1) {
            return 1 + "";
        } else {
            return name.get(0);
        }
    }
}