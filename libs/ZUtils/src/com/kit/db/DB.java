package com.kit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.kit.utils.ZogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DB extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "db.sqlite";
	public static final int DATABASE_VERSION = 1;

	public static final String ORDER_TYPE_ASC = "asc";
	public static final String ORDER_TYPE_DESC = "desc";

	private static final int BUFFER_SIZE = 1024;

	public static String package_name;
	public static String db_path;

	public DB mDbHelper;
	public SQLiteDatabase mDb;
	public Context context;

	public String dbName;
	private File file = null;

	public DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context.getApplicationContext();
		initDB();
	}

	/**
	 * 创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		// createSmaple2(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void initDB() {
		package_name = context.getPackageName();
		db_path = "/data" + Environment.getDataDirectory().getAbsolutePath()
				+ "/" + package_name + "/databases";

	}

	public void open(String assetsName) {

		dbName = assetsName;

//		LogUtils.printLog(getClass(), db_path + "/" + DATABASE_NAME);

		this.mDb = this.openDatabase(db_path + "/" + DATABASE_NAME);

	}
	
	
	public void open() {

		dbName = DATABASE_NAME;

		ZogUtils.printLog(getClass(), db_path + "/" + DATABASE_NAME);

		this.mDb = this.openDatabase(db_path + "/" + DATABASE_NAME);

	}


	public void close() {
		if (mDb != null && mDb.isOpen()) {
			mDb.close();
		}
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {

			File directory = new File(db_path);
			directory.mkdirs();

			file = new File(dbfile);

			if (!directory.exists()) {
				ZogUtils.printLog(getClass(), "can not mkdir: " + db_path);
				return null;
			} else {
				if (!file.exists()) {
					// 从raw中读取文件place 并写入到databases文件夹下
					InputStream is = context.getResources().getAssets()
							.open(dbName);

					FileOutputStream fos = new FileOutputStream(dbfile);

					byte[] buffer = new byte[BUFFER_SIZE];
					int count = 0;
					while ((count = is.read(buffer)) > 0) {
						fos.write(buffer, 0, count);
						// Log.e("cc", "while");
						fos.flush();
					}
					fos.close();
					is.close();
				}
				mDb = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
				return mDb;
			}
		} catch (FileNotFoundException e) {
			ZogUtils.printLog(getClass(), "e.getClass():" + e.getClass());
			ZogUtils.showException(e);
		} catch (IOException e) {
			ZogUtils.printLog(getClass(), "e.getClass():" + e.getClass());
			ZogUtils.showException(e);
		} catch (Exception e) {
			ZogUtils.printLog(getClass(), "e.getClass():" + e.getClass());
			ZogUtils.showException(e);
		}
		return null;
	}

	public SQLiteDatabase getDatabase() {
		return this.mDb;
	}

}