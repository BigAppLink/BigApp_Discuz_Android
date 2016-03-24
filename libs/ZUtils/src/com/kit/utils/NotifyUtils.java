package com.kit.utils;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotifyUtils {

	/**
	 * 
	 * @Title creatResidentNotification
	 * @Description 常驻状态栏的通知,通过view设置点击，可以设置点击时间
	 * @param notificationFlag
	 *            设置常驻状态栏 Notification.FLAG_ONGOING_EVENT
	 * @return int 返回类型
	 */
	@SuppressWarnings("deprecation")
	public static Notification creatResidentNotification(Context context,
			int notificationId, int statusBarIcon, Intent intent,
			RemoteViews view, String notice, int notificationFlag) {

		Notification notification = new Notification(statusBarIcon, notice,
				System.currentTimeMillis());

		notification.contentView = view;
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.flags = notificationFlag;

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		// notification.setLatestEventInfo(context, title, content,
		// contentIntent);

		return notification;
	}

	/**
	 * 
	 * @Title creatResidentNotification
	 * @Description 常驻状态栏的通知,通过view设置点击，可以设置点击时间
	 * @param notificationFlag
	 *            设置常驻状态栏 Notification.FLAG_ONGOING_EVENT
	 * @return int 返回类型
	 */
	@SuppressWarnings("deprecation")
	public static void creatResidentNotification(Context context,
			NotificationManager nm, int notificationId, int statusBarIcon,
			Intent intent, RemoteViews view, String notice, int notificationFlag) {

		Notification notification = new Notification(statusBarIcon, notice,
				System.currentTimeMillis());

		notification.contentView = view;
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.flags = notificationFlag;

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		// notification.setLatestEventInfo(context, title, content,
		// contentIntent);

		nm.notify(notificationId, notification);

	}

	/**
	 * 
	 * @Title creatNotification
	 * @Description 常驻状态栏的通知,通过view设置点击，可以设置点击时间
	 * @param notificationFlag
	 *            设置常驻状态栏 Notification.FLAG_ONGOING_EVENT
	 * @return int 返回类型
	 */
	@SuppressWarnings("deprecation")
	public static int creatNotification(Context context,
			NotificationManager nm, Intent intent, String iconName,
			int layoutId, String notice, String title, String content,
			int notificationFlag) {

		int m = 10000;
		int n = 100000;
		int notificationId = (int) (Math.random() * (m - n)) + n;

		int id = context.getResources().getIdentifier(iconName, "drawable",
				context.getApplicationContext().getPackageName());

		Notification notification = new Notification(id, notice,
				System.currentTimeMillis());
		RemoteViews contentView = new RemoteViews(context.getPackageName(),
				layoutId);
		notification.contentView = contentView;
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.flags = notificationFlag; // 设置常驻 Flag
												// Notification.FLAG_ONGOING_EVENT

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		// notification.setLatestEventInfo(context, title, content,
		// contentIntent);

		nm.notify(notificationId, notification);

		return notificationId;

	}

	public static void startForegroundNotification(Context context,
			int notificationId, RemoteViews view, Intent intent,
			PendingIntent pendingIntent, int iconR, String resStringTitleR,
			String str) {
		int id = context.getResources().getIdentifier(resStringTitleR,
				"string", context.getApplicationContext().getPackageName());
		Notification notification = NotifyUtils
				.creatResidentNotification(context, id, iconR, intent, view,
						"", Notification.FLAG_ONGOING_EVENT);

		((Service) context).startForeground(notificationId, notification);

	}

}
