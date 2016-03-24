package com.kit.utils;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

public class RingtoneSetting {
	private static String mUrl;
	private static Context mContext;

	public static void setmUrl(String url) {
		mUrl = url;
	}

	public static void setting(Context context) {
		// 外部调用者传来的context
		mContext = context;
		// 设置歌曲路径
		File filePath = new File(mUrl);
		ContentValues values = new ContentValues();
		// The data stream for the file
		values.put(MediaStore.MediaColumns.DATA, filePath.getAbsolutePath());
		// The title of the content
		values.put(MediaStore.MediaColumns.TITLE, filePath.getName());
		// The MIME type of the file
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		// values.put(MediaStore.Audio.Media.ARTIST, "Madonna");
		// values.put(MediaStore.Audio.Media.DURATION, 230);
		// 来电铃声
		// 第二个参数若是true则会在铃音库中显示
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		// 通知/短信铃声
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		// 闹钟铃声
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		// 系统铃声
		values.put(MediaStore.Audio.Media.IS_MUSIC, true);
		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(filePath
				.getAbsolutePath());
		// 下面这一句很重要
		mContext.getContentResolver().delete(
				uri,
				MediaStore.MediaColumns.DATA + "=\""
						+ filePath.getAbsolutePath() + "\"", null);
		Uri newUri = mContext.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(mContext,
				RingtoneManager.TYPE_RINGTONE, newUri);

	}
}