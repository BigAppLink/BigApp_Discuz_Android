package com.youzu.clan.base.json.search;

import java.util.ArrayList;
import java.util.List;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.json.model.Variables;

public class SearchThreadVariables extends PagedVariables{
	private static final long serialVersionUID = 7596446775686810626L;
	private String tpp;
	private String page;
	@JSONField(name="need_more")
	private String needMore;
	@JSONField(name="thread_list")
	private ArrayList<SearchThread> threadList;
	
	public String getTpp() {
		return tpp;
	}
	public void setTpp(String tpp) {
		this.tpp = tpp;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getNeedMore() {
		return needMore;
	}
	public void setNeedMore(String needMore) {
		this.needMore = needMore;
	}
	public ArrayList<SearchThread> getThreadList() {
		return threadList;
	}
	public void setThreadList(ArrayList<SearchThread> threadList) {
		this.threadList = threadList;
	}

    @Override
    public List getList() {
        return threadList;
    }
}
