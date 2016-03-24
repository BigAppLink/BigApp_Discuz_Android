package com.youzu.clan.main.base.forumnav;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kit.utils.ZogUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.util.jump.JumpForumUtils;
import com.youzu.clan.base.widget.list.RefreshListView;

import java.util.ArrayList;

public class ForumChildrenFragment extends BaseFragment {

    private ForumnavAdapter mAdapter;
    @ViewInject(R.id.list)
    private RefreshListView mListView;
    private Activity act;

    private ArrayList<NavForum> forums;
    public int type = 2;
    public static final int EMPTY_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int OTHER_TYPE = 2;
    private ForumParentFragment forumParentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView =  (RefreshListView)inflater.inflate(R.layout.fragment_list, container, false);
        ViewUtils.inject(this, mListView);
        ForumnavAdapter adapter = new ForumnavAdapter(getActivity(), null, forumParentFragment.getAdapter());
        mListView.setAutoRefresh(false);
        mListView.setAdapter(adapter);
        mAdapter = adapter;
        ZogUtils.printError(ForumChildrenFragment.class, "ForumChildrenFragment onCreateView");
        act = getActivity();
        initType();

        return mListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ZogUtils.printError(ForumChildrenFragment.class, "ForumChildrenFragment onResume");
    }

    private void initType() {
        if(mListView==null){
            return;
        }
        if (type == EMPTY_TYPE) {
            showEmpty();
        } else if (type == ERROR_TYPE) {
            showError();
        } else {
            setData(forums);
        }
    }

    public void setForums(ArrayList<NavForum> forums) {
        this.forums = forums;
    }

    public void setForumParentFragment(ForumParentFragment forumParentFragment) {
        this.forumParentFragment = forumParentFragment;
    }

    public void setType(int type) {
        this.type = type;
        initType();
    }

    public void showEmpty() {
        clearData();
        mListView.getmEmptyView().showEmpty();
    }

    public void showError() {
        clearData();
        mListView.getmEmptyView().showError();
    }

    public void setData(ArrayList<NavForum> forums) {
        if (forums == null) {
            clearData();
            return;
        }

        mListView.onRefreshComplete();
        ZogUtils.printError(ForumChildrenFragment.class, "mListView onRefreshComplete");
        mAdapter.setData(forums);
        mAdapter.notifyDataSetChanged();
    }

    private void clearData() {
        mListView.onRefreshComplete();
        if (mAdapter.getData() != null) {
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
        }
    }


    public RefreshListView getListView() {
        return mListView;
    }

    @OnItemClick(R.id.list)
    public void itemClick(AdapterView<?> parent, View view, int position, long id) {
        NavForum forum = (NavForum) mAdapter.getItem(position);
//        ClanUtils.showForum(getActivity(), forum);

        JumpForumUtils.gotoForum(getActivity(),forum);
    }
}
