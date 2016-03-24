package com.kit.widget.actionsheet;

import android.view.View;

/**
 * 
 * @ClassName ActionSheetConfig
 * @Description ActionSheet配置项
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-6-3 上午11:01:48
 * 
 */

public class ActionSheetConfig {
	
	/**
	 * actionsheetStyle actionsheet风格
	 */
	public int actionsheetStyle;
	/**
	 * actionSheet 按钮item的文字
	 */
	public String[] actionSheetStrings;

	/**
	 * actionSheet 按钮item的文字颜色
	 */
	public int[] colors;

	/**
	 * actionSheet 按钮item的文字大小
	 */
	public float[] actionSheetTextSize;

	/**
	 * 提示语
	 */
	public String title;

	/**
	 * 自定义视图的提示
	 */
	public View titleLayout;
}
