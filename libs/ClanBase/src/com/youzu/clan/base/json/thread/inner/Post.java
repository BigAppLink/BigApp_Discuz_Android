package com.youzu.clan.base.json.thread.inner;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.ArrayList;

/**
 * 帖子
 * @author wangxi
 *
 */
public class Post {
	private String pid;
	private String tid;
	private String first;
	private String author;
	private String authorid;
	private String dateline;
	private String message;
	private String anonymous;
	private String attachment;
	private String status;
	private String position;
	private String uid;
	private String extcredits2;
	private String posts;
	private String threads;
	private String username;
	private String adminid;
	private String groupid;
	private String memberstatus;
	private String number;
	private String dbdateline;
	private String authortitle;
	private ArrayList<Attachment> attachments;
	private String recommends;
	private String recommendAdd;
	private String recommend_sub;
	private String enableRecommend;
	private String click2login;
	private String addtext;
	private String subtext;
	private String recommendValue;
	private String recommended;


	private String attachlist;
	private String imagelist;
	private String enableSupport;
	private String support;





//	private ArrayList<String> attachlist;
//	private ArrayList<String> imagelist;
	private String avatar;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
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

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getExtcredits2() {
		return extcredits2;
	}

	public void setExtcredits2(String extcredits2) {
		this.extcredits2 = extcredits2;
	}

	public String getPosts() {
		return posts;
	}

	public void setPosts(String posts) {
		this.posts = posts;
	}

	public String getThreads() {
		return threads;
	}

	public void setThreads(String threads) {
		this.threads = threads;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAdminid() {
		return adminid;
	}

	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getMemberstatus() {
		return memberstatus;
	}

	public void setMemberstatus(String memberstatus) {
		this.memberstatus = memberstatus;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDbdateline() {
		return dbdateline;
	}

	public void setDbdateline(String dbdateline) {
		this.dbdateline = dbdateline;
	}

	public String getAuthortitle() {
		return authortitle;
	}

	public void setAuthortitle(String authortitle) {
		this.authortitle = authortitle;
	}

	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getRecommends() {
		return recommends;
	}

	public void setRecommends(String recommends) {
		this.recommends = recommends;
	}

	public String getRecommendAdd() {
		return recommendAdd;
	}

	@JSONField(name="recommend_add")
	public void setRecommendAdd(String recommendAdd) {
		this.recommendAdd = recommendAdd;
	}

	public String getRecommend_sub() {
		return recommend_sub;
	}

	public void setRecommend_sub(String recommend_sub) {
		this.recommend_sub = recommend_sub;
	}
	public String getEnableRecommend() {
		return enableRecommend;
	}
	@JSONField(name="enable_recommend")
	public void setEnableRecommend(String enableRecommend) {
		this.enableRecommend = enableRecommend;
	}


	public String getClick2login() {
		return click2login;
	}

	public void setClick2login(String click2login) {
		this.click2login = click2login;
	}

	public String getAddtext() {
		return addtext;
	}

	public void setAddtext(String addtext) {
		this.addtext = addtext;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String subtext) {
		this.subtext = subtext;
	}

	public String getRecommendValue() {
		return recommendValue;
	}

	@JSONField(name="recommend_value")
	public void setRecommendValue(String recommendValue) {
		this.recommendValue = recommendValue;
	}

	public String getRecommended() {
		return recommended;
	}

	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}

	public String getAttachlist() {
		return attachlist;
	}

	public void setAttachlist(String attachlist) {
		this.attachlist = attachlist;
	}

	public String getImagelist() {
		return imagelist;
	}

	public void setImagelist(String imagelist) {
		this.imagelist = imagelist;
	}

	public String getEnableSupport() {
		return enableSupport;
	}
	@JSONField(name="enable_support")
	public void setEnableSupport(String enableSupport) {
		this.enableSupport = enableSupport;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}

	//	public ArrayList<String> getAttachlist() {
//		return attachlist;
//	}
//
//	public void setAttachlist(ArrayList<String> attachlist) {
//		this.attachlist = attachlist;
//	}
//
//	public ArrayList<String> getImagelist() {
//		return imagelist;
//	}
//
//	public void setImagelist(ArrayList<String> imagelist) {
//		this.imagelist = imagelist;
//	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
