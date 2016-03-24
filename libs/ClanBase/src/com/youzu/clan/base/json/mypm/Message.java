package com.youzu.clan.base.json.mypm;

import com.youzu.android.framework.json.annotation.JSONField;

public class Message {
	private String plid;
	private String isnew;
	private String pmnum;
	private String lastupdate;
	private String lastdateline;
	private String authorid;
	private String pmtype;
	private String subject;
	private String members;
	private String dateline;
	private String touid;
	private String pmid;
	private String lastauthorid;
	private String lastauthor;
	private String msgfromid;
	private String msgfrom;
	private String msgtoid;
	private String tousername;
	@JSONField(name="msgtoid_avatar") private String msgtoidAvatar;
	@JSONField(name="msgfromid_avatar") private String msgfromidAvatar;
	public String getPlid() {
		return plid;
	}
	public void setPlid(String plid) {
		this.plid = plid;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	public String getPmnum() {
		return pmnum;
	}
	public void setPmnum(String pmnum) {
		this.pmnum = pmnum;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
	public String getLastdateline() {
		return lastdateline;
	}
	public void setLastdateline(String lastdateline) {
		this.lastdateline = lastdateline;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getPmtype() {
		return pmtype;
	}
	public void setPmtype(String pmtype) {
		this.pmtype = pmtype;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getTouid() {
		return touid;
	}
	public void setTouid(String touid) {
		this.touid = touid;
	}
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public String getLastauthorid() {
		return lastauthorid;
	}
	public void setLastauthorid(String lastauthorid) {
		this.lastauthorid = lastauthorid;
	}
	public String getLastauthor() {
		return lastauthor;
	}
	public void setLastauthor(String lastauthor) {
		this.lastauthor = lastauthor;
	}
	public String getMsgfromid() {
		return msgfromid;
	}
	public void setMsgfromid(String msgfromid) {
		this.msgfromid = msgfromid;
	}
	public String getMsgfrom() {
		return msgfrom;
	}
	public void setMsgfrom(String msgfrom) {
		this.msgfrom = msgfrom;
	}
	public String getMsgtoid() {
		return msgtoid;
	}
	public void setMsgtoid(String msgtoid) {
		this.msgtoid = msgtoid;
	}
	public String getTousername() {
		return tousername;
	}
	public void setTousername(String tousername) {
		this.tousername = tousername;
	}
	public String getMsgtoidAvatar() {
		return msgtoidAvatar;
	}
	public void setMsgtoidAvatar(String msgtoidAvatar) {
		this.msgtoidAvatar = msgtoidAvatar;
	}
	public String getMsgfromidAvatar() {
		return msgfromidAvatar;
	}
	public void setMsgfromidAvatar(String msgfromidAvatar) {
		this.msgfromidAvatar = msgfromidAvatar;
	}
	
	
}
