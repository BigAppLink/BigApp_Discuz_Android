package com.kit.receiver.bootreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver implements IBootService {

	public Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		startBootService();

	}

	@Override
	public boolean startBootService() {
		return true;
	}

}