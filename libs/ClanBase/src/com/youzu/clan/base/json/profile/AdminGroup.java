package com.youzu.clan.base.json.profile;

import java.io.Serializable;

public class AdminGroup implements Serializable{
	
	private static final long serialVersionUID = 4773325945277326995L;
	private String type;
	private String grouptitle;
	private String stars;
	private String color;
	private String icon;
	private String readaccess;
	private String allowgetattach;
	private String allowgetimage;
	private String allowmediacode;
	private String maxsigsize;
	private String allowbegincode;
	private String userstatusby;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGrouptitle() {
		return grouptitle;
	}
	public void setGrouptitle(String grouptitle) {
		this.grouptitle = grouptitle;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getReadaccess() {
		return readaccess;
	}
	public void setReadaccess(String readaccess) {
		this.readaccess = readaccess;
	}
	public String getAllowgetattach() {
		return allowgetattach;
	}
	public void setAllowgetattach(String allowgetattach) {
		this.allowgetattach = allowgetattach;
	}
	public String getAllowgetimage() {
		return allowgetimage;
	}
	public void setAllowgetimage(String allowgetimage) {
		this.allowgetimage = allowgetimage;
	}
	public String getAllowmediacode() {
		return allowmediacode;
	}
	public void setAllowmediacode(String allowmediacode) {
		this.allowmediacode = allowmediacode;
	}
	public String getMaxsigsize() {
		return maxsigsize;
	}
	public void setMaxsigsize(String maxsigsize) {
		this.maxsigsize = maxsigsize;
	}
	public String getAllowbegincode() {
		return allowbegincode;
	}
	public void setAllowbegincode(String allowbegincode) {
		this.allowbegincode = allowbegincode;
	}
	public String getUserstatusby() {
		return userstatusby;
	}
	public void setUserstatusby(String userstatusby) {
		this.userstatusby = userstatusby;
	}
	
	
}
