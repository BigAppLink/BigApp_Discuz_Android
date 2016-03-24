package com.kit.widget.update;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 更新信息实体类 UpdateInfo.java
 */
public class UpdateInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5872395487568968823L;
	public String versionName; // 版本号
	public String serverVersionName; // 版本号

	public int versionCode; // 版本号
	public int serverVersionCode; // 版本号

	public String url; // 新版本存放url路径
	public String description; // 更新说明信息，比如新增什么功能特性等
	public boolean isForce;// 是否为强制更新

	public String belong;// 属于谁

	public ArrayList<UserCondiction> condictions;

	/*
	 * userType=0 教师 userType=1 学生 userType=2 家长
	 * 
	 * status:0 表示不必须安装 status:1 表示必须安装 status:3 表示不必须安装，但要用提示
	 */
	public class UserCondiction {

		public int userType;

		public int status;

		public ArrayList<Condiction> condiction;
	}

	public class Condiction {

		public String packagename;

		public String packagetips;

		public String url;

	}
}
