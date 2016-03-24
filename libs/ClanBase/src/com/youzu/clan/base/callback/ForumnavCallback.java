package com.youzu.clan.base.callback;

import android.content.Context;

import com.youzu.clan.base.json.ForumnavJson;

public abstract class ForumnavCallback extends HttpCallback<ForumnavJson>{

	@Override
	public abstract void onSuccess(Context ctx,ForumnavJson t);
	@Override
	public abstract void onFailed(Context cxt,int errorCode, String errorMsg);

}
