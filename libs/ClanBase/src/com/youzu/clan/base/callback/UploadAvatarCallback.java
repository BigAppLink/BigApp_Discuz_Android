package com.youzu.clan.base.callback;

import android.support.v4.app.FragmentActivity;

import com.youzu.clan.base.json.VariablesJson;

public abstract class UploadAvatarCallback extends ProgressCallback<VariablesJson>{

	public UploadAvatarCallback(FragmentActivity activity) {
		super(activity);
	}
}
