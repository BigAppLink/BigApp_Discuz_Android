package com.youzu.clan.base.json.favforum;

import com.youzu.android.framework.JsonUtils;
import com.youzu.android.framework.db.annotation.Id;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.json.forum.BaseForum;

public class Forum extends BaseForum {
	private static final long serialVersionUID = -2168141218996214085L;
	private String favid;
	private String uid;
	@Id
	private String id;
	private String idtype;
	private String spaceuid;
	private String description;
	private String dateline;
	private String url;
	public String getFavid() {
		return favid;
	}
	public void setFavid(String favid) {
		this.favid = favid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getSpaceuid() {
		return spaceuid;
	}
	public void setSpaceuid(String spaceuid) {
		this.spaceuid = spaceuid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getFid() {
		return id;
	}

	public NavForum toNavForum() {
		String str = JsonUtils.toJSON(this).toString();

		NavForum f = JsonUtils.parseObject(str, NavForum.class);
		return f;
	}


}
