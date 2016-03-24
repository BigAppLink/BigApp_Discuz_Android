package com.kit.widget.listview.pulltorefreshlistviewextend;


/**
 * implements this interface to get refresh/load more event.
 */
public abstract class PullToRefreshListViewListener {

    public PullToRefreshListView pullToRefreshListView;

    public PullToRefreshListViewListener(PullToRefreshListView pullToRefreshListView) {
        this.pullToRefreshListView = pullToRefreshListView;
    }

    public void onRefresh() {
        pullToRefreshListView.preRefresh();
    }


    // public void onRefreshed();

    public void onLoadMore() {
        pullToRefreshListView.preLoadMore();
    }
}