package com.youzu.clan.base.json.search;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;

public class SearchForumJson extends BaseJson{
	private static final long serialVersionUID = 4632639636665615828L;
	
	@JSONField(name="Variables")
	private SearchForumVariables variables;
	
	public SearchForumVariables getVariables() {
		return variables;
	}
	public void setVariables(SearchForumVariables variables) {
		this.variables = variables;
	}
}
