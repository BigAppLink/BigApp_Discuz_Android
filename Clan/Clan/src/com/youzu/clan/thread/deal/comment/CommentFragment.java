package com.youzu.clan.thread.deal.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.utils.FragmentUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.app.constant.Key;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.enums.JoinFiledType;
import com.youzu.clan.base.json.threadview.comment.CommentField;
import com.youzu.clan.base.widget.list.RefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * 点评帖子
 */
public class CommentFragment extends BaseFragment {

    private CommentAdapter mAdapter;

    @ViewInject(R.id.list)
    private RefreshListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (RefreshListView) inflater.inflate(R.layout.fragment_list, container, false);
        ViewUtils.inject(this, mListView);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);

        BundleData bundleData = FragmentUtils.getData(this);

        ZogUtils.printObj(CommentFragment.class, bundleData);

        Type type = new TypeToken<ArrayList<CommentField>>() {
        }.getType();
        ArrayList<CommentField> list = bundleData.getArrayList(Key.CLAN_DATA,type);

        ZogUtils.printError(CommentFragment.class, "list.size():" + list.size());

        mAdapter = new CommentAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
        return mListView;
    }


    public RefreshListView getListView() {
        return mListView;
    }


    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {


        int type = mAdapter.getItemViewType(position);
        CommentField commentField =  mAdapter.getItem(position);

        switch (type) {
            case JoinFiledType.SELECT_ONE:

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


    public CommentAdapter getAdapter() {
        return mAdapter;
    }
}
