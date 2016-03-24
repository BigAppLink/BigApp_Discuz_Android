package cn.sharesdk.applist;

import java.util.ArrayList;

import m.framework.ui.widget.asyncview.AsyncImageView;
import m.framework.ui.widget.asyncview.BitmapProcessor;
import m.framework.ui.widget.pulltorefresh.PullToRefreshListAdapter;
import m.framework.ui.widget.pulltorefresh.PullToRefreshView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.sharesdk.analysis.R;
import cn.sharesdk.analysis.model.PostResult;
import cn.sharesdk.applist.APPDataList.APP;
import cn.sharesdk.net.NetworkHelper;
import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;

public class APPAdapter extends PullToRefreshListAdapter implements Callback {
	public static final int MSG_LIST_GOT = 1;
	public static final int MSG_AFTER_SHARE = 3;
	public static final int PAGE_SIZE = 10;
	//private NetworkThread network;
	private PRTHeader llHeader;
	private static  ArrayList<APP> applist;
	private LinearLayout llLoading;
	private Handler handler;
	
	public APPAdapter(PullToRefreshView view) {
		super(view);
		//network = new NetworkThread();
		applist = new ArrayList<APP>();
		getListView().setDivider(new ColorDrawable());
		getListView().setDividerHeight(0);
	}
	
//	public APPList getCommentList() {
//		return clData;
//	}
	
//	public void readCacheOrRequst() {
//		getParent().performPulling(false);
//		Message msg = new Message();
//		msg.what = NetworkThread.READ_CACHE_OR_REQUST;
//		msg.obj = this;
//		network.handler.sendMessage(msg);
//	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (position >= applist.size()-1 && applist.size() <= totalNum) {
			if (llLoading == null) {
				llLoading = new LinearLayout(getContext());
				llLoading.setOrientation(LinearLayout.VERTICAL);
				
				LinearLayout llInner = new LinearLayout(getContext());
				LinearLayout.LayoutParams lpInner = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lpInner.gravity = Gravity.CENTER_HORIZONTAL;
				llLoading.addView(llInner, lpInner);
				
				ProgressBar pbRefreshing = new ProgressBar(getContext());
				int dp_32 = DeviceHelper.getInstance(getContext()).dipToPx(32);;
				LinearLayout.LayoutParams lpIv = new LinearLayout.LayoutParams(dp_32, dp_32);
				lpIv.gravity = Gravity.CENTER_VERTICAL;
				llInner.addView(pbRefreshing, lpIv);
				
				TextView tvLoading = new TextView(parent.getContext());
				tvLoading.setText(R.string.analysissdk_lv_loading_next);
				tvLoading.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				tvLoading.setGravity(Gravity.CENTER);
				int dp_10 = DeviceHelper.getInstance(getContext()).dipToPx(10);;
				tvLoading.setPadding(dp_10, dp_10, dp_10, dp_10);
				tvLoading.setTextColor(0xff000000);
				LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lpTv.gravity = Gravity.CENTER_VERTICAL;
				llInner.addView(tvLoading, lpTv);			
			}
			onNext();
			return llLoading;
		}
		
		if (convertView == null || convertView.equals(llLoading)) {
			convertView = new APPItemView(this);
		}
		APPItemView itemView = (APPItemView) convertView;
		itemView.setData(position);
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}

	public APP getItem(int position) {		
		return applist == null ? null : applist.get(position);
	}

	public int getCount() {
		return applist == null ? 0 : applist.size();
	}

	public View getHeaderView() {
		if (llHeader == null) {
			llHeader = new PRTHeader(getContext());
		}
		return llHeader;
	}

	public void onPullDown(int percent) {
		llHeader.onPullDown(percent);
	}

	public void onRequest() {
		llHeader.onRequest();		
		curPage = 0;
		applist.clear();
		onNext();
//		Message msg = new Message();
//		msg.what = NetworkThread.ON_NEXT;
//		msg.obj = this;
//		network.handler.sendMessage(msg);
	}
	
	public void onReversed() {
		llHeader.reverse();
	}

	private void onNext() {
		// TODO 
		String appkey = DeviceHelper.getInstance(getContext()).getAppKey();
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("appkey", appkey));
		
		PostResult post = NetworkHelper.httpPost("http://192.168.1.195/statistics.sharesdk.cn/api/index.php/client/app/index", pairs);

		if (post!=null && post.isSuccess()) {
			parseResponseData(post.getResponseMsg());
		} else {
			Ln.e("error", post.getResponseMsg());
		}
	}
	
	private boolean parseResponseData(String jsonMsg) {
		try {
			JSONObject object = new JSONObject(jsonMsg);
			int status = Integer.parseInt(object.getString("status"));
			if (status == 200) {
				ArrayList<APP> data = new ArrayList<APP>();
				object = object.optJSONObject("res");
				if(object != null){
					JSONArray array = object.optJSONArray("item");
					if(array != null){
						for(int i = 0; i < array.length(); i ++){
							object = array.getJSONObject(i);
							APP app = new APP();
							app.id = String.valueOf(object.optString("id"));
							app.title = String.valueOf(object.optString("app_name"));
							app.text = String.valueOf(object.optString("description"));
							app.iconUrl = String.valueOf(object.optString("icon"));
							app.downloadUrl = String.valueOf(object.optString("download_url"));
							data.add(app);
						}
						totalNum = data.size();
					}
				}
				
				if (data != null && data.size() > 0) {
					curPage ++;
					Message msg = new Message();
					msg.what = MSG_LIST_GOT;
					msg.obj = data;
					handler.sendMessage(msg);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case MSG_LIST_GOT: {
				ArrayList<APP> pageData = (ArrayList<APP>) msg.obj;
				if (pageData != null) {
					applist.addAll(pageData);
				}				
				notifyDataSetChanged();
			}
			break;
		}
		return false;
	}	
	
	/**填充数据*/
	private static class APPItemView extends RelativeLayout implements OnClickListener {
		
		private AsyncImageIcon imv_icon;
		private ImageView imv_download;
		private TextView tv_title;
		private TextView tv_msg;
		private APP app;
		private APPAdapter adapter;

		public APPItemView(APPAdapter adapter) {
			super(adapter.getContext());
			View view = inflate(adapter.getContext(), R.layout.analysissdk_app_list_item, null);
			this.addView(view);
			this.adapter = adapter;
			imv_icon = (AsyncImageIcon) view.findViewById(R.id.imv_icon);
			imv_download = (ImageView) view.findViewById(R.id.imv_download);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_msg = (TextView) view.findViewById(R.id.tv_msg);
			
		}
		
		public void setData(int position) {
			//TODO 设置listview的view
			app = adapter.getItem(position);
			tv_title.setText(app.title);
			tv_msg.setText(app.text);
			imv_download.setOnClickListener(this);

			if (adapter.isFling()) {
				Bitmap bm = BitmapProcessor.getBitmapFromCache(app.iconUrl);
				if (bm != null && !bm.isRecycled()) {
					imv_icon.setImageBitmap(bm);
				} else {
					imv_icon.execute(null, AsyncImageView.DEFAULT_TRANSPARENT);
				}
			} else {
				imv_icon.execute(app.iconUrl);
			}
		}
		
		public void onClick(View v) {
			//TODO 下载应用
		}
		
	}
	
	// TODO curPage 需要做处理
	public static int curPage = 0;
	public static int totalNum = 0;

	public void setHandler(Handler handler) {
		this.handler = handler;		
	}
	
}
