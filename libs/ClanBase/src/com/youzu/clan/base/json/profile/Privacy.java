package com.youzu.clan.base.json.profile;

import java.io.Serializable;

public class Privacy implements Serializable{
	private static final long serialVersionUID = -1990880984910195669L;
	private Feed feed;
	private View view;
	private Profile profile;
	public Feed getFeed() {
		return feed;
	}
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	
}
