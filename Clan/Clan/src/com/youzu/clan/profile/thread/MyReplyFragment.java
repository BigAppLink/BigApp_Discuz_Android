package com.youzu.clan.profile.thread;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.BaseFragment;
import com.youzu.clan.base.json.mythread2.Reply;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.jump.JumpThreadUtils;
import com.youzu.clan.base.widget.list.RefreshListView;

/**
 * 我的回帖
 * 
 * @author wangxi
 *
 */
public class MyReplyFragment extends BaseFragment {
	private String mUid;
	private MyReplyAdapter mAdapter;
	public MyReplyFragment(){}

	public MyReplyFragment(String uid) {
		mUid = uid;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RefreshListView listView = (RefreshListView) inflater.inflate(R.layout.fragment_list, container, false);
		listView.getRefreshableView().setDivider(null);
		ViewUtils.inject(this, listView);
		ClanHttpParams params = new ClanHttpParams(getActivity());
		params.addQueryStringParameter("module", "mythread2");
		params.addQueryStringParameter("iyzmobile", "1");
		params.addQueryStringParameter("type", "reply");
		if (!TextUtils.isEmpty(mUid)) {
			params.addQueryStringParameter("uid", mUid);
		}
		MyReplyAdapter adapter = new MyReplyAdapter(getActivity(), params);
		listView.setAdapter(adapter);
		mAdapter = adapter;
		return listView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}
	
	@OnItemClick(R.id.list)
	public void itemClick(AdapterView<?> parent, View view, int position, long id) {
		final Reply reply = (Reply) mAdapter.getItem(position);
		if (reply == null) {
			return;
		}
		final com.youzu.clan.base.json.forumdisplay.Thread thread = reply.getThread();
		if (thread == null) {
			return;
		}
		JumpThreadUtils.gotoThreadDetail(getActivity(),thread.getTid());
	}
}
