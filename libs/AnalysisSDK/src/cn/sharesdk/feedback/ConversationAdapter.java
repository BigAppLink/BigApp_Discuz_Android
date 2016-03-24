package cn.sharesdk.feedback;

import java.util.ArrayList;
import java.util.Collections;

import m.framework.ui.widget.pulltorefresh.PullToRefreshListAdapter;
import m.framework.ui.widget.pulltorefresh.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.sharesdk.analysis.R;
import cn.sharesdk.feedback.model.Reply;
import cn.sharesdk.feedback.model.Store;
import cn.sharesdk.utils.Ln;

public class ConversationAdapter extends PullToRefreshListAdapter implements Callback,FeedbackListener {
	public static final int GET_SERVER_MSG = 0;
	public static final int MSG_LIST_GOT = 1;
	public static final int PAGE_SIZE = 5;
	public static final long CHAT_TIME_SHOW = 10 * 60 * 1000;


	private Handler handler;

	private Store store;
	private PRTHeader llHeader;
	private static ArrayList<Reply> conversationlist;
	
	// TODO curPage 需要做处理
	private int curPage = 1;
	
	public ConversationAdapter(PullToRefreshView view) {
		super(view);
		conversationlist = new ArrayList<Reply>();
		getListView().setDivider(new ColorDrawable());
		getListView().setDividerHeight(0);
		store = Store.getInstance(getContext());
		FeedbackAgent.setFeedbackListener(this);
	}
	
	public ArrayList<Reply> getConversations(){
		return conversationlist;
	}

	public void addNewRelyToList(Reply reply) {
		if(conversationlist != null){
			conversationlist.add(reply);
			sortConversationData();
		}
	}

	// public void readCacheOrRequst() {
	// getParent().performPulling(false);
	// Message msg = new Message();
	// msg.what = NetworkThread.READ_CACHE_OR_REQUST;
	// msg.obj = this;
	// network.handler.sendMessage(msg);
	// }
	
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		Reply reply = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.analysissdk_fb_chat_item, null);
			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tvToMsg = (TextView) convertView.findViewById(R.id.tv_to_msg);
			holder.tvFromMsg = (TextView) convertView.findViewById(R.id.tv_from_msg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//间隔一分钟，显示时间
		if(reply.showTime){
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText(reply.getDateString());
		}else{
			holder.tvTime.setVisibility(View.GONE);
		}
		
		if (reply.type == Reply.TYPE.REPLY) {
			holder.tvFromMsg.setText(reply.content);
			holder.tvFromMsg.setVisibility(View.VISIBLE);
			holder.tvToMsg.setVisibility(View.GONE);
		}else {
			holder.tvToMsg.setText(reply.content);
			holder.tvToMsg.setVisibility(View.VISIBLE);
			holder.tvFromMsg.setVisibility(View.GONE);
		}
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}

	public Reply getItem(int position) {
		return conversationlist == null ? null : conversationlist.get(position);
	}

	public int getCount() {
		return conversationlist == null ? 0 : conversationlist.size();
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
		onNext();
	}

	public void onReversed() {
		llHeader.reverse();
	}

	private void onNext() {		
		//if (totalNum == 0 || totalNum > (curPage-1) * PAGE_SIZE ) {
			// get feedback date from server
			Ln.d("page == pagesize ", curPage +"==="+ PAGE_SIZE);
			FeedbackAgent.getHistoryConversation(getContext(), curPage, PAGE_SIZE);
//		}else {
//			getParent().stopPulling();
//		}
	}

	private ArrayList<Reply> parseResponseData(String jsonMsg) {
		ArrayList<Reply> data = new ArrayList<Reply>();
		if (TextUtils.isEmpty(jsonMsg)) {
			return data;
		}
		try {
			JSONObject object = new JSONObject(jsonMsg);
			int status = object.optInt("status");
			if (status == 200) {
				object = object.optJSONObject("res");
				if (object != null) {
					JSONArray array = object.optJSONArray("feedback_data");
					if (array != null) {
						for (int i = 0; i < array.length(); i++) {
							object = array.getJSONObject(i);
							Reply reply = new Reply(object);
							data.add(reply);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
			case GET_SERVER_MSG:
				parseResponseData(String.valueOf(msg.obj));
				break;
			case MSG_LIST_GOT: 
				ArrayList<Reply> pageData = parseResponseData(String.valueOf(msg.obj));				
				if (pageData != null && pageData.size() > 0) {
					//之前有本地缓存的，删除
					if(curPage == 1 && conversationlist.size() > 0){
						conversationlist.clear();
					}
					curPage++;
					conversationlist.addAll(0, pageData);
					sortConversationData();
				}
				if(conversationlist == null || conversationlist.size() == 0){
					// 先获取本地缓存，然后再请求服务器
					conversationlist = store.getCoversations();
				}
				notifyDataSetChanged();
				break;
		}
		return true;
	}

	private class ViewHolder {
		TextView tvTime;
		TextView tvToMsg;
		TextView tvFromMsg;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	//排列数据，然后判断是否显示时间，大于10分钟显示
	private void sortConversationData(){
		Collections.sort(conversationlist);
		long curSystemTime = 0;
		for (Reply reply : conversationlist) {
			if(reply.datetime.getTime() - curSystemTime > CHAT_TIME_SHOW){
				curSystemTime = reply.datetime.getTime();
				reply.showTime = true;
			}else{
				reply.showTime = false;
			}
		}
	}

	@Override
	public void onError(String jsonStr) {
		Ln.e("get feedback from server happen error ==>>", jsonStr);
		Message msg = new Message();
		msg.what = MSG_LIST_GOT;
		msg.obj = null;
		handler.sendMessage(msg);
	}

	@Override
	public void onComplete(String jsonStr) {
		Message msg = new Message();
		msg.what = MSG_LIST_GOT;
		msg.obj = jsonStr;
		handler.sendMessage(msg);
	}

}
