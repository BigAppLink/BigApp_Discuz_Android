package com.youzu.clan.base.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.youzu.clan.app.service.ClanService;
import com.youzu.clan.base.common.Action;
import com.youzu.clan.base.common.Key;

public class PollingUtils {

	public static void startCheckNewMessage(Context context,SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
		 PollingUtils.startPolling(context, Action.ACTION_CHECK_NEW_MESSAGE, 60);
		SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_PREFERENCES, context.MODE_PRIVATE);
		mSharedPreferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);
	}

	public static void stopCheckNewMessage(Context context,SharedPreferences.OnSharedPreferenceChangeListener mPreferenceListener) {
		 PollingUtils.stopPolling(context, Action.ACTION_CHECK_NEW_MESSAGE);
		SharedPreferences mSharedPreferences = context.getSharedPreferences(Key.FILE_PREFERENCES, context.MODE_PRIVATE);
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);
	}



	public static void startPolling(Context context, String action, int seconds) {
		final AlarmManager manager = (AlarmManager) context.
				getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(context, ClanService.class);
		intent.setAction(action);
		
		final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		final long triggerAtTime = SystemClock.elapsedRealtime();
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, 
				triggerAtTime, seconds * 1000, pendingIntent);
	}

	public static void stopPolling(Context context, String action) {
		final AlarmManager manager = (AlarmManager) context.
				getSystemService(Context.ALARM_SERVICE);
		final Intent intent = new Intent(context, ClanService.class);
		intent.setAction(action);
		final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		manager.cancel(pendingIntent);
	}

}
