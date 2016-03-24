package com.kit.app;

import android.app.Activity;

import com.kit.utils.ZogUtils;

import java.util.Iterator;
import java.util.LinkedList;

public class ActivityManager {
	private LinkedList<Activity> activities = new LinkedList<Activity>();
	private static ActivityManager instance;

	private ActivityManager() {
	}

	// 单例模式中获取唯一的ZhaoActivityManager实例
	public static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * activity压入activities
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		synchronized (activities) {
			if (activity != null) {
				activities.add(activity);
			}
			ZogUtils.printLog(ActivityManager.class, "activities.size():"
					+ activities.size());
		}
	}

	/**
	 * 根据类名来销毁activity
	 * 
	 * @param cls
	 */
	public void popActivity(Class cls) {
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (activity.getClass().equals(cls)) {
				if (activity != null) {
					activity.finish();
				}
				iter.remove();
			}
		}

		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
	}

	/**
	 * 弹出activity
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {

		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity act = iter.next();
			if (act != null && activity != null) {
				if (act.getClass().equals(activity.getClass())) {
					activity.finish();
					activity = null;
					iter.remove();
				}
			}
		}

		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
	}

	/**
	 * 遍历所有Activity并finish（一般用于退出应用，销毁APP）
	 */
	public void popAllActivity() {
		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (activity != null) {
				activity.finish();
				activity = null;
			}
			iter.remove();
		}

		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
	}

	/**
	 * 销毁除了类名为cls的activity的其余所有的activity
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class cls) {
		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (activity != null && !activity.getClass().equals(cls)) {
				activity.finish();
				activity = null;
				iter.remove();
			}

		}

		ZogUtils.printLog(ActivityManager.class, "activities.size():"
				+ activities.size());
	}

	/**
	 * 获取最新压入list的activity
	 * 
	 * @return
	 */
	public Activity getCurrActivity() {

		Activity activity = null;
		try {
			activity = activities.get(activities.size() - 1);
		} catch (Exception e) {
			ZogUtils.showException(e);
		}
		return activity;
	}

	/**
	 * 获取压入list的特定activity
	 * 
	 * @return
	 */
	public Activity getActivity(Class cls) {
		Activity activity = null;
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity act = iter.next();
			if (act.getClass().equals(cls)) {
				activity = act;
				break;
			}
		}
		return activity;
	}



	/**
	 * 判断list是否存在类名为cls的activity
	 * 
	 * @param cls
	 * @return
	 */
	public boolean isExistActivity(Class cls) {

		int i = 0;
		Iterator<Activity> iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (activity != null && activity.getClass().equals(cls)) {
				i++;
			}
		}

		if (i > 0)
			return true;
		else
			return false;
	}

}