package com.youzu.clan.base.json.checkpost;

import com.youzu.android.framework.json.annotation.JSONField;

import java.util.Map;

public class Allowperm {
	@JSONField(name="allowpost")
	private String allowPost;

	@JSONField(name="allowreply")
	private String allowReply;

	@JSONField(name="uploadhash")
	private String uploadHash;

	@JSONField(name="allowupload")
	private Map<String,String> allowUpload;



	public String getAllowPost() {
		return allowPost;
	}

	public void setAllowPost(String allowPost) {
		this.allowPost = allowPost;
	}

	public String getAllowReply() {
		return allowReply;
	}

	public void setAllowReply(String allowReply) {
		this.allowReply = allowReply;
	}

	public String getUploadHash() {
		return uploadHash;
	}

	public void setUploadHash(String uploadHash) {
		this.uploadHash = uploadHash;
	}

	public Map<String,String>  getAllowUpload() {
		return allowUpload;
	}

	public void setAllowUpload(Map<String,String>  allowUpload) {
		this.allowUpload = allowUpload;
	}
}
