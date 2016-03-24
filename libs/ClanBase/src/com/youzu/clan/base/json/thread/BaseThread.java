package com.youzu.clan.base.json.thread;

import android.text.SpannableStringBuilder;

import com.youzu.android.framework.db.annotation.Transient;
import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseThread implements Serializable{
	private static final long serialVersionUID = -756999223132973139L;
	private String tid;
	private String fid;
	private String readperm;
	private String author;
	private String authorid;
	private String subject;
	private String dateline;
	private String lastpost;
	private String lastposter;
	private String views;
	private String replies;
	private String attachment;
	private String heats;
	private String icon;
	private String digest;
	private String dbdateline;
	private String typeid;
	private String dblastpost;
	private String messageAbstract;
	private String avatar;
	private ArrayList<String> attachmentUrls;
	private String forumName;


	private String typename;
	private String typeicon;

	@Transient
	private SpannableStringBuilder spanStr;

	public SpannableStringBuilder getSpanStr() {
		return spanStr;
	}
	public void setSpanStr(SpannableStringBuilder spanStr) {
		this.spanStr = spanStr;
	}


	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getReadperm() {
		return readperm;
	}
	public void setReadperm(String readperm) {
		this.readperm = readperm;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorid() {
		return authorid;
	}
	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getLastpost() {
		return lastpost;
	}
	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}
	public String getLastposter() {
		return lastposter;
	}
	public void setLastposter(String lastposter) {
		this.lastposter = lastposter;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getReplies() {
		return replies;
	}
	public void setReplies(String replies) {
		this.replies = replies;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getHeats() {
		return heats;
	}
	public void setHeats(String heats) {
		this.heats = heats;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDbdateline() {
		return dbdateline;
	}
	public void setDbdateline(String dbdateline) {
		this.dbdateline = dbdateline;
	}
	public String getDblastpost() {
		return dblastpost;
	}
	public void setDblastpost(String dblastpost) {
		this.dblastpost = dblastpost;
	}
	public ArrayList<String> getAttachmentUrls() {
		return attachmentUrls;
	}
	@JSONField(name="attachment_urls")
	public void setAttachmentUrls(ArrayList<String> attachmentUrls) {
		this.attachmentUrls = attachmentUrls;
	}
	public String getMessageAbstract() {
		return messageAbstract;
	}
	@JSONField(name="message_abstract")
	public void setMessageAbstract(String messageAbstract) {
		this.messageAbstract = messageAbstract;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getTypeicon() {
		return typeicon;
	}
	public void setTypeicon(String typeicon) {
		this.typeicon = typeicon;
	}


	public String getForumName() {
		return forumName;
	}

	@JSONField(name="forum_name")
	public void setForumName(String forumName) {
		this.forumName = forumName;
	}
}
