package com.youzu.clan.base.json;

import com.youzu.clan.base.json.mypm.ChatVariables;

public class ChatJson extends BaseJson {
	
	private ChatVariables variables;

	@Override
	public ChatVariables getVariables() {
		return variables;
	}

	public void setVariables(ChatVariables variables) {
		this.variables = variables;
	}
}
