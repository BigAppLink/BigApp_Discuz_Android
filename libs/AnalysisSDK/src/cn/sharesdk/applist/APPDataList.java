package cn.sharesdk.applist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import m.framework.utils.Data;
import android.content.Context;
import android.os.Environment;

/** 评论列表 */
public class APPDataList {
	private static final int DB_VERSION = 1;
	private ArrayList<APP> applist;
	
	APPDataList() {
		applist = new ArrayList<APP>();
	}
	
	/**
	 * 下载评论列表
	 * <p>
	 * 此方法会执行联网操作，不应该在主线程中调用
	 */
	public void downloadList() {
		//TODO 下载应用推荐列表
		//TOD 保存应用列表
	}
		
	/** 获取下载apk目录路径 */
	private File getAppSaveFile() {
		String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File parentFile = new File(sdcardPath, "ShareSDKStatistics");
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		return parentFile;
	}
	
	/**
	 * 保存评论列表到本地磁盘
	 * 
	 * @return 返回true表示成功
	 */
	@Deprecated
	public boolean saveList(Context context) {
		String path = null;
		try {
			path = Data.MD5("app_list_") + DB_VERSION;
		} catch (Throwable t) {
			t.printStackTrace();
			path = null;
		}
		
		if (path != null) {
			File cacheFile = null;
			try {
				cacheFile = new File(getAppSaveFile(), path);
				if (cacheFile.exists()) {
					cacheFile.delete();
				}
				if (!cacheFile.getParentFile().exists()) {
					cacheFile.getParentFile().mkdirs();
				}
				cacheFile.createNewFile();
			} catch (Throwable t) {
				t.printStackTrace();
				cacheFile = null;
			}
			
			if (cacheFile != null) {
				try {
					FileOutputStream fos = new FileOutputStream(cacheFile);
					GZIPOutputStream gzos = new GZIPOutputStream(fos);
					ObjectOutputStream oos = new ObjectOutputStream(gzos);
					oos.writeObject(applist);
					oos.flush();
					oos.close();
					return true;
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 从本地磁盘读取评论列表
	 * 
	 * @return 返回true表示成功
	 */
	public boolean readList(Context context) {
		String path = null;
		try {
			path = Data.MD5("app_list_") + DB_VERSION;
		} catch (Throwable t) {
			t.printStackTrace();
			path = null;
		}
		
		if (path != null) {
			File cacheFile = null;
			try {
				cacheFile = new File(getAppSaveFile(), path);
				if (!cacheFile.exists()) {
					cacheFile = null;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				cacheFile = null;
			}
			
			if (cacheFile != null) {
				try {
					FileInputStream fis = new FileInputStream(cacheFile);
					GZIPInputStream gzis = new GZIPInputStream(fis);
					ObjectInputStream ois = new ObjectInputStream(gzis);
					@SuppressWarnings("unchecked")
					ArrayList<APP> list = (ArrayList<APP>) ois.readObject();
					ois.close();
					
					if (list != null && list.size() > 0) {
						applist = list;
						return true;
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	public int size() {
		return applist.size();
	}
	
	public static class APP{
		public String id;
		public String title;
		public String text;
		public String iconUrl;
		public String downloadUrl;
	}
	
}
