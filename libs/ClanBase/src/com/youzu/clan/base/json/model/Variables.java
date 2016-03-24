package com.youzu.clan.base.json.model;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

public class Variables implements Serializable {
	
	private static final long serialVersionUID = 7043670579715811615L;
	private String cookiepre;
	private String auth;
	private String saltkey;
	private String memberUid;
	private String memberUsername;
	private String memberAvatar;
	
	private String groupid;
	private String formhash;
	private String ismoderator;
	private String readaccess;
	private Notice notice;
	private ArrayList<String> trace;
	
	public String getCookiepre() {
		return cookiepre;
	}
	public void setCookiepre(String cookiepre) {
		this.cookiepre = cookiepre;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getSaltkey() {
		return saltkey;
	}
	public void setSaltkey(String saltkey) {
		this.saltkey = saltkey;
	}
	public String getMemberUid() {
		return memberUid;
	}
	
	@JSONField(name="member_uid")
	public void setMemberUid(String memberUid) {
		this.memberUid = memberUid;
	}
	public String getMemberUsername() {
		return memberUsername;
	}
	@JSONField(name="member_username")
	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
	}
	public String getMemberAvatar() {
		return memberAvatar;
	}
	@JSONField(name="member_avatar")
	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getFormhash() {
		return formhash;
	}
	public void setFormhash(String formhash) {
		this.formhash = formhash;
	}
	public String getIsmoderator() {
		return ismoderator;
	}
	public void setIsmoderator(String ismoderator) {
		this.ismoderator = ismoderator;
	}
	public String getReadaccess() {
		return readaccess;
	}
	public void setReadaccess(String readaccess) {
		this.readaccess = readaccess;
	}
	public Notice getNotice() {
		return notice;
	}
	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	public ArrayList<String> getTrace() {
		return trace;
	}
	public void setTrace(ArrayList<String> trace) {
		this.trace = trace;
	}
	
	
	
}
