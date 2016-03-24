package cn.sharesdk.feedback.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
	public String userName;
	public String userContact;
	
	public UserInfo(String name, String contact){
		this.userName = name;
		this.userContact = contact;
	}

	public UserInfo(JSONObject paramJSONObject) throws JSONException {
		userName = paramJSONObject.optString("username", "");
		userContact = paramJSONObject.optString("contact", "");
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("username", userName);
			object.put("contact", userContact);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
