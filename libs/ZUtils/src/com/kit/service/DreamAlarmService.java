package com.kit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DreamAlarmService extends Service {

	/**
	 * 响铃动作
	 */
	public static final String ALARM_INTENT_ACTION = "com.zhao.dreamalarm.MUSIC";

	/**
	 * 响铃时间
	 */
	public static final int ALARM_TIME = 3 * 60 * 1000;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	public void onDestroy() {
		super.onDestroy();

	}

}