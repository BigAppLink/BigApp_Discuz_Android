package com.youzu.clan.message.pm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kit.utils.ZogUtils;
import com.youzu.clan.base.widget.list.IRefreshAndEditableAdapter;
import com.youzu.clan.base.widget.list.OnLoadListener;
import com.youzu.clan.base.widget.list.RefreshListView;

public class ChatListView extends RefreshListView {

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setMode(Mode.PULL_FROM_END);
	}

	@Override
	public void refresh() {
		onPullUpToRefresh(this);
	}


	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		IRefreshAndEditableAdapter adapter =  getAdapter();
		if (adapter != null) {
			adapter.loadMore(new OnLoadListener() {
				@Override
				public void onSuccess(boolean hasMore) {

					ZogUtils.printError(ChatListView.class, "onPullDownToRefresh onSuccess");

					clearChoices();
					onRefreshComplete();
					setMode(hasMore ? Mode.BOTH : Mode.PULL_FROM_END);
					if (mEmptyView != null) {
						mEmptyView.showEmpty();
					}
				}
				
				@Override
				public void onFailed() {

					ZogUtils.printError(ChatListView.class, "onPullDownToRefresh onFailed");

					onRefreshComplete();
					if (mEmptyView != null) {
						mEmptyView.showError();
					}
				}
			});
		}
	}
	
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		IRefreshAndEditableAdapter adapter =  getAdapter();
		if (adapter != null) {

			adapter.refresh(new OnLoadListener() {
				@Override
				public void onSuccess(boolean hasMore) {
					onRefreshComplete();
					clearChoices();
					setMode(hasMore ? Mode.BOTH : Mode.PULL_FROM_END);

					if (mEmptyView != null) {
						mEmptyView.showEmpty();
					}
				}
				
				@Override
				public void onFailed() {
					onRefreshComplete();
					if (mEmptyView != null) {
						mEmptyView.showError();
					}
				}
			});
		}
	}


}
