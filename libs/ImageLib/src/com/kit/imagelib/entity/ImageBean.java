package com.kit.imagelib.entity;

import java.io.Serializable;

public class ImageBean implements Serializable {

	/**
	 * 资源文件只有id
	 */
	public int drawableId;

	public String parentName;
	public long size;
	public String displayName;
	public String path;
	public boolean isChecked;

	/**
	 * thumbnail_pic 缩略图
	 */
	public String thumbnail_pic;

	/**
	 * bmiddle_pic 中等图
	 */
	public String bmiddle_pic;

	/**
	 * original_pic 高清图
	 */
	public String original_pic;


	public ImageBean() {
		super();
	}

	public ImageBean(String path) {
		super();
		this.path = path;
	}


	public ImageBean(int drawableId) {
		super();
		this.drawableId = drawableId;
	}

	public ImageBean(String parentName, long size, String displayName,
			String path, boolean isChecked) {
		super();
		this.parentName = parentName;
		this.size = size;
		this.displayName = displayName;
		this.path = path;
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "ImageBean [parentName=" + parentName + ", size=" + size
				+ ", displayName=" + displayName + ", path=" + path
				+ ", isChecked=" + isChecked + "]";
	}

}
