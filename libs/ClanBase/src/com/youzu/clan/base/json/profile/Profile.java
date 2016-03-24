package com.youzu.clan.base.json.profile;

import java.io.Serializable;

public class Profile implements Serializable {
	private static final long serialVersionUID = -116377879101069896L;
	private String realname;
	private String gender;
	private String birthday;
	private String birthcity;
	private String residecity;
	private String affectivestatus;
	private String lookingfor;
	private String bloodtype;
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBirthcity() {
		return birthcity;
	}
	public void setBirthcity(String birthcity) {
		this.birthcity = birthcity;
	}
	public String getResidecity() {
		return residecity;
	}
	public void setResidecity(String residecity) {
		this.residecity = residecity;
	}
	public String getAffectivestatus() {
		return affectivestatus;
	}
	public void setAffectivestatus(String affectivestatus) {
		this.affectivestatus = affectivestatus;
	}
	public String getLookingfor() {
		return lookingfor;
	}
	public void setLookingfor(String lookingfor) {
		this.lookingfor = lookingfor;
	}
	public String getBloodtype() {
		return bloodtype;
	}
	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}
	
	
}
