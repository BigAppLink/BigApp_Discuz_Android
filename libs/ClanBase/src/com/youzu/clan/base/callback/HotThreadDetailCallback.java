package com.youzu.clan.base.callback;

import android.support.v4.app.FragmentActivity;

import com.youzu.clan.base.json.threadview.ThreadDetailJson;

public  class HotThreadDetailCallback extends ProgressCallback<ThreadDetailJson>{

	public HotThreadDetailCallback(FragmentActivity activity) {
		super(activity);
	}
}
