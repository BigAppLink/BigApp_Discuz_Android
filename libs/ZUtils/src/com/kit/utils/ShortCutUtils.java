package com.kit.utils;


import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

public class ShortCutUtils {
	// <uses-permission
	// android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
	// <uses-permission
	// android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />

	public static boolean hasShortcut(Context context, String name) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();
		final String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { name.trim() }, null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	public static void addShortcut(Context context, String name, int resIconId,
			Intent intent) {

		final Intent addIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		final Parcelable icon = Intent.ShortcutIconResource.fromContext(
				context, resIconId); // 获取快捷键的图标

		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);// 快捷方式的标题
		addIntent.putExtra("duplicate", false); // 不允许重复创建
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);// 快捷方式的图标
		
		//创建一键锁屏图标
//		Intent i = new Intent();
//		i.setClass(context.getApplicationContext(), OneKeyLockActivity.class);
//		ShortCutUtils.addShortcut(context, "锁屏",
//				R.drawable.composer_sleep, i);

		// PackageManager pm = context.getPackageManager();
		// Intent i = pm.getLaunchIntentForPackage(context.getPackageName());
		
		
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);// 快捷方式的动作
		context.sendBroadcast(addIntent);

		// Intent shortcut = new Intent(
		// "com.android.launcher.action.INSTALL_SHORTCUT");
		//
		// // 快捷方式的名称
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		// shortcut.putExtra("duplicate", false); // 不允许重复创建
		//
		// /**************************** 此方法已失效 *************************/
		// // ComponentName comp = new ComponentName(this.getPackageName(),
		// // "."+this.getLocalClassName());
		// // shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
		// // Intent(Intent.ACTION_MAIN).setComponent(comp)); 　　
		// /****************************** end *******************************/
		// // Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		// // shortcutIntent.setClassName(context,
		// context.getClass().getName());
		// // shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);// 快捷方式的动作
		// // 快捷方式的图标
		// ShortcutIconResource iconRes =
		// Intent.ShortcutIconResource.fromContext(
		// context, resIconId);
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		//
		// context.sendBroadcast(shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 */
	public static void delShortcut(Context context, String name) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		String appClass = context.getPackageName() + "."
				+ ((Activity) context).getLocalClassName();
		ComponentName comp = new ComponentName(context.getPackageName(),
				appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		context.sendBroadcast(shortcut);

	}

}
