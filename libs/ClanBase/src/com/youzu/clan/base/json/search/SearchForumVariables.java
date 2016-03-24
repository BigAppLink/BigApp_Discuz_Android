package com.youzu.clan.base.json.search;

import java.util.ArrayList;
import java.util.List;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.PagedVariables;
import com.youzu.clan.base.json.model.Variables;

public class SearchForumVariables extends PagedVariables{
	private static final long serialVersionUID = 7596446775686810626L;
	@JSONField(name="forum_list")
	private ArrayList<SearchForum> forumList;
	
	public ArrayList<SearchForum> getForumList() {
		return forumList;
	}
	public void setForumList(ArrayList<SearchForum> forumList) {
		this.forumList = forumList;
	}

    @Override
    public List getList() {
        return forumList;
    }
}
