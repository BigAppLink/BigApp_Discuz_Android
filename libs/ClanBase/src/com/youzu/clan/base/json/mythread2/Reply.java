package com.youzu.clan.base.json.mythread2;
import com.youzu.clan.base.json.forumdisplay.Thread;

import java.io.Serializable;

public class Reply implements Serializable {
	private static final long serialVersionUID = 9007782903452185771L;
	private String avatar;
	private com.youzu.clan.base.json.forumdisplay.Thread thread;
	private Detail detail;
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public Detail getDetail() {
		return detail;
	}
	public void setDetail(Detail detail) {
		this.detail = detail;
	}
	
}
