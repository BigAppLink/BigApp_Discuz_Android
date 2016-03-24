package com.youzu.clan.base.json.forum;

import java.io.Serializable;

public abstract class BaseForum implements Serializable{
	private static final long serialVersionUID = -6961600628754411671L;

	private String threads;
	private String posts;
	private String todayposts;
	private String yesterdayposts;
	private String icon;
	private String name;
	public abstract String getId();

	public String getThreads() {
		return threads;
	}

	public void setThreads(String threads) {
		this.threads = threads;
	}

	public String getPosts() {
		return posts;
	}

	public void setPosts(String posts) {
		this.posts = posts;
	}

	public String getTodayposts() {
		return todayposts;
	}

	public void setTodayposts(String todayposts) {
		this.todayposts = todayposts;
	}

	public String getYesterdayposts() {
		return yesterdayposts;
	}

	public void setYesterdayposts(String yesterdayposts) {
		this.yesterdayposts = yesterdayposts;
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

}
