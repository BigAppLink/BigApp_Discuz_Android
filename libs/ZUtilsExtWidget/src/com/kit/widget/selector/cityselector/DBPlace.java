package com.kit.widget.selector.cityselector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kit.db.DB;
import com.kit.utils.ZogUtils;

import java.util.ArrayList;
import java.util.List;

public class DBPlace extends DB {

    public final static String TABLE_NAME = "place";

    public static final String FIELD_ID = "_id";

    public final static String FIELD_PLACE_ID = "id";
    public final static String FIELD_CODE = "code";
    public final static String FIELD_NAME = "name";
    public final static String FIELD_PCODE = "pcode";
    public final static String FIELD_LEVEL = "level";

    public DBPlace(Context context) {

        super(context);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        createPlace(db);
    }

    public void createPlace(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE if not exists ");
        sql.append(DBPlace.TABLE_NAME);
        sql.append(" (");
        sql.append(DBPlace.FIELD_PLACE_ID
                + " integer primary key autoincrement, ");

        sql.append(DBPlace.FIELD_CODE + " TEXT , ");
        sql.append(DBPlace.FIELD_NAME + " TEXT , ");
        sql.append(DBPlace.FIELD_PCODE + " TEXT , ");
        sql.append(DBPlace.FIELD_LEVEL + " INTEGER ");

        sql.append(");");

        // LogUtils.printLog(getClass(), sql.toString());

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor selectAll() {
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null,
                FIELD_PLACE_ID + " desc");
        return cursor;
    }

    public List<Place> selectAll4List() {

        Cursor cursor = selectAll();
        List<Place> list = toList(cursor);
        cursor.close();
        return list;
    }

