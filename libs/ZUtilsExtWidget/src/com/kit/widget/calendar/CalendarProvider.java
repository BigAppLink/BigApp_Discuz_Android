package com.kit.widget.calendar;

import java.util.HashMap;

import com.kit.widget.calendar.CalendarColumns.CalendarColumn;



import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class CalendarProvider extends ContentProvider
{
	//private static final String TAG	= "CalendarProvider";
	// 数据库名
	private static final String DATABASE_NAME = "calendar.db";
	private static final int DATABASE_VERSION = 1;
	// 表名
	private static final String CalendarColumn_TABLE_NAME	= "CalendarColumn";
	private static HashMap<String, String>	sCalendarColumnProjectionMap;
	private static final int CALENDAR = 1;
	private static final int CALENDAR_ID = 2;
	private static final UriMatcher sUriMatcher;
	private DatabaseHelper	mOpenHelper = new DatabaseHelper(getContext());;
	//创建表SQL语句
	private static final String	CREATE_TABLE="CREATE TABLE " 
														+ CalendarColumn_TABLE_NAME 
														+ " (" + CalendarColumn._ID 
														+ " INTEGER PRIMARY KEY AUTOINCREMENT," 
														+ CalendarColumn.CONTENT 
														+ " TEXT," 
														+ CalendarColumn.CREATED 
														+ " TEXT,"
														+ CalendarColumn.DATE 
														+ " TEXT," 
														+ CalendarColumn.TIME 
														+ " TEXT,"
														+ CalendarColumn.ISORUSE 
														+ " TEXT,"
														+ CalendarColumn.ISORREVEAL 
														+ " TEXT" + ");";
	
	static
	{
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CalendarColumns.AUTHORITY, "CalendarColumn", CALENDAR);
		sUriMatcher.addURI(CalendarColumns.AUTHORITY, "CalendarColumn/#", CALENDAR_ID);

		sCalendarColumnProjectionMap = new HashMap<String, String>();
		sCalendarColumnProjectionMap.put(CalendarColumn._ID, CalendarColumn._ID);
		sCalendarColumnProjectionMap.put(CalendarColumn.CONTENT, CalendarColumn.CONTENT);
		sCalendarColumnProjectionMap.put(CalendarColumn.CREATED, CalendarColumn.CREATED);
		sCalendarColumnProjectionMap.put(CalendarColumn.DATE, CalendarColumn.DATE);
		sCalendarColumnProjectionMap.put(CalendarColumn.TIME, CalendarColumn.TIME);
		sCalendarColumnProjectionMap.put(CalendarColumn.ISORUSE, CalendarColumn.ISORUSE);
		sCalendarColumnProjectionMap.put(CalendarColumn.ISORREVEAL, CalendarColumn.ISORREVEAL);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		//构造函数-创建数据库
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		//创建表
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL("DROP TABLE IF EXISTS CalendarColumn");
			db.execSQL(CREATE_TABLE);
		}
		//更新数据库
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			onCreate(db);
		}
	}
	@Override
	public boolean onCreate()
	{
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}
	@Override
	//查询操作
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri))
		{
			case CALENDAR:
				qb.setTables(CalendarColumn_TABLE_NAME);
				qb.setProjectionMap(sCalendarColumnProjectionMap);
				break;

			case CALENDAR_ID:
				qb.setTables(CalendarColumn_TABLE_NAME);
				qb.setProjectionMap(sCalendarColumnProjectionMap);
				qb.appendWhere(CalendarColumn._ID + "=" + uri.getPathSegments().get(1));
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder))
		{
			orderBy = CalendarColumns.CalendarColumn.DEFAULT_SORT_ORDER;
		}
		else
		{
			orderBy = sortOrder;
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	@Override
	// 如果有自定义类型，必须实现该方法
	public String getType(Uri uri)
	{
		switch (sUriMatcher.match(uri))
		{
			case CALENDAR:
				return CalendarColumn.CONTENT_TYPE;

			case CALENDAR_ID:
				return CalendarColumn.CONTENT_ITEM_TYPE;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	@Override
	//插入数据库
	public Uri insert(Uri uri, ContentValues initialValues)
	{
		if (sUriMatcher.match(uri) != CALENDAR)
		{
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues values;
		if (initialValues != null)
		{
			values = new ContentValues(initialValues);
		}
		else
		{
			values = new ContentValues();
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		long rowId = db.insert(CalendarColumn_TABLE_NAME, CalendarColumn.CONTENT, values);
		if (rowId > 0)
		{
			Uri noteUri = ContentUris.withAppendedId(CalendarColumns.CalendarColumn.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
	@Override
	//删除数据
	public int delete(Uri uri, String where, String[] whereArgs)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri))
		{
			case CALENDAR:
				count = db.delete(CalendarColumn_TABLE_NAME, where, whereArgs);
				break;

			case CALENDAR_ID:
				String noteId = uri.getPathSegments().get(1);
				count = db.delete(CalendarColumn_TABLE_NAME, CalendarColumn._ID + "=" + noteId + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	@Override
	//更新数据
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri))
		{
			case CALENDAR:
				count = db.update(CalendarColumn_TABLE_NAME, values, where, whereArgs);
				break;

			case CALENDAR_ID:
				String noteId = uri.getPathSegments().get(1);
				count = db.update(CalendarColumn_TABLE_NAME, values, CalendarColumn._ID + "=" + noteId + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}
