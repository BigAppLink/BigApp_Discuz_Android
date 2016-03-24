package com.youzu.clan.base.json.model;

import java.util.List;

import com.youzu.android.framework.json.annotation.JSONField;

public abstract class PagedVariables extends Variables {
	
	private static final long serialVersionUID = 5261018475324864075L;
	private String count;
	private String needMore;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getNeedMore() {
		return needMore;
	}
	
	@JSONField(name="need_more")
	public void setNeedMore(String needMore) {
		this.needMore = needMore;
	}
	
	public abstract List getList();
	
}
