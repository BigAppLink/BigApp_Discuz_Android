package com.youzu.clan.base.json.favthread;

import java.io.Serializable;

public class Thread implements Serializable {
	private static final long serialVersionUID = 7886303572634660613L;
	private String favid;
	private String uid;
	private String id;
	private String idtype;
	private String spaceuid;
	private String title;
	private String description;
	private String dateline;
	private String icon;
	private String url;
	private String replies;
	private String author;
	public String getFavid() {
		return favid;
	}
	public void setFavid(String favid) {
		this.favid = favid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getSpaceuid() {
		return spaceuid;
	}
	public void setSpaceuid(String spaceuid) {
		this.spaceuid = spaceuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReplies() {
		return replies;
	}
	public void setReplies(String replies) {
		this.replies = replies;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
}
