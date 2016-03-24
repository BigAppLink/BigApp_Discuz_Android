package com.kit.widget.listview.reboundlistview;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.kit.extend.widget.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.ZogUtils;
import com.kit.widget.listview.reboundlistview.ReboundListView.IPullListViewListener;

public class ReboundListViewSampleActivity extends BaseActivity implements
		IPullListViewListener {

	private Handler mHandler = new Handler();

	private ReboundListView mListView;

	private ArrayList<String> list = new ArrayList<String>();

	private ArrayAdapter arrayAdapter;

	private int pageIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private ArrayList<String> getList(int start, int end) {
		ArrayList<String> mlist = new ArrayList<String>();
		for (int i = 0; i < end - start; i++) {
			mlist.add("Title" + (start + i));
		}

		return mlist;
	}

	public void getData(int index) {

		ArrayList<String> templist = new ArrayList<String>();

		ArrayList<String> mlist = getList(15 * (index - 1), 15 * index);

		templist.addAll(list);
		templist.addAll(mlist);

		list.clear();

		list.addAll(templist);

		templist.clear();

		mListView.stopLoadMore();

		arrayAdapter.notifyDataSetChanged();

	}

	public boolean initWidget() {

		mContext = ReboundListViewSampleActivity.this;

		setContentView(R.layout.rebound_listview_activity);

		initListView();

		return true;

	}

	private void initListView() {

		list = getList(15 * (pageIndex - 1), 15 * pageIndex);

		mListView = (ReboundListView) findViewById(R.id.reboundlistview);

		// 注意setPullToRefreshListViewListener一定要在setPullLoadEnable、setPullRefreshEnable之上setPullToRefreshListViewListener的onRefresh、onLoadMore才会生效
		mListView.setPullListViewListener(this);
		// mListView.setPullLoadEnable(false);

		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);

		mListView.setAdapter(arrayAdapter);

	}

//	private void onLoad() {
//		mListView.stopRefresh();
//		mListView.stopLoadMore();
//		mListView
//				.setRefreshTime(DateUtils.getCurrDateFormat("yyy-MM-dd HH:mm"));
//	}

	// @Override
	// public void onRefresh() {
	// mHandler.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	//
	// // mAdapter.notifyDataSetChanged();
	//
	// onLoad();
	// }
	// }, 2000);
	//
	// }

	@Override
	public void onLoadMore() {

		ZogUtils.printLog(getClass(), "onLoadMore()");

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageIndex++;
				getData(pageIndex);
			}
		}, 2000);

	}
}
