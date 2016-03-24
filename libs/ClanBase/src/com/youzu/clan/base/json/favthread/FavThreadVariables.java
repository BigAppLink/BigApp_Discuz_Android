package com.youzu.clan.base.json.favthread;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;

import java.util.ArrayList;

public class FavThreadVariables extends PagedVariables {

	private static final long serialVersionUID = 7706298251287772954L;
	private ArrayList<Thread> list;
	private String count;
	private String needMore;
	private String favid;
	@Override
	public ArrayList<Thread> getList() {
		return list;
	}
	public void setList(ArrayList<Thread> list) {
		this.list = list;
	}
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

	public String getFavid() {
		return favid;
	}

	public void setFavid(String favid) {
		this.favid = favid;
	}
}
