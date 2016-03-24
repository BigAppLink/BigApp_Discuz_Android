package com.kit.utils;

import android.content.Context;

/**
 * 
 * @ClassName ResUtils
 * @Description 获取资源文件的工具类
 * @author Zhao laozhao1005@gmail.com
 * @date 2014-7-24 下午12:55:00
 * 
 */
public class ResUtils {

	public static String getText4ResStringArray(Context context, int intR) {
		String[] hibernate_copy = context.getResources().getStringArray(intR);
		String textStr = (String) ArrayUtils.getOne(hibernate_copy);
		return textStr;

	}
}
