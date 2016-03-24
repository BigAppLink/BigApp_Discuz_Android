package com.youzu.clan.base.json.mypm;

import java.util.ArrayList;

public class MessageGroup {
	private long time;
	private ArrayList<Message> children;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public ArrayList<Message> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<Message> children) {
		this.children = children;
	}
	
}
