package com.youzu.clan.base.json.profile;

import java.io.Serializable;

public class Feed implements Serializable {
	private static final long serialVersionUID = -1865591672114301855L;
	private String doing;
	private String blog;
	private String upload;
	private String poll;
	private String newthread;
	public String getDoing() {
		return doing;
	}
	public void setDoing(String doing) {
		this.doing = doing;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public String getPoll() {
		return poll;
	}
	public void setPoll(String poll) {
		this.poll = poll;
	}
	public String getNewthread() {
		return newthread;
	}
	public void setNewthread(String newthread) {
		this.newthread = newthread;
	}
	
	
}
