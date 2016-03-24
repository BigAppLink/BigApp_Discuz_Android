//package com.kit.widget.update;
//
//import java.io.File;
//
//
//
//import com.kit.widget.dialog.DefaultDialog;
////import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
//import com.kit.extend.widget.R;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//
//public class UpdateChecker {
//
//	public Context mContext;
//
//	public static DefaultDialog checkVersion(final Context mContext,
//			double serverVersionName, double versionName, String title,
//			String downloadUrl) {
//		// System.out.println(updateInfo.getLocalVersionName() + " "
//		// + updateInfo.getServerVersionName());
//
//		if (versionName < serverVersionName) {
//
//
//
//			final DefaultDialog mDialog = new DefaultDialog(mContext,
//					mContext.getString(R.string.is_update),
//					R.layout.dialog_default, true);
//
//			if(mDialog.mButtonCancel!=null){
//				mDialog.mButtonCancel.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mDialog.cancel();
//
//					}
//				});
//			}
//			mDialog.show();
//
//			return mDialog;
//
//		} else {
//			File updateFile = new File(downloadUrl, title + ".apk");
//			if (updateFile.exists()) {
//				// 当不需要的时候，清除之前的下载文件，避免浪费用户空间
//				updateFile.delete();
//
//			}
//			return null;
//		}
//
//	}
//
//}
