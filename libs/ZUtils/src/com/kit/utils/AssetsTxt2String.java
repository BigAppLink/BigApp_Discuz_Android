package com.kit.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsTxt2String {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static String getString(Context context, String fileName) {
		String str = "";

		String encoding = "UTF-8"; // 字符编码(可解决中文乱码问题 )
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		InputStreamReader isReader = null;
		try {
			isReader = new InputStreamReader(is, encoding);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BufferedReader bReader = new BufferedReader(isReader);
		String lineTXT = null;
		try {
			while ((lineTXT = bReader.readLine()) != null) {
				// String [] mimeTypes = lineTXT.trim().split(" ");
				// System.out.println(mimeTypes );

				str += lineTXT.toString().trim();

				// System.out.println(lineTXT.toString().trim());

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return str;

	}

	public static String txt2String(Context context, String fileName) {
		String str = "";

		String encoding = "UTF-8"; // 字符编码(可解决中文乱码问题 )
		//String encoding = "Unicode"; // 字符编码(可解决中文乱码问题 )
		InputStream is = null;
		try {
			is = context.getResources().getAssets().open(fileName);
			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a string.
			str = new String(buffer, encoding);

		} catch (IOException e) {
			// Should never happen!
			throw new RuntimeException(e);
		}

		return str;

	}

}
