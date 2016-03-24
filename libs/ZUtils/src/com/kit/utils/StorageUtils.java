/**  
 * @Title StorageUtils.java
 * @Package com.kit.utils
 * @Description  TODO(用一句话描述该文件做什么)
 * @author Zhao  laozhao1005@gmail.com
 * @date 2014-5-14 下午10:10:33
 */
package com.kit.utils;

import android.os.Environment;
import android.os.StatFs;

/**
 * @ClassName StorageUtils
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-5-14 下午10:10:33
 * 
 */
public class StorageUtils {

	public static boolean isExternalStorageAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {// 已经插入了sd卡，并且可以读写
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {// 已经插入了sd卡，但是是只读的情况
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// 其他错误的状态。外部存储设备可能在其他的装备，但是我们要知道，在这一种情况下，我们不能对其进行读写。
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable;
	}

	public static boolean isExternalStorageWriteable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {// 已经插入了sd卡，并且可以读写
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {// 已经插入了sd卡，但是是只读的情况
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// 其他错误的状态。外部存储设备可能在其他的装备，但是我们要知道，在这一种情况下，我们不能对其进行读写。
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}

	/** * 计算sdcard上的剩余空间 * @return */
	public static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / (1024 * 1024);

		return (int) sdFreeMB;
	}
}
