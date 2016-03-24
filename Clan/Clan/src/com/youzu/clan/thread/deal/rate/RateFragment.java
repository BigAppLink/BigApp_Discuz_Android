package com.youzu.clan.thread.deal.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.kit.utils.FragmentUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.listview.MiniListView;
import com.kit.widget.selector.selectonefromlist.SelectOneFromListActivity;
import com.kit.widget.selector.selectonefromlist.SelectOneFromListConstant;
import com.kit.widget.textview.GoBackTextView;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.threadview.rate.Rate;
import com.youzu.clan.base.json.threadview.rate.RateCheckJson;
import com.youzu.clan.base.util.StringUtils;

import java.util.ArrayList;


/**
 * 帖子评分
 */
public class RateFragment extends BaseFragment {

    private RateAdapter mAdapter;

    private MiniListView mListView;
    RateCheckJson rateCheckJson;


    @ViewInject(R.id.gtv)
    private GoBackTextView gtv;


    @ViewInject(R.id.et)
    private EditText et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread_deal, container, false);
        ViewUtils.inject(this, view);
        mListView = (MiniListView) view.findViewById(R.id.listview);
        BundleData bundleData = FragmentUtils.getData(this);

        ZogUtils.printObj(RateFragment.class, bundleData);
        rateCheckJson = bundleData.getObject(Key.CLAN_DATA, RateCheckJson.class);
        ArrayList<Rate> list = rateCheckJson.getVariables().getRateList();


        ZogUtils.printError(RateFragment.class, "list.size():" + list.size());

        mAdapter = new RateAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
        return view;
    }


    public ListView getListView() {
        return mListView;
    }


    @OnClick(R.id.gtv)
    public void selectReason(View view) {
        ArrayList choices = rateCheckJson.getVariables().getReasons();

        if (ListUtils.isNullOrContainEmpty(choices)) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ACTIVITY_TITLE, getString(R.string.select_rate_reason));
        bundle.putStringArrayList(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ITEMS_ARRAYLIST, choices);
        bundle.putInt(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_POSITION, 0);
        IntentUtils.gotoNextActivity(this, SelectOneFromListActivity.class, bundle, SelectOneFromListConstant.ACTIVITY_ON_RESULT_SELECT_ONE_FROM_LIST_REQUEST);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZogUtils.printError(RateFragment.class, "resultCode:" + resultCode + " requestCode:" + requestCode);
        if (SelectOneFromListConstant.ACTIVITY_ON_RESULT_SELECT_ONE_FROM_LIST_REQUEST == requestCode) {
            switch (resultCode) {
                case SelectOneFromListConstant.ACTIVITY_ON_RESULT_SELECT_ONE_FROM_LIST_RESULT:
                    Bundle bundle = data.getExtras();
                    int p = bundle.getInt(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_POSITION, 0);
                    String value = bundle.getString(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_ITEM_STRING);

                    String reason = StringUtils.isEmptyOrNullOrNullStr(value) ? "" : value;
                    et.setText(reason);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void refresh() {
        super.refresh();
    }


    public RateAdapter getAdapter() {
        return mAdapter;
    }


    public String getReason() {
//        reason = StringUtils.isEmptyOrNullOrNullStr(reason) ? "" : reason;

        String etStr = et.getText().toString();
        String reason = (StringUtils.isEmptyOrNullOrNullStr(etStr))
                ? "" : etStr;
        return reason;
    }
}
