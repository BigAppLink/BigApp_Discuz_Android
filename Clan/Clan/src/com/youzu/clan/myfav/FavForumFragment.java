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
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.event.OnItemClick;
import com.youzu.clan.R;
import com.youzu.clan.base.EditableFragment;
import com.youzu.clan.base.callback.ProgressCallback;
import com.youzu.clan.base.config.Url;
import com.youzu.clan.base.json.BaseJson;
import com.youzu.clan.base.json.favforum.Forum;
import com.youzu.clan.base.json.forum.BaseForum;
import com.youzu.clan.base.json.model.Message;
import com.youzu.clan.base.net.BaseHttp;
import com.youzu.clan.base.net.ClanHttpParams;
import com.youzu.clan.base.util.ClanBaseUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.base.util.jump.JumpForumUtils;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏版块
 * @author wangxi
 *
 */
public class FavForumFragment extends EditableFragment implements OnEditListener{
	private RefreshListView mListView;
	private FavForumAdapter mAdapter;
	private OnDataSetChangedObserver mObserver;
	
	public FavForumFragment() {
	}
	
	public FavForumFragment(OnDataSetChangedObserver observer) {
		mObserver = observer;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RefreshListView listView = (RefreshListView) inflater.inflate(R.layout.fragment_list, container, false);
		ViewUtils.inject(this, listView);
		ClanHttpParams params = new ClanHttpParams(getActivity());
		params.addQueryStringParameter("module", "myfavforum");
		FavForumAdapter adapter = new FavForumAdapter(getActivity(), params);
		listView.setAdapter(adapter);
		listView.setOnEditListener(this);
		mListView = listView;
		mAdapter = adapter;
		adapter.setOnDataSetChangedObserver(mObserver);
		return listView;
	}
	
	@OnItemClick(R.id.list)
	public void itemClick(AdapterView<?> parent, View view, int position, long id) {
		BaseForum forum = (BaseForum) mAdapter.getItem(position);
//		ClanUtils.showForum(getActivity(),forum);
		JumpForumUtils.gotoForum(getActivity(),forum);
	}
	
	/**
	 * 删除
	 */
	@Override
	public void onDelete() {
		if (mListView.getCheckedItemCount() < 1) {
			return;
		}
		final ArrayList<Forum> forums = getDeletedForums();
		ClanHttpParams params = getDeleteParams(forums);
		BaseHttp.post(Url.DOMAIN, params, new ProgressCallback<BaseJson>(getActivity()) {
			@Override
			public void onSuccess(Context ctx, BaseJson t) {
				super.onSuccess(ctx,t);
				Message message = t.getMessage();
				if (message != null && "favorite_delete_succeed".equals(message.getMessageval())) {
					mListView.deleteChoices();
					ToastUtils.show(getActivity(), R.string.delete_success);
					deleteLocalFavForum(forums);
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
	 * 删除本地数据库中的版块
	 * @param forums
	 */
	public void deleteLocalFavForum(List<Forum> forums) {
		for (Forum forum:forums) {
			MyFavUtils.deleteForum(getActivity(), forum.getId());
		}
	}

	/**
	 * 拼接删除的参数
	 * 
	 * @return
	 */
	private ClanHttpParams getDeleteParams(ArrayList<Forum> forums) {

		ClanHttpParams params = new ClanHttpParams(getActivity());
		params.addQueryStringParameter("iyzmobile", "1");
		params.addQueryStringParameter("module", "delfav");
		for (Forum forum:forums) {
			if (forum != null && !TextUtils.isEmpty(forum.getFavid())) {
				params.addQueryStringParameter("favorite[]", forum.getFavid());
			}
		}
		params.addBodyParameter("delfavorite", "true");


		if (!StringUtils.isEmptyOrNullOrNullStr(ClanBaseUtils.getFormhash(getActivity())))
			params.addBodyParameter("formhash", ClanBaseUtils.getFormhash(getActivity()));
		return params;
	}

	
	private ArrayList<Forum> getDeletedForums() {
		final SparseBooleanArray array = mListView.getChoicePostions();
		final int headers = mListView.getRefreshableView().getHeaderViewsCount();
		final int count = mAdapter.getCount();
		final ArrayList<Forum> forums = new ArrayList<Forum>();
		for (int i = headers; i < count + headers; i++) {
			if (array.get(i)) {
				Forum forum = (Forum) mAdapter.getItem(i-headers);
				forums.add(forum);
			}
		}
		return forums;
	}

	
	@Override
	public RefreshListView getListView() {
		return mListView;
	}
	
	
	

	
}
