/**
 * ************************************************************
 * ShareSDKStatistics
 * An open source analytics android sdk for mobile applications
 * ************************************************************
 *
 * @package ShareSDK Statistics
 * @author ShareSDK Limited Liability Company
 * @copyright Copyright 2014-2016, ShareSDK Limited Liability Company
 * @filesource https://github.com/OSShareSDK/OpenStatistics/tree/master/Android
 * <p/>
 * *****************************************************
 * This project is available under the following license
 * *****************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @since Version 1.0
 */
package cn.sharesdk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.List;

import cn.sharesdk.utils.DeviceHelper;
import cn.sharesdk.utils.Ln;

public class DBProvider {

    private Context context;

    private DBHelp dbHelp = null;
    private static DBProvider provider = null;

    private DBProvider(Context c) {
        if (c == null) {
            Ln.e("create db fail", "context is null!");
            return;
        }
        this.context = c.getApplicationContext();
        String dBPath = DBHelp.DBFILE;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dbHelp = new DBHelp(context, dBPath);
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && DeviceHelper.getInstance(context).checkPermissions("android.permission.WRITE_EXTERNAL_STORAGE")) {
            try {
                File dbPath = new File(DBHelp.DBParentFile, c.getPackageName()+ "/db/");
                File dbFile = new File(dbPath, DBHelp.DBFILE);
                if (!dbPath.exists()) {
                    dbPath.mkdirs();
                }
                if (!dbFile.exists()) {
                    dbFile.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dBPath = DBHelp.DBParentFile + "/" + c.getPackageName() + "/db/" + DBHelp.DBFILE;
            dbHelp = new DBHelp(context, dBPath);
        } else {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }


    public static synchronized DBProvider getDBProvider(Context c) {
        if (provider == null) {
            provider = new DBProvider(c);
        }
        return provider;
    }

    /**
     * 查询
     *
     * @param table
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return null;
        }

        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Ln.d("Query table: " + table, "Query table");
        Cursor c = null;
        try {
            c = db.query(table, columns, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            Ln.e("when query database occur error table:" + table, e.getMessage());
        }
        return c;
    }

    /**
     * 插入
     *
     * @param table
     * @param values
     * @return
     */
    public long insert(String table, ContentValues values) {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return -1;
        }
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        long rowId = -1;
        try {
            Ln.i("insert database ", "insert");
            rowId = db.replace(table, null, values);
        } catch (Exception e) {
            Ln.e("when insert database occur error table:" + table, e.getMessage());
        }

        return rowId;
    }

    /**
     * 批量插入，数据越多越好用；最好多于100条
     *
     * @param table
     * @param list
     * @return
     */
    public long transactInsert(String table, List<ContentValues> list) {
        if (dbHelp == null) {
            Ln.i("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return -1;
        }

        SQLiteDatabase db = dbHelp.getWritableDatabase();
        db.beginTransaction();
        long rowId = -1;
        try {
            for (int i = 0; i < list.size(); i++) {
                rowId = db.insert(table, null, list.get(i));
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Ln.e("when insert database occur error table:" + table, e.getMessage());
        } finally {
            db.endTransaction();
        }

        return rowId;
    }

    /**
     * 删除
     *
     * @param table
     * @param selection
     * @param selectionArgs
     * @return
     */
    public int delete(String table, String selection, String[] selectionArgs) {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return 0;
        }

        SQLiteDatabase db = dbHelp.getWritableDatabase();
        int count = 0;
        try {
            count = db.delete(table, selection, selectionArgs);
            Ln.d("Deleted " + table + " rows from table: " + count, "delete");
        } catch (Exception e) {
            Ln.e("when delete database occur error table:" + table, e.getMessage());
        }

        return count;
    }

    /**
     * 更新
     *
     * @param table
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    public int update(String table, ContentValues values, String selection, String[] selectionArgs) {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return 0;
        }

        SQLiteDatabase db = dbHelp.getWritableDatabase();
        int count = 0;
        try {
            count = db.update(table, values, selection, selectionArgs);
            Ln.d("Updated " + table + " row from table: %s" + count, "update");
        } catch (Exception e) {
            Ln.e("when update database occur error table:" + table, e.getMessage());
        }

        return count;
    }

    public void close() {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return;
        }
        dbHelp.close();
    }

    /**
     * 获取当前数据库的count
     *
     * @param table 表名
     * @return 表中的数据条数
     */
    public int getCount(String table) {
        if (dbHelp == null) {
            Ln.e("create db fail", "lost permission--->android.permission.WRITE_EXTERNAL_STORAGE");
            return 0;
        }

        int count = 0;
        Cursor c = null;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        try {
            c = db.rawQuery("select count(*) from " + table, null);
            if (c.moveToNext())
                count = c.getInt(0);
        } catch (Exception e) {
            Ln.e("getCount", e.getMessage());
            count = 0;
        } finally {
            c.close();
        }
        return count;
    }

    /**获取数据库的最后一条消息的时间
     *
     * @param table表名
     * @return 表中的数据条数
     */
//	public long getLastTime(String table) {
//		long time = 0;
//		Cursor c = null;
//		SQLiteDatabase db = dbHelp.getWritableDatabase();
//		try {
//			c = db.rawQuery("select " + DBHelp.COLUMN_POSTIME + " from "+ table + " desc LIMIT 1 ", null);
//			if(c.moveToNext())
//				time = c.getLong(0);
//			c.close();
//		} catch (Exception e) {
//			Ln.e("getLastTime", e.getMessage());
//			time = 0;
//			c.close();
//		}
//		return time;
//	}

}
