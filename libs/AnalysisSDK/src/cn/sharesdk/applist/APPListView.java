package cn.sharesdk.applist;

import m.framework.ui.widget.pulltorefresh.PullToRefreshView;
import android.content.Context;
import android.util.AttributeSet;

public class APPListView extends PullToRefreshView {
	public APPListView(Context context) {
		super(context);
		//init(context);
	}
	
	public APPListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//init(context);
	}
	
	public APPListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//init(context);
	}
	
//	private void init(Context context) {
//		adapter = new APPAdapter(this);
//		setAdapter(adapter);
//	}
	
//	public void readCacheOrRequst() {
//		adapter.readCacheOrRequst();
//	}
	
//	public void refresh() {
//		Message msg = new Message();
//		msg.what = APPAdapter.MSG_LIST_GOT;
//		UIHandler.sendMessage(msg, adapter);
//	}

}
