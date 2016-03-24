package com.youzu.clan.base.db;

import com.youzu.android.framework.DbUtils;
import com.youzu.android.framework.exception.DbException;

import android.content.Context;

public final class DBUtils {
	
	private static DbUtils getDbUtils(Context context) {
		return DbUtils.create(context, "clan");
	}
	
	public static void save(Context context, Object entity) {
		try {
			getDbUtils(context).save(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
