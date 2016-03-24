package cn.sharesdk.feedback.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

public class Store {

	private Context context;
	private static Store stroe = null;
	

	private Store(Context context) {
		this.context = context.getApplicationContext();
	}

	public static Store getInstance(Context context) {
		if (stroe == null && context != null) {
			stroe = new Store(context);
		}
		return stroe;
	}

	public void saveCoversation(ArrayList<Reply> conversations) {
		JSONArray array = new JSONArray();
		for(Reply reply : conversations){
			array.put(reply.toJson());
		}
		context.getSharedPreferences("analysissdk_feedback_conversations", 0).edit().putString("conversation", array.toString()).commit();		
	}

	public ArrayList<Reply> getCoversations() {
		ArrayList<Reply> conversations = new ArrayList<Reply>();
		String history = context.getSharedPreferences("analysissdk_feedback_conversations", 0).getString("conversation", "");
		if(!TextUtils.isEmpty(history)){
			try {
				JSONArray array = new JSONArray(history);
				for(int i = 0; i < array.length(); i++){
					JSONObject reply = array.optJSONObject(i);
					conversations.add(new Reply(reply));
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conversations;
	}

	public void saveUserInfo(UserInfo userInfo) {
		context.getSharedPreferences("analysissdk_feedback_user_info", 0).edit().putString("user", userInfo.toJson().toString()).commit();
	}

	public UserInfo getUserInfo() {
		String str = context.getSharedPreferences("analysissdk_feedback_user_info", 0).getString("user", "");

		if (TextUtils.isEmpty(str))
			return null;
		try {
			return new UserInfo(new JSONObject(str));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
