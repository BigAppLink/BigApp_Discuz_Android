package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.forumnav.ForumnavVariables;

public class ForumnavJson extends BaseJson {

	private static final long serialVersionUID = 894045894026317236L;
	@JSONField(name="Variables") private ForumnavVariables variables;

	public ForumnavVariables getVariables() {
		return variables;
	}

	public void setVariables(ForumnavVariables variables) {
		this.variables = variables;
	}
	
	
}
