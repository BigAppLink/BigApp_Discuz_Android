package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;

public class SplashAdJson {
	private long showtime;
	private String image;
	private String recomId;
	private String clickurl;
	@JSONField(name="random_num")
	private String randomNum;
	public long getShowtime() {
		return showtime;
	}
	public void setShowtime(long showtime) {
		this.showtime = showtime;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getRecomId() {
		return recomId;
	}
	public void setRecomId(String recomId) {
		this.recomId = recomId;
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
	
	
}
