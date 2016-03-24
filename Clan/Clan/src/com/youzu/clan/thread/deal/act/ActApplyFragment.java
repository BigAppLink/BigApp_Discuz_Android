package com.youzu.clan.thread.deal.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.utils.ArrayUtils;
import com.kit.utils.FragmentUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;
import com.kit.widget.selector.selectonefromlist.SelectOneFromListActivity;
import com.kit.widget.selector.selectonefromlist.SelectOneFromListConstant;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.enums.JoinFiledType;
import com.youzu.clan.base.json.act.JoinField;
import com.youzu.clan.base.json.act.SpecialActivity;
import com.youzu.clan.base.widget.list.RefreshListView;

import java.util.ArrayList;


public class ActApplyFragment extends BaseFragment {

    private ActApplyAdapter mAdapter;

    @ViewInject(R.id.list)
    private RefreshListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (RefreshListView) inflater.inflate(R.layout.fragment_list, container, false);
        ViewUtils.inject(this, mListView);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);

        BundleData bundleData = FragmentUtils.getData(this);

        ZogUtils.printObj(ActApplyFragment.class, bundleData);

        SpecialActivity specialActivity = bundleData.getObject(Key.CLAN_DATA, SpecialActivity.class);
        ArrayList<JoinField> list = specialActivity.getJoinFields();

        ZogUtils.printError(ActApplyFragment.class, "list.size():" + list.size());

        mAdapter = new ActApplyAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
        return mListView;
    }


    public RefreshListView getListView() {
        return mListView;
    }


    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {


       int type =  mAdapter.getItemViewType(position);
        JoinField joinField = (JoinField) mAdapter.getItem(position);

        switch (type){
            case JoinFiledType.SELECT_ONE:

                if(ArrayUtils.isNullOrContainEmpty(joinField.getChoices())){
                    return;
                }
                ArrayList choices = ArrayUtils.toArrayList(joinField.getChoices());

                Bundle bundle = new Bundle();
                bundle.putString(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ACTIVITY_TITLE, joinField.getTitle());
                bundle.putStringArrayList(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ITEMS_ARRAYLIST, choices);
                bundle.putInt(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_POSITION, 0);
                IntentUtils.gotoNextActivity(getActivity(), SelectOneFromListActivity.class,bundle);

                break;
            case JoinFiledType.SELECT_MULIT:
                break;

            case JoinFiledType.DATEPICKER:
                break;


        }

    }


    @Override
    public void refresh() {
        super.refresh();
        mListView.getRefreshableView().smoothScrollToPosition(0);
        mListView.setRefreshing(true);
        mListView.refresh();
    }
}
