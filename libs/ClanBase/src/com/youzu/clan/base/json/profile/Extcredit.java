package com.youzu.clan.base.json.profile;

import java.io.Serializable;

public class Extcredit implements Serializable {
	private static final long serialVersionUID = 7069718049500255220L;
	private String img;
	private String title;
	private String unit;
	private String ratio;
	private String showinthread;
	private String allowexchangein;
	private String allowexchangeout;

	private String value;
	private String name;

	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getShowinthread() {
		return showinthread;
	}
	public void setShowinthread(String showinthread) {
		this.showinthread = showinthread;
	}
	public String getAllowexchangein() {
		return allowexchangein;
	}
	public void setAllowexchangein(String allowexchangein) {
		this.allowexchangein = allowexchangein;
	}
	public String getAllowexchangeout() {
		return allowexchangeout;
	}
	public void setAllowexchangeout(String allowexchangeout) {
		this.allowexchangeout = allowexchangeout;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
