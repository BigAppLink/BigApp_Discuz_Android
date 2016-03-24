package com.kit.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AssetsUtils extends  AssetsTxt2String{
	public static String getFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line+"\\n";
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
