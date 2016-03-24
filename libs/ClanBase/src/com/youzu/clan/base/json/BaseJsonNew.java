package com.youzu.clan.base.json;

import java.io.Serializable;


public class BaseJsonNew implements Serializable {
	private int status;
	private String msg;
	private BaseResponse res;


	public BaseResponse getRes() {
		return res;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
