package com.youzu.clan.base.json.threadview.rate;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.BaseJson;


/**
 * 点评帖子前置检查返回值
 */
public class ViewRatingJson extends BaseJson {
	private ViewRatingVariables variables;

	public ViewRatingVariables getVariables() {
		return variables;
	}

	@JSONField(name="Variables")
	public void setVariables(ViewRatingVariables variables) {
		this.variables = variables;
	}
}
