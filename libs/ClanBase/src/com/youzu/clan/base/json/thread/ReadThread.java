package com.youzu.clan.base.json.thread;

import com.youzu.android.framework.db.annotation.Id;

public class ReadThread {
	@Id
	private long id;
	private String tid;
	private long ts;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
}
