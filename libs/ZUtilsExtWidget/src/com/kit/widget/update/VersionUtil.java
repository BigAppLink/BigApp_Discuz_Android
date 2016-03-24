package com.kit.widget.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 版本工具类 VersionUtil.java
 */
public class VersionUtil {
	/**
	 * 获取版本号
	 * 
	 * @param context
	 *            上下文
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getVersionName(Context context)
			throws NameNotFoundException {
		// 获取PackageManager 实例
		PackageManager packageManager = context.getPackageManager();
		// 获得context所属类的包名，0表示获取版本信息
		PackageInfo packageInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packageInfo.versionName;
	}
}
