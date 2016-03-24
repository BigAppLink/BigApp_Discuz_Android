package com.youzu.clan.base.callback;

import android.content.Context;

public abstract class RequestCallback {
	public void onStart(){
		
	}
	public abstract void onSuccess();
	public abstract void onFailed(Context context, int errorCode, String msg);
	
	public void onProgress(long total, long current) {
		
	}
	
	
}