    public Cursor selectAll(String orderColumn, String orderType) {
        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null,
                orderColumn + " " + orderType);
        return cursor;
    }

    public List<Place> selectAll4List(String column, String str) {

        Cursor cursor = selectAll(column, str);
        List<Place> list = toList(cursor);
        cursor.close();
        return list;
    }

    public Cursor selectById(int id) {
        String where = FIELD_PLACE_ID + " = " + id;
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                null);
        return cursor;
    }

    public Cursor selectById(int id, String orderColumn, String orderType) {
        String where = FIELD_PLACE_ID + " = " + id;
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                orderColumn + " " + orderType);
        return cursor;
    }

    public Cursor selectByName(String column, String str) {
        String where = " " + column + " = '" + str + "' ";
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                FIELD_PLACE_ID + " asc");
        return cursor;
    }

    public Cursor selectByNameLike(String column, String str) {
        String where = " " + column + " like '%" + str + "%' ";
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                FIELD_PLACE_ID + " asc");
        return cursor;
    }

    public List<Place> selectByNameLike2List(String column, String str) {
        String where = " " + column + " like '%" + str + "%' ";
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                FIELD_PLACE_ID + " asc");
        List<Place> list = toList(cursor);
        cursor.close();
        return list;
    }

    public Place selectByNameLike2Obj(String column, String str) {
        String where = " " + column + " like '%" + str + "%' ";
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                FIELD_PLACE_ID + " asc");
        List<Place> list = toList(cursor);
        cursor.close();
        return list.get(0);
    }

    public List<Place> selectByName4List(String column, String str) {

        Cursor cursor = selectByName(column, str);
        List<Place> list = toList(cursor);
        cursor.close();
        return list;
    }

    public Cursor selectByName(String column, String str, String orderColumn,
                               String orderType) {
        String where = " " + column + " = '" + str + "' ";
        Cursor cursor = mDb.query(TABLE_NAME, null, where, null, null, null,
                orderColumn + " " + orderType);

        return cursor;
    }

    public List<Place> selectByName4List(String column, String str,
                                         String orderColumn, String orderType) {
        Cursor cursor = selectByName(column, str, orderColumn, orderType);
        List<Place> list = toList(cursor);
        cursor.close();
        return list;
    }

    public List<Place> toList(Cursor cursor) {

        List<Place> list = new ArrayList<Place>();

        try {
            int count = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                String code = cursor.getString(cursor
                        .getColumnIndex(DBPlace.FIELD_CODE));
                String pcode = cursor.getString(cursor
                        .getColumnIndex(DBPlace.FIELD_PCODE));
                byte[] bytes = cursor.getBlob(cursor
                        .getColumnIndex(DBPlace.FIELD_NAME));
                int level = cursor.getInt(cursor
                        .getColumnIndex(DBPlace.FIELD_LEVEL));
                String name = new String(bytes, "utf-8");

                Place myListItem = new Place();

                myListItem.setName(name);
                myListItem.setCode(code);
                myListItem.setPcode(pcode);
                myListItem.setLevel(level);

                list.add(myListItem);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            ZogUtils.showException(e);
        }
//        LogUtils.printLog(getClass(), "list.size():" + list.size());
        return list;
    }

    public long insert(ArrayList<Place> list) {
        // 使用事务插入速度最快，使用基本sql语句次之 使用ContentValues最慢
        // long before = System.nanoTime();
        long i = 0;
        try {
            mDb.beginTransaction();
            for (Place c : list) {

                ContentValues values = new ContentValues();

                // values.put(DBPlace.FIELD_PLACE_ID, c.getId());
                values.put(DBPlace.FIELD_CODE, c.getCode());
                values.put(DBPlace.FIELD_NAME, c.getName());
                values.put(DBPlace.FIELD_PCODE, c.getPcode());
                values.put(DBPlace.FIELD_LEVEL, c.getLevel());

                i += mDb.insert(TABLE_NAME, null, values);
            }
            mDb.setTransactionSuccessful();
            mDb.endTransaction();

            // long after = System.nanoTime();
            // long chazhi = after - before;
            // System.out.println("after - before: " + chazhi);

            return i;
        } catch (Exception e) {
//            e.printStackTrace();

            ZogUtils.printLog(DBPlace.class, "insert error");

            mDb.endTransaction();

            return i;
        }

    }

    public long insert(Place obj) {

        long i = 0;
        try {
            mDb.beginTransaction();
            ContentValues values = new ContentValues();
            // values.put(DBPlace.FIELD_PLACE_ID, c.getId());
            values.put(DBPlace.FIELD_CODE, obj.getCode());
            values.put(DBPlace.FIELD_NAME, obj.getName());
            values.put(DBPlace.FIELD_PCODE, obj.getPcode());
            values.put(DBPlace.FIELD_LEVEL, obj.getLevel());

            i += mDb.insert(TABLE_NAME, null, values);

            mDb.setTransactionSuccessful();
            mDb.endTransaction();

            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }

    }

    public long insert(ContentValues initialValues) {

        return mDb.insert(TABLE_NAME, null, initialValues);
    }

    public int update(int id) {
        // SQLiteDatabase mDb = this.getWritableDatabase();
        String where = FIELD_PLACE_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();

        cv.put(FIELD_PLACE_ID, id);

        int row = mDb.update(TABLE_NAME, cv, where, whereValue);

        mDb.close();
        return row;
    }

    public int getCount() {
        Cursor cursor = mDb
                .rawQuery("select count(*) from " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        return count;
    }

    public void delete(int id) {
        // SQLiteDatabase mDb = this.getWritableDatabase();
        String where = FIELD_PLACE_ID + "=?";
        String[] whereValue = {Integer.toString(id)};
        mDb.delete(TABLE_NAME, where, whereValue);
    }

    public void addColumn(String column, String columnType) throws Exception {
        // SQLiteDatabase mDb = this.getWritableDatabase();

        String updateSql = "alter table user add " + column + " " + columnType;
        mDb.execSQL(updateSql);
    }

    /**
     * 清空表中的数据
     */
    public void clean() {
        mDb.execSQL("DELETE FROM " + TABLE_NAME);
        mDb.execSQL("update sqlite_sequence set seq=0 where name='"
                + TABLE_NAME + "'");
    }

}