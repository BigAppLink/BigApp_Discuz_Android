package com.youzu.clan.base.json;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.login.YZLoginParams;

public class YZLoginJson {
	private String error;
	private String errorMsg;
	private YZLoginParams params;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	@JSONField(name="error_msg")
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public YZLoginParams getParams() {
		return params;
	}
	public void setParams(YZLoginParams params) {
		this.params = params;
	}
	
	public boolean isSuccess() {
		return "0".equals(error);
	}
	
	public boolean needNickname() {
		return "1".equals(error);
	}
	
	
}
