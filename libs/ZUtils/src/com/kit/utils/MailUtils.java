package com.kit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MailUtils {
	public static void mkCall(Context context, String strEmailReciver,
			String stremailCc, String strEmailSubject, String strEmailBody) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);// 通过Intent来发送邮件

		emailIntent.setType("plain/text");// 设置邮件格式为plain/text

		/* 将收件人地址、附件、主旨、正文放入emailIntent中 */

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				strEmailReciver);

		emailIntent.putExtra(android.content.Intent.EXTRA_CC, stremailCc);

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				strEmailSubject);

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody);

		/* 打开Gmail并将相关参数传入 */

		/* Gmail程序是收发Email的程序，是Android手机内置的 */

		context.startActivity(Intent.createChooser(emailIntent, "pantosoft"));

	}

	public static void mkCall(Context context, String strEmailReciver,
			String stremailCc, String strEmailSubject, String strEmailBody,String strStream) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);// 通过Intent来发送邮件

		emailIntent.setType("plain/text");// 设置邮件格式为plain/text

		/* 将收件人地址、附件、主旨、正文放入emailIntent中 */

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				strEmailReciver);

		emailIntent.putExtra(android.content.Intent.EXTRA_CC, stremailCc);

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				strEmailSubject);

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody);
		emailIntent.putExtra(android.content.Intent.EXTRA_STREAM,Uri.parse(strStream));
	     
		/* 打开Gmail并将相关参数传入 */

		/* Gmail程序是收发Email的程序，是Android手机内置的 */

		context.startActivity(Intent.createChooser(emailIntent, "pantosoft"));

	}
}
