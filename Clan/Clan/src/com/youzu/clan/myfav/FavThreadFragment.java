package com.youzu.clan.myfav;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kit.utils.StringUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.IntentUtils;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.EditableFragment;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.favthread.Thread;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.RefreshListView;
import com.youzu.clan.thread.detail.ThreadDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏帖子
 * 
 * @author wangxi
 *
 */
public class FavThreadFragment extends EditableFragment {

	private RefreshListView mListView;
	private FavThreadAdapter mAdapter;
	private OnDataSetChangedObserver mObserver;
	
	public FavThreadFragment() {
	}
	
	public FavThreadFragment(OnDataSetChangedObserver observer) {
		mObserver = observer;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RefreshListView listView = (RefreshListView) inflater.inflate(R.layout.fragment_list, container, false);
		ViewUtils.inject(this, listView);
		ClanHttpParams params = new ClanHttpParams(getActivity());
		params.addQueryStringParameter("module", "myfavthread");
		FavThreadAdapter adapter = new FavThreadAdapter(getActivity(), params);
		listView.setAdapter(adapter);
		listView.setOnEditListener(this);
		mListView = listView;
		mAdapter = adapter;
		adapter.setOnDataSetChangedObserver(mObserver);
		return listView;
	}

	@OnItemClick(R.id.list)
	public void itemClick(AdapterView<?> parent, View view, int position, long id) {
    	ZogUtils.printLog(FavThreadFragment.class, "position--->" + position
                + " adapter.getCount()：" + mAdapter.getCount());
    	com.youzu.clan.base.json.favthread.Thread thread = (Thread) mAdapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putString("tid", thread.getId());

        IntentUtils.gotoNextActivity(getActivity(), ThreadDetailActivity.class, bundle);
	}
	
	/**
	 * 删除
	 */
	@Override
	public void onDelete() {
		if (mListView.getCheckedItemCount() < 1) {
			return;
		}
		final List<Thread> threads = getDeletedThreads();
		ClanHttpParams params = getDeleteParams(threads);
		BaseHttp.post(Url.DOMAIN, params, new ProgressCallback<BaseJson>(getActivity()) {
			@Override
			public void onSuccess(Context ctx,BaseJson t) {
				super.onSuccess(ctx,t);
				Message message = t.getMessage();
				if (message != null && "favorite_delete_succeed".equals(message.getMessageval())) {
					mListView.deleteChoices();
					ToastUtils.show(getActivity(), R.string.delete_success);
					deleteLocalFavTheads(threads);
					return;
				}
				String value = message.getMessageval();
				onFailed(ctx,-1, value);
			}

			@Override
			public void onFailed(Context cxt, int errorCode, String msg) {
				super.onFailed(getActivity(),errorCode, msg);
				ToastUtils.show(getActivity(), TextUtils.isEmpty(msg) ? getString(R.string.delete_failed) : msg);
			}
		});
	}
	
	/**
	 * 删除本地数据库中的帖子
	 * @param threads
	 */
	public void deleteLocalFavTheads(List<Thread> threads) {
		for (Thread thread:threads) {
			MyFavUtils.deleteThread(getActivity(), thread.getId());
		}
	}

	/**
	 * 拼接删除的参数
	 * 
	 * @return
	 */
	private ClanHttpParams getDeleteParams(List<Thread> threads) {
		
		ClanHttpParams params = new ClanHttpParams(getActivity());
		params.addQueryStringParameter("iyzmobile", "1");
		params.addQueryStringParameter("module", "delfav");
		for (Thread thread:threads) {
			if (!TextUtils.isEmpty(thread.getFavid())) {
				params.addQueryStringParameter("favorite[]", thread.getFavid());
			}
		}
		params.addBodyParameter("delfavorite", "true");


		if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(getActivity())))
			params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(getActivity()));
		return params;
	}

	private ArrayList<Thread> getDeletedThreads() {
		SparseBooleanArray array = mListView.getChoicePostions();
		int headers = mListView.getRefreshableView().getHeaderViewsCount();
		int count = mAdapter.getCount();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = headers; i < count + headers; i++) {
			if (array.get(i)) {
				Thread thread = (Thread) mAdapter.getItem(i-headers);
				threads.add(thread);
			}
		}
		return threads;
	}

	@Override
	public RefreshListView getListView() {
		return mListView;
	}


}
