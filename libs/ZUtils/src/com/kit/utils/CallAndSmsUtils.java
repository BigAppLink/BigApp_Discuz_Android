package com.kit.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

public class CallAndSmsUtils {
	public static void mkCall(Context context, String strPhone) {
		Uri uri = Uri.parse("tel:" + strPhone);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);// 注意：call是直接就打出去了，dial是经过系统的确定才能打
		context.startActivity(intent);

	}

	public static void mkDail(Context context, String strPhone) {
		Uri uri = Uri.parse("tel:" + strPhone);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);// 注意：call是直接就打出去了，dial是经过系统的确定才能打
		context.startActivity(intent);

	}

	public static void mkMsm(Context context, String strPhone) {

		Uri smsToUri = Uri.parse("smsto:" + strPhone);
		Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		context.startActivity(mIntent);

	}

	public static void mkMsm(Context context, String strPhone, String strSms) {

		// 第一种方法,有一个界面让你选择是否发送
		Uri uri = Uri.parse("smsto:" + strPhone);
		Intent intent = new Intent();
		intent.putExtra("sms_body", strSms);// 设置短信内容

		intent.setAction(Intent.ACTION_SENDTO);
		intent.setData(uri);
		context.startActivity(intent);
		// // 第二种方法,直接就发送过去了
		// SmsManager smsManager = SmsManager.getDefault();
		// PendingIntent pendingIntent = PendingIntent.getBroadcast(
		// MainActivity.this, 0, new Intent(), 0);
		// smsManager.sendTextMessage(strPhone, null, strSms,
		// pendingIntent, null);
	}

	public static int findNewSmsCount(Context ctx) {
		Cursor csr = null;
		try {
			csr = ctx
					.getApplicationContext()
					.getContentResolver()
					.query(Uri.parse("content://sms"), null,
							"type = 1 and read = 0", null, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		return csr.getCount(); // 未读短信数目

	}

	public static int findNewMmsCount(Context ctx) {
		Cursor csr = null;
		try {
			csr = ctx
					.getApplicationContext()
					.getContentResolver()
					.query(Uri.parse("content://mms/inbox"), null, "read = 0",
							null, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		return csr.getCount();// 未读彩信数目
	}

	public static void sendSMS(Context context, Intent intent,
			String phonenumber, String msg) {// 发送短信的类
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phonenumber, null, msg, pi, null);// 发送信息到指定号码
	}
}
