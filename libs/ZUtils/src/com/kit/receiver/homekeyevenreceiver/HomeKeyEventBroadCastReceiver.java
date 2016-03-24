package com.kit.receiver.homekeyevenreceiver;

import com.kit.config.AppConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
	static final String SYSTEM_REASON = "reason";
	static final String SYSTEM_HOME_KEY = "homekey";// home key
	static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra(SYSTEM_REASON);
			if (reason != null) {
				if (reason.equals(SYSTEM_HOME_KEY)) {
					// home key处理点

					System.out.println("Home home home");

					Intent intentLOCKNOW = new Intent();
					intentLOCKNOW.setAction(AppConfig.LOCKNOW);
					context.sendBroadcast(intentLOCKNOW);

				} else if (reason.equals(SYSTEM_RECENT_APPS)) {
					// long home key处理点

					System.out.println("Long long Home home home");

					Intent intentLOCKNOW = new Intent();
					intentLOCKNOW.setAction(AppConfig.LOCKNOW);
					context.sendBroadcast(intentLOCKNOW);
				}
			}
		}
	}
}