package com.youzu.clan.base.json.model;

import java.io.Serializable;

public class Notice implements Serializable {
	
	private static final long serialVersionUID = -7990512265772848652L;
	
	private String newpush;
	private String newpm;
	private String newprompt;
	private String newmypost;
	public String getNewpush() {
		return newpush;
	}
	public void setNewpush(String newpush) {
		this.newpush = newpush;
	}
	public String getNewpm() {
		return newpm;
	}
	public void setNewpm(String newpm) {
		this.newpm = newpm;
	}
	public String getNewprompt() {
		return newprompt;
	}
	public void setNewprompt(String newprompt) {
		this.newprompt = newprompt;
	}
	public String getNewmypost() {
		return newmypost;
	}
	public void setNewmypost(String newmypost) {
		this.newmypost = newmypost;
	}
	
	
}
