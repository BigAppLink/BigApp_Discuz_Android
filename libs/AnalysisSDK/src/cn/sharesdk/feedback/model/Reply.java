package cn.sharesdk.feedback.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Reply implements Comparable<Reply> {
	private static final String a = Reply.class.getName();

	public String content;
	public String content_id;
	public TYPE type;
	public Date datetime;
	//是否显示时间
	public boolean showTime = false;

	public Reply(String content, String content_id, TYPE type) {
		this.content = content;
		this.content_id = content_id;
		this.type = type;
		this.datetime = new Date();
	}

	public Reply(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return;
		}
		content = jsonObject.optString("content", "");
		content_id = jsonObject.optString("content_id", "");
		try {
			type = TYPE.get(jsonObject.getString("type"));
		} catch (Exception localException) {
			throw new JSONException(localException.getMessage());
		}

		try {
			datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("create_date"));
		} catch (ParseException localParseException1) {
			try {
				datetime = new SimpleDateFormat().parse(jsonObject.getString("create_date"));
			} catch (ParseException localParseException2) {
				localParseException2.printStackTrace();
				Log.e(a, "Reply(JSONObject json): error parsing datetime from json " + jsonObject.optString("create_date", "") + ", using current Date instead.");
				datetime = new Date();
			}
		}

	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("content", content);
			object.put("content_id", content_id);
			object.put("type", type);
			object.put("create_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(datetime));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public int compareTo(Reply paramReply) {
		return this.datetime.compareTo(paramReply.datetime);
	}

	public String getContentID() {
		return content_id;
	}

	public String getDateString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(datetime);
	}

	protected static String getReplyID() {
		StringBuilder builder = new StringBuilder();
		builder.append("FB");
		builder.append(System.currentTimeMillis());
		builder.append(String.valueOf((int)(1000.0D + Math.random() * 9000.0D)));
		return builder.toString();
	}

	public static enum TYPE {
		FEEDBACK, REPLY;

		public static TYPE get(String paramString) {
			String type = paramString.toUpperCase();
			if (FEEDBACK.toString().equals(type))
				return FEEDBACK;
			if (REPLY.toString().equals(type))
				return REPLY;
			throw new RuntimeException(paramString + "Cannot convert " + paramString + " to enum " + TYPE.class.getName());
		}
	}
}