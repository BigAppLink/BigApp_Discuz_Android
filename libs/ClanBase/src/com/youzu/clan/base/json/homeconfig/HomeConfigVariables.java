package com.youzu.clan.base.json.homeconfig;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.model.Variables;

public class HomeConfigVariables extends Variables{
	private HomeConfig myHome;

	public HomeConfig getMyHome() {
		return myHome;
	}

	@JSONField(name = "myhome")
	public void setMyHome(HomeConfig myHome) {
		this.myHome = myHome;
	}
}
