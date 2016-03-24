package com.youzu.clan.base.json.article;

import com.youzu.android.framework.db.annotation.Id;

public class ReadArticle {

	@Id
	private String aid;
	private long ts;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}
}
