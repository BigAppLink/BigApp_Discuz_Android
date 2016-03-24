package com.youzu.clan.base.json.mypm;

import java.util.ArrayList;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;

/**
 * 消息
 * @author wangxi
 *
 */
public class MypmVariables extends PagedVariables {
	private static final long serialVersionUID = -1927982129926443352L;
	private ArrayList<Mypm> list;
	private String count;
	private String perpage;
	private String page;
	private String needMore;
	private String pmid;
	public ArrayList<Mypm> getList() {
		return list;
	}
	public void setList(ArrayList<Mypm> list) {
		this.list = list;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPerpage() {
		return perpage;
	}
	public void setPerpage(String perpage) {
		this.perpage = perpage;
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
	@JSONField(name="need_more") 
	public void setNeedMore(String needMore) {
		this.needMore = needMore;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	
	
	
	
}
