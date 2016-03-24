package com.youzu.clan.base.json.search;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;

public class SearchUserJson extends BaseJson{
	private static final long serialVersionUID = 4632639636665615828L;
	
	@JSONField(name="Variables")
	private SearchUserVariables variables;
	
	public SearchUserVariables getVariables() {
		return variables;
	}
	public void setVariables(SearchUserVariables variables) {
		this.variables = variables;
	}
}
