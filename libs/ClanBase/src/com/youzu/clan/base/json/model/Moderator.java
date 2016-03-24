package com.youzu.clan.base.json.model;

import java.io.Serializable;

public class Moderator implements Serializable {
	private static final long serialVersionUID = 1014458428195388102L;
	private String uid;
	private String username;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
