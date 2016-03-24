/**
 * ************************************************************
 * ShareSDKStatistics
 * An open source analytics android sdk for mobile applications
 * ************************************************************
 * 
 * @package		ShareSDK Statistics
 * @author		ShareSDK Limited Liability Company
 * @copyright	Copyright 2014-2016, ShareSDK Limited Liability Company
 * @since		Version 1.0
 * @filesource  https://github.com/OSShareSDK/OpenStatistics/tree/master/Android
 *  
 * *****************************************************
 * This project is available under the following license
 * *****************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.sharesdk.analysis.model;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;
import cn.sharesdk.utils.PreferencesHelper;

public class PostEvent {

	private String event_id;
	private String label;
	private String acc;
	private String time;
	private String activity;
	private String session_id;

	private long duration;
	private PreferencesHelper dbHelper;
	private DeviceHelper deviceHelper;
	private HashMap<String, String> stringMap;

	public PostEvent(Context context, String event_id, String label, String acc, long duration, HashMap<String, String> stringMap) {
		super();
		this.event_id = event_id;
		this.label = label;
		this.acc = acc;
		this.duration = duration;
		this.stringMap = stringMap;
		dbHelper = PreferencesHelper.getInstance(context);
		deviceHelper = DeviceHelper.getInstance(context);

		this.time = deviceHelper.getTime();
		this.activity = deviceHelper.getActivityName();
		this.session_id = dbHelper.getSessionID();

	}

	public boolean verification() {
		if (this.getAcc().contains("-") || this.getAcc() == null || this.getAcc().equals("")) {
			Ln.d("test", this.getAcc());
			return false;
		} else {
			return true;
		}
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}

	public HashMap<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(HashMap<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public JSONObject eventToJOSNObj() {
		if (getStringMap() == null) {
			return getEventJOSNobj();
		} else {
			return getEventKVJOSNobj();
		}
	}

	private JSONObject getEventJOSNobj() {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("session_id", session_id);
			localJSONObject.put("create_date", time);
			localJSONObject.put("eventkey", event_id);
			localJSONObject.put("notice_num", acc);
			localJSONObject.put("page", activity);
			if (!TextUtils.isEmpty(label))
				localJSONObject.put("label", label);

			if (getDuration() != 0)
				localJSONObject.put("duration", duration);

		} catch (JSONException localJSONException) {
			Ln.e("ShareSDK Statistics", "json error in PostObjEvent getEventJOSNobj");
			localJSONException.printStackTrace();
		}
		return localJSONObject;
	}

	private JSONObject getEventKVJOSNobj() {

		JSONObject localJSONObject = new JSONObject();
		try {

			localJSONObject.put("session_id", session_id);
			localJSONObject.put("create_date", time);
			localJSONObject.put("eventkey", event_id);
			localJSONObject.put("notice_num", acc);
			localJSONObject.put("page", activity);
			if (!TextUtils.isEmpty(label))
				localJSONObject.put("label", label);

			if (getDuration() != 0)
				localJSONObject.put("duration", duration);
			
			int count = 1;
			Set<String> keySet = getStringMap().keySet();
			for (String key : keySet) {
				if(count >10)
					break;
				localJSONObject.put(key, getStringMap().get(key));
				count ++;
			}

		} catch (JSONException localJSONException) {
			Ln.e("ShareSDK Statistics", "json error in PostObjEvent getEventJOSNobj");
			localJSONException.printStackTrace();
		}
		return localJSONObject;
	}

}
