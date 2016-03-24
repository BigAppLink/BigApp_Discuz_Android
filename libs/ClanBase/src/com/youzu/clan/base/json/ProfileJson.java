package com.youzu.clan.base.json;

import com.youzu.clan.base.json.profile.ProfileVariables;
import com.youzu.clan.base.json.profile.Space;

/**
 * 个人中心
 * @author wangxi
 *
 */
public class ProfileJson extends BaseJson {

	private static final long serialVersionUID = -9124542162087122769L;
	private ProfileVariables variables;
	private Space data;
	@Override
	public ProfileVariables getVariables() {
		return variables;
	}
	
	public void setVariables(ProfileVariables variables) {
		this.variables = variables;
	}


	public Space getData() {
		return data;
	}

	public void setData(Space data) {
		this.data = data;
	}


}
