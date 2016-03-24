package com.youzu.clan.base.json.profile;

import com.youzu.clan.base.json.model.Variables;

public class ProfileVariables extends Variables {
	private static final long serialVersionUID = 1715402302348076579L;
	private Space space;
	private Extcredits extcredits;
	private Wsq wsq;
	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}
	public Extcredits getExtcredits() {
		return extcredits;
	}
	public void setExtcredits(Extcredits extcredits) {
		this.extcredits = extcredits;
	}
	public Wsq getWsq() {
		return wsq;
	}
	public void setWsq(Wsq wsq) {
		this.wsq = wsq;
	}
	
	
}
