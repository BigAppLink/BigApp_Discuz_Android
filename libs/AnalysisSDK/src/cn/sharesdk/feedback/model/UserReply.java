package cn.sharesdk.feedback.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserReply extends Reply{

	public UserReply(JSONObject paramJSONObject) throws JSONException {
		super(paramJSONObject);
		if (this.type != Reply.TYPE.FEEDBACK)
		      throw new JSONException(UserReply.class.getName() + ".type must be " + Reply.TYPE.FEEDBACK);
	}
	
	public UserReply(String content){
		super(content, getReplyID(), Reply.TYPE.FEEDBACK);
	}
	
}
