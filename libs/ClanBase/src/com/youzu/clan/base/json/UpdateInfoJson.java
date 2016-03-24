package com.youzu.clan.base.json;

import com.youzu.clan.base.json.update.AutoUpdateInfo;

public class UpdateInfoJson extends BaseResponse {
	
	private AutoUpdateInfo data;

	@Override
	public AutoUpdateInfo getData() {
		return data;
	}

	public void setData(AutoUpdateInfo data) {
		this.data = data;
	}
}
