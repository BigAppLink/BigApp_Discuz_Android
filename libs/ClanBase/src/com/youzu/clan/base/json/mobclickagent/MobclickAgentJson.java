package com.youzu.clan.base.json.mobclickagent;

import com.youzu.clan.base.json.BaseJsonNew;

public class MobclickAgentJson extends BaseJsonNew {

	private static final long serialVersionUID = 894045894026317236L;


	private MobclickAgentRes res;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public MobclickAgentRes getRes() {
		return res;
	}

	public void setRes(MobclickAgentRes res) {
		this.res = res;
	}
}
