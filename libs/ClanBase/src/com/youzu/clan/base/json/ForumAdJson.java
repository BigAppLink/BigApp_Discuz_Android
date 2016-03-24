package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;

public class ForumAdJson {
	private String recom_id;
	private String icon;
	private String name;
	private String content;
	private long time = System.currentTimeMillis();
	@JSONField(name="image")
	private String[] images;
	@JSONField(name="recom_name")
	private String recomName;
	@JSONField(name="download_name")
	private String downloadName;
	private int position;
	private String clickurl;
	@JSONField(name="random_num")
	private String randomNum;
	public String getRecom_id() {
		return recom_id;
	}
	public void setRecom_id(String recom_id) {
		this.recom_id = recom_id;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getRecomName() {
		return recomName;
	}
	public void setRecomName(String recomName) {
		this.recomName = recomName;
	}
	public String getDownloadName() {
		return downloadName;
	}
	public void setDownloadName(String downloadName) {
		this.downloadName = downloadName;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getClickurl() {
		return clickurl;
	}
	public void setClickurl(String clickurl) {
		this.clickurl = clickurl;
	}
	public String getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
