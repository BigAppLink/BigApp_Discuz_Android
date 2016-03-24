package com.youzu.clan.base.net;

import org.apache.http.client.CookieStore;

import android.content.Context;

public class CookieManager {
	
	private CookieStore mCookieStore;
	private static CookieManager mCookieManager;
	
	private CookieManager() {
	}
	
	public static synchronized CookieManager getInstance() {
		if (mCookieManager == null) {
			mCookieManager = new CookieManager();
		}
		return mCookieManager;
	}
	
	
	public synchronized CookieStore getCookieStore(Context context) {
		if (mCookieStore == null) {
			mCookieStore = new PersistentCookieStore(context);
		}
		return mCookieStore;
	}
	
	
	public void clearCookies() {
		if (mCookieStore != null) {
			mCookieStore.clear();
		}
	}
	
}
