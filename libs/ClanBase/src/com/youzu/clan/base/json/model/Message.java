package com.youzu.clan.base.json.model;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 4325729296969339804L;
	private String messageval;
	private String messagestr;

	public String getMessageval() {
		return messageval;
	}
	public void setMessageval(String messageval) {
		this.messageval = messageval;
	}
	public String getMessagestr() {
		return messagestr;
	}
	public void setMessagestr(String messagestr) {
		this.messagestr = messagestr;
	}
	
	
}
