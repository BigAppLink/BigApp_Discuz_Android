package com.kit.widget.actionsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface.OnCancelListener;

/**
 * 
 * @ClassName ActionSheet
 * @Description 非常牛逼的ActionSheet，可以控制ActionSheet Item的个数
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-5-31 上午10:05:59
 * 
 */
public class ActionSheet {

	public static int actionsheetStyle = 1;
	public static final int ACTIONSHEET_IOS = 1;
	public static final int ACTIONSHEET_WECHAT = 2;

	public ActionSheet() {
	}

	/**
	 * 
	 * @Title showSheet
	 * @Description TODO(这里用一句话描述这个方法的作用)
	 * @param mContext
	 *            上下文环境
	 * @param actionSheetConfig
	 *            ActionSheet配置项
	 * @param actionSheetItemSelected
	 *            ActionSheet Item选中监听
	 * @param cancelListener
	 *            取消按钮的监听,无需监听可传null
	 * @return Dialog 返回类型
	 */
	@SuppressLint("NewApi")
	public void show(final Activity activity,
			ActionSheetConfig actionSheetConfig,
			final OnActionSheetItemSelected actionSheetItemSelected,
			OnCancelListener cancelListener) {
		switch (actionSheetConfig.actionsheetStyle) {

		case ACTIONSHEET_IOS:
			new ActionSheet4IOS().show(activity, actionSheetConfig,
					actionSheetItemSelected, cancelListener);

			break;
		case ACTIONSHEET_WECHAT:
			new ActionSheet4WeChat().show(activity, actionSheetConfig,
					actionSheetItemSelected, cancelListener);
			break;
		default:
			new ActionSheet4WeChat().show(activity, actionSheetConfig,
					actionSheetItemSelected, cancelListener);
		}
	}

	/**
	 * 
	 * @ClassName OnActionSheetSelected
	 * @Description ActionSheet Item选中监听接口
	 * @author Zhao laozhao1005@gmail.com
	 * @date 2014-5-30 下午2:10:05
	 * 
	 */
	public interface OnActionSheetItemSelected {
		void onActionSheetItemSelected(Activity activity, int whichButton);
	}

}
