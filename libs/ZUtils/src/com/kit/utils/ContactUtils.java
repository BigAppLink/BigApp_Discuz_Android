package com.kit.utils;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

public class ContactUtils {

	public static void insertContact(Context context, String name, String phone) {
		// 首先插入空值，再得到rawContactsId ，用于下面插值
		ContentValues values = new ContentValues();
		// insert a null value
		Uri rawContactUri = context.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactsId = ContentUris.parseId(rawContactUri);

		// 往刚才的空记录中插入姓名
		values.clear();
		// A reference to the _ID that this data belongs to
		values.put(StructuredName.RAW_CONTACT_ID, rawContactsId);
		// "CONTENT_ITEM_TYPE" MIME type used when storing this in data table
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		// The name that should be used to display the contact.
		values.put(StructuredName.DISPLAY_NAME, name);
		// insert the real values
		context.getContentResolver().insert(Data.CONTENT_URI, values);
		// 插入电话
		values.clear();
		values.put(Phone.RAW_CONTACT_ID, rawContactsId);
		// String "Data.MIMETYPE":The MIME type of the item represented by this
		// row
		// String "CONTENT_ITEM_TYPE": MIME type used when storing this in data
		// table.
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, phone);
		context.getContentResolver().insert(Data.CONTENT_URI, values);
	}

	public static Cursor getContacts(Context context) {
		// 获得所有的联系人
		Cursor cur = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// Log.i("contactId",contactId);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				// Log.i("disPlayName",disPlayName);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							// Log.i("phoneNumber",phoneNumber);
						} while (phones.moveToNext());
					}

				}

			} while (cur.moveToNext());

		}
		return cur;
	}

	// 查找联系人
	public void getUserInfo(Context context) {
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// Log.d(TAG , "Name is : "+name);
			int isHas = Integer
					.parseInt(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
			if (isHas > 0) {
				Cursor c = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + id, null, null);
				while (c.moveToNext()) {
					String number = c
							.getString(c
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Log.d(TAG , "Number is : "+number);
				}
				c.close();
			}
		}
		cursor.close();
	}

	// 根据号码找联系人的情况，及时提示用户。
	public static ArrayList<String> getNameFromPhone(Context context,
			String number) {
		ArrayList<String> name = new ArrayList<String>();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, // Which columns to return.
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number
						+ "'", // WHERE clause.
				null, // WHERE clause value substitution
				null); // Sort order.

		if (cursor == null) {
			// Log.d(TAG, "getPeople null");
			return null;
		}
		// Log.d(TAG, "getPeople cursor.getCount() = " + cursor.getCount());
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String nameTemp = cursor.getString(nameFieldColumnIndex);
			// Log.i(TAG, "" + name + " .... " + nameFieldColumnIndex);
			name.add(nameTemp);
		}
		cursor.close();
		return name;

	}

	/**
	 * 
	 * @Title getPhoneFromName
	 * @Description 根据姓名取得手机号码
	 * @param
	 * @return String 返回类型
	 */
	public static ArrayList<String> getPhoneFromName(Context context,
			String name) {
		ArrayList<String> phone = new ArrayList<String>();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };
		String[] selectionArgs = new String[] { "%" + name + "%" };
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, // Which columns to return.
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " like ? ", // WHERE clause.
				selectionArgs, // WHERE clause value substitution
				null); // Sort order.

		if (cursor == null) {
			// Log.d(TAG, "getPeople null");
			return null;
		}
		ZogUtils.printLog(ContactUtils.class, "getPeople cursor.getCount() = "
                + cursor.getCount());
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameFieldColumnIndex);

			String phoneNumber = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			// Log.i(TAG, "" + name + " .... " + nameFieldColumnIndex);

			phone.add(phoneNumber);

		}
		cursor.close();
		return phone;

	}

}
