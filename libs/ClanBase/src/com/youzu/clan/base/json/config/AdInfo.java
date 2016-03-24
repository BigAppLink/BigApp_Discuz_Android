package com.youzu.clan.base.json.config;

import android.content.Context;

import java.io.Serializable;

public class AdInfo implements Serializable {
	private static final long serialVersionUID = 7925030127241950522L;
	private String recomId;
	private String action;
	private String forumId;
	private String forumName;
	private String forumNavId;
	private String forumNavName;
	private int showPosition;
	private String randomNum;
	
	private AdInfo(Context context) {
	}
	public static AdInfo click(Context context) {
		AdInfo adInfo = new AdInfo(context);
		adInfo.action = "click";
		return adInfo;
	}
	
	public static AdInfo show(Context context) {
		AdInfo adInfo = new AdInfo(context);
		adInfo.action = "show";
		return adInfo;
	}

	public String getRandomNum() {
		return randomNum;
	}
	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}
	public String getRecomId() {
		return recomId;
	}

	public void setRecomId(String recomId) {
		this.recomId = recomId;
	}

	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	public String getForumNavId() {
		return forumNavId;
	}

	public void setForumNavId(String forumNavId) {
		this.forumNavId = forumNavId;
	}

	public String getForumNavName() {
		return forumNavName;
	}

	public void setForumNavName(String forumNavName) {
		this.forumNavName = forumNavName;
	}

	public int getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(int showPosition) {
		this.showPosition = showPosition;
	}

	public String getAction() {
		return action;
	}

}
