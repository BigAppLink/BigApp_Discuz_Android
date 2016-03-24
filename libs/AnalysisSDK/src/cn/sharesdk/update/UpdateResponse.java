package cn.sharesdk.update;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateResponse implements Serializable{
	private static final long serialVersionUID = 8418344410988244639L;
	
	public String time;
	public String content;
	public String path;
	public String md5;
	public int size;
	public int status;
	public int version;

	public UpdateResponse(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return;
		}
		status = jsonObject.optInt("status");
		version = jsonObject.optInt("version");
		time = jsonObject.optString("time", "");
		content = jsonObject.optString("content", "");
		path = jsonObject.optString("path", "");
		md5 = jsonObject.optString("md5", "");
		size = jsonObject.optInt("size");
	}
	
	public int getStatus(int currentVersionCode){		
//		return UpdateStatus.Yes;
		if(status == 1 && version > currentVersionCode){
			return UpdateStatus.Yes;
		}else{
			return UpdateStatus.No;
		}
	}
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("status", status);
			object.put("version", version);
			object.put("time", time);
			object.put("content", content);
			object.put("path", path);
			object.put("md5", md5);
			object.put("size", size);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
