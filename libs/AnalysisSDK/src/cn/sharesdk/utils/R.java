package cn.sharesdk.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

public class R {
	private static float density;
	
	public static int dipToPx(Context context, int dip) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) (dip * density + 0.5f);
	}
	
	public static int pxToDip(Context context, int px) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) (px / density + 0.5f);
	}
	
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	private static int getResId(Class<?> resHolder, String resName) {
		int resId = 0;
		if (resName != null) {
			String nameLow = resName.toLowerCase();
			try {
				Field nameFld = resHolder.getField(nameLow);
				nameFld.setAccessible(true);
				resId = (Integer) nameFld.get(null);
			} catch(Throwable t) {
				Ln.e("getResId == >>", Log.getStackTraceString(t));
				resId = 0;
			}
		}
		
		if (resId <= 0) {
			Ln.e("getResId ==>>", "resource " + resHolder.getName() + "." + resName + " not found!");
		}
		return resId;
	}

	public static int getBitmapRes(Context context, String resName) {
		int resId = 0;
		String pck = context.getPackageName();
		try {
			Class<?> cls = Class.forName(pck + ".R$drawable");
			resId = getResId(cls, resName);
		} catch(Throwable t) {
			Ln.e("getBitmapRes ==>>", Log.getStackTraceString(t));
			resId = 0;
		}
		
		if (resId <= 0) {
			resId = context.getResources().getIdentifier(resName.toLowerCase(), "drawable", pck);
		}
		return resId;
	}
	
	public static int getStringRes(Context context, String resName) {
		int resId = 0;
		String pck = context.getPackageName();
		try {
			Class<?> cls = Class.forName(pck + ".R$string");
			resId = getResId(cls, resName);
		} catch(Throwable t) {
			Ln.e("getStringRes ==>>", Log.getStackTraceString(t));
			resId = 0;
		}
		
		if (resId <= 0) {
			resId = context.getResources().getIdentifier(resName.toLowerCase(), "string", pck);
		}
		return resId;
	}

	public static int getStringArrayRes(Context context, String resName) {
		int resId = 0;
		String pck = context.getPackageName();
		try {
			Class<?> cls = Class.forName(pck + ".R$array");
			resId = getResId(cls, resName);
		} catch(Throwable t) {
			Ln.e("getStringArrayRes ==>>", Log.getStackTraceString(t));
			resId = 0;
		}
		
		if (resId <= 0) {
			resId = context.getResources().getIdentifier(resName.toLowerCase(), "array", pck);
		}
		return resId;
	}
	

	public static int getIdRes(Context context, String resName) {
		int resId = 0;
		String pck = context.getPackageName();
		try {
			Class<?> cls = Class.forName(pck + ".R$id");
			resId = getResId(cls, resName);
		} catch(Throwable t) {
			Ln.e("getIdRes ==>>", Log.getStackTraceString(t));
			resId = 0;
		}
		
		if (resId <= 0) {
			resId = context.getResources().getIdentifier(resName, "id", pck);
		}
		return resId;
	}
	
}
