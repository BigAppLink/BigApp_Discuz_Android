package com.kit.widget.calendar;

import android.net.Uri;
import android.provider.BaseColumns;

public class CalendarColumns {
	//ContentProvider的uri
	public static final String	AUTHORITY	= "com.calendar.sqlite.CalendarProvider";

	private CalendarColumns(){}

	// 定义基本字段
	public static final class CalendarColumn implements BaseColumns
	{
		private CalendarColumn(){}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/CalendarColumn");

		// 新的MIME类型-多个
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.calendar.calendar";

		// 新的MIME类型-单个
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.calendar.calendar";

		public static final String DEFAULT_SORT_ORDER = "created DESC";

		//字段
		public static final String CONTENT = "content";
		public static final String CREATED = "created";
		public static final String DATE = "date";
		public static final String TIME = "time";
		public static final String ISORUSE = "isOrUse";
		public static final String ISORREVEAL = "isOrReveal";
		
	}
}
