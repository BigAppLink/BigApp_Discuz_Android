package com.youzu.clan.base.json.profile;

import java.io.Serializable;

import com.youzu.android.framework.json.annotation.JSONField;

public class Extcredits implements Serializable {
	private static final long serialVersionUID = -7863707821961450266L;
	@JSONField(name="1") private Extcredit extcredit1;
	@JSONField(name="2") private Extcredit extcredit2;
	@JSONField(name="3") private Extcredit extcredit3;
	public Extcredit getExtcredit1() {
		return extcredit1;
	}
	public void setExtcredit1(Extcredit extcredit1) {
		this.extcredit1 = extcredit1;
	}
	public Extcredit getExtcredit2() {
		return extcredit2;
	}
	public void setExtcredit2(Extcredit extcredit2) {
		this.extcredit2 = extcredit2;
	}
	public Extcredit getExtcredit3() {
		return extcredit3;
	}
	public void setExtcredit3(Extcredit extcredit3) {
		this.extcredit3 = extcredit3;
	}
	
}
