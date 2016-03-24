package com.kit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Intent;
import android.view.View;

/**
 * 
 * @ClassName ScrollUtils
 * @Description 禁用滑动组件的越界效果
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-5-27 下午2:47:53
 * 
 */
public class ScrollUtils {
	/**
	 * 禁用滑动组件的越界效果，利用反射，可用于2.2、2.3平台
	 * 
	 * @param view
	 *            传入ScrollView、HorizontalScroll、WebView和ListView
	 */
	public static void disableOVER_SCROLL_NEVER(View view) {
		try {

			Method method = view.getClass().getMethod("setOverScrollMode",
					int.class);

			Field field = view.getClass().getField("OVER_SCROLL_NEVER");

			if (method != null && field != null) {

				method.invoke(view, field.getInt(View.class));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

}
