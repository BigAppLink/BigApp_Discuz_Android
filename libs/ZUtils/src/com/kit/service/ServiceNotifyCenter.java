package com.kit.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class ServiceNotifyCenter extends Service {

	Timer notifyTimer;
	TimerTask task;
	// Timer timer = new Timer(true);

	// FrequencyChangedReceiver frequencyChangedReceiver;

	public int onCompleted = 0;

	@Override
	public void onCreate() {

		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		System.out.println("ServiceNotifyCenter onStart");

		if (task != null) {
			task.cancel(); // 将原任务从队列中移除
		}

		if (notifyTimer != null) {
			notifyTimer.cancel();
		}

		task = new TimerTask() {
			public void run() {

			}
		};

		notifyTimer = new Timer();

		notifyTimer.schedule(task, 0, 30 * 60 * 1000);

		super.onStart(intent, startId);

	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onDestroy() {
		System.out.println("ServiceNotifyCenter onDestroy");

		// unregisterReceiver(frequencyChangedReceiver);

		if (task != null) {
			task.cancel(); // 将原任务从队列中移除
		}

		if (notifyTimer != null) {
			notifyTimer.cancel();
		}

		super.onDestroy();
	}

	public void clearSharedPerference(String type) {
		SharedPreferences sp = getSharedPreferences("notify", 0);
		sp.edit().putString(type, "").commit();

	}

	public void setCount(String type) {
	}

}
