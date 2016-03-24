package com.youzu.clan.base.json.threadview.comment;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;


/**
 * 点评帖子前置检查返回值
 */
public class CommentCheckJson extends BaseJson {
	private CommentCheckVariables variables;

	public CommentCheckVariables getVariables() {
		return variables;
	}

	@JSONField(name="Variables")
	public void setVariables(CommentCheckVariables variables) {
		this.variables = variables;
	}
}
