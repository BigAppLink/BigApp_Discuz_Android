package com.youzu.clan.base.json.profile;

import com.youzu.android.framework.json.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

public class Space implements Serializable {
	private static final long serialVersionUID = -4724054727165257731L;
	private String uid;
	private String username;
	private String status;
	private String emailstatus;
	private String avatarstatus;
	private String videophotostatus;
	private String adminid;
	private String groupid;
	private String groupexpiry;
	private String extgroupids;
	private String regdate;
	private String credits;
	private String notifysound;
	private String timeoffset;
	private String newpm;
	private String newprompt;
	private String accessmasks;
	private String allowadmincp;
	private String onlyacceptfriendpm;
	private String conisbind;
	private String freeze;
	private String self;
	private Extcredit[] extcredits;
	private String friends;
	private String posts;
	private String threads;
	private String digestposts;
	private String doings;
	private String blogs;
	private String albums;
	private String sharings;
	private String attachsize;
	private String views;
	private String oltime;
	private String todayattachs;
	private String todayattachsize;
	private String feeds;
	private String follower;
	private String following;
	private String newfollower;
	private String blacklist;
	private String videophoto;
	private String spacename;
	private String spacedescription;
	private String domain;
	private String addsize;
	private String addfriend;
	private String menunum;
	private String theme;
	private String spacecss;
	private String blockposition;
	private String recentnote;
	private String spacenote;
	private Privacy privacy;
	private String feedfriend;
	private ArrayList<String> acceptemail;
	private String magicgift;
	private String stickblogs;
	private String publishfeed;
	private String customshow;
	private String customstatus;
	private String medals;
	private String sightml;
	private String groupterms;
	private String authstr;
	private String groups;
	private String attentiongroup;
	private String realname;
	private String gender;
	private String birthyear;
	private String birthmonth;
	private String birthday;
	private String constellation;
	private String zodiac;
	private String telephone;
	private String mobile;
	private String idcardtype;
	private String idcard;
	private String address;
	private String zipcode;
	private String nationality;
	private String birthprovince;
	private String birthcity;
	private String birthdist;
	private String birthcommunity;
	private String resideprovince;
	private String residecity;
	private String residedist;
	private String residecommunity;
	private String residesuite;
	private String graduateschool;
	private String company;
	private String education;
	private String occupation;
	private String position;
	private String revenue;
	private String affectivestatus;
	private String lookingfor;
	private String bloodtype;
	private String height;
	private String weight;
	private String alipay;
	private String icq;
	private String qq;
	private String yahoo;
	private String msn;
	private String taobao;
	private String site;
	private String bio;
	private String interest;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;
	private String field8;
	private String port;
	private String lastvisit;
	private String lastactivity;
	private String lastpost;
	private String lastsendmail;
	private String invisible;
	private String buyercredit;
	private String sellercredit;
	private String favtimes;
	private String sharetimes;
	private String profileprogress;
	private AdminGroup admingroup;
	private AdminGroup group;
	private String lastactivitydb;
	private String buyerrank;
	private String sellerrank;
	private String avatar;
	private String groupiconid;
	private String isMyFriend;

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmailstatus() {
		return emailstatus;
	}
	public void setEmailstatus(String emailstatus) {
		this.emailstatus = emailstatus;
	}
	public String getAvatarstatus() {
		return avatarstatus;
	}
	public void setAvatarstatus(String avatarstatus) {
		this.avatarstatus = avatarstatus;
	}
	public String getVideophotostatus() {
		return videophotostatus;
	}
	public void setVideophotostatus(String videophotostatus) {
		this.videophotostatus = videophotostatus;
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
	public String getGroupexpiry() {
		return groupexpiry;
	}
	public void setGroupexpiry(String groupexpiry) {
		this.groupexpiry = groupexpiry;
	}
	public String getExtgroupids() {
		return extgroupids;
	}
	public void setExtgroupids(String extgroupids) {
		this.extgroupids = extgroupids;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getCredits() {
		return credits;
	}
	public void setCredits(String credits) {
		this.credits = credits;
	}
	public String getNotifysound() {
		return notifysound;
	}
	public void setNotifysound(String notifysound) {
		this.notifysound = notifysound;
	}
	public String getTimeoffset() {
		return timeoffset;
	}
	public void setTimeoffset(String timeoffset) {
		this.timeoffset = timeoffset;
	}
	public String getNewpm() {
		return newpm;
	}
	public void setNewpm(String newpm) {
		this.newpm = newpm;
	}
	public String getNewprompt() {
		return newprompt;
	}
	public void setNewprompt(String newprompt) {
		this.newprompt = newprompt;
	}
	public String getAccessmasks() {
		return accessmasks;
	}
	public void setAccessmasks(String accessmasks) {
		this.accessmasks = accessmasks;
	}
	public String getAllowadmincp() {
		return allowadmincp;
	}
	public void setAllowadmincp(String allowadmincp) {
		this.allowadmincp = allowadmincp;
	}
	public String getOnlyacceptfriendpm() {
		return onlyacceptfriendpm;
	}
	public void setOnlyacceptfriendpm(String onlyacceptfriendpm) {
		this.onlyacceptfriendpm = onlyacceptfriendpm;
	}
	public String getConisbind() {
		return conisbind;
	}
	public void setConisbind(String conisbind) {
		this.conisbind = conisbind;
	}
	public String getFreeze() {
		return freeze;
	}
	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}
	public String getSelf() {
		return self;
	}
	public void setSelf(String self) {
		this.self = self;
	}
	public String getFriends() {
		return friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
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
	public String getDigestposts() {
		return digestposts;
	}
	public void setDigestposts(String digestposts) {
		this.digestposts = digestposts;
	}
	public String getDoings() {
		return doings;
	}
	public void setDoings(String doings) {
		this.doings = doings;
	}
	public String getBlogs() {
		return blogs;
	}
	public void setBlogs(String blogs) {
		this.blogs = blogs;
	}
	public String getAlbums() {
		return albums;
	}
	public void setAlbums(String albums) {
		this.albums = albums;
	}
	public String getSharings() {
		return sharings;
	}
	public void setSharings(String sharings) {
		this.sharings = sharings;
	}
	public String getAttachsize() {
		return attachsize;
	}
	public void setAttachsize(String attachsize) {
		this.attachsize = attachsize;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public String getOltime() {
		return oltime;
	}
	public void setOltime(String oltime) {
		this.oltime = oltime;
	}
	public String getTodayattachs() {
		return todayattachs;
	}
	public void setTodayattachs(String todayattachs) {
		this.todayattachs = todayattachs;
	}
	public String getTodayattachsize() {
		return todayattachsize;
	}
	public void setTodayattachsize(String todayattachsize) {
		this.todayattachsize = todayattachsize;
	}
	public String getFeeds() {
		return feeds;
	}
	public void setFeeds(String feeds) {
		this.feeds = feeds;
	}
	public String getFollower() {
		return follower;
	}
	public void setFollower(String follower) {
		this.follower = follower;
	}
	public String getFollowing() {
		return following;
	}
	public void setFollowing(String following) {
		this.following = following;
	}
	public String getNewfollower() {
		return newfollower;
	}
	public void setNewfollower(String newfollower) {
		this.newfollower = newfollower;
	}
	public String getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}
	public String getVideophoto() {
		return videophoto;
	}
	public void setVideophoto(String videophoto) {
		this.videophoto = videophoto;
	}
	public String getSpacename() {
		return spacename;
	}
	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}
	public String getSpacedescription() {
		return spacedescription;
	}
	public void setSpacedescription(String spacedescription) {
		this.spacedescription = spacedescription;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAddsize() {
		return addsize;
	}
	public void setAddsize(String addsize) {
		this.addsize = addsize;
	}
	public String getAddfriend() {
		return addfriend;
	}
	public void setAddfriend(String addfriend) {
		this.addfriend = addfriend;
	}
	public String getMenunum() {
		return menunum;
	}
	public void setMenunum(String menunum) {
		this.menunum = menunum;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getSpacecss() {
		return spacecss;
	}
	public void setSpacecss(String spacecss) {
		this.spacecss = spacecss;
	}
	public String getBlockposition() {
		return blockposition;
	}
	public void setBlockposition(String blockposition) {
		this.blockposition = blockposition;
	}
	public String getRecentnote() {
		return recentnote;
	}
	public void setRecentnote(String recentnote) {
		this.recentnote = recentnote;
	}
	public String getSpacenote() {
		return spacenote;
	}
	public void setSpacenote(String spacenote) {
		this.spacenote = spacenote;
	}
	public Privacy getPrivacy() {
		return privacy;
	}
	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}
	public String getFeedfriend() {
		return feedfriend;
	}
	public void setFeedfriend(String feedfriend) {
		this.feedfriend = feedfriend;
	}
	public ArrayList<String> getAcceptemail() {
		return acceptemail;
	}
	public void setAcceptemail(ArrayList<String> acceptemail) {
		this.acceptemail = acceptemail;
	}
	public String getMagicgift() {
		return magicgift;
	}
	public void setMagicgift(String magicgift) {
		this.magicgift = magicgift;
	}
	public String getStickblogs() {
		return stickblogs;
	}
	public void setStickblogs(String stickblogs) {
		this.stickblogs = stickblogs;
	}
	public String getPublishfeed() {
		return publishfeed;
	}
	public void setPublishfeed(String publishfeed) {
		this.publishfeed = publishfeed;
	}
	public String getCustomshow() {
		return customshow;
	}
	public void setCustomshow(String customshow) {
		this.customshow = customshow;
	}
	public String getCustomstatus() {
		return customstatus;
	}
	public void setCustomstatus(String customstatus) {
		this.customstatus = customstatus;
	}
	public String getMedals() {
		return medals;
	}
	public void setMedals(String medals) {
		this.medals = medals;
	}
	public String getSightml() {
		return sightml;
	}
	public void setSightml(String sightml) {
		this.sightml = sightml;
	}
	public String getGroupterms() {
		return groupterms;
	}
	public void setGroupterms(String groupterms) {
		this.groupterms = groupterms;
	}
	public String getAuthstr() {
		return authstr;
	}
	public void setAuthstr(String authstr) {
		this.authstr = authstr;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getAttentiongroup() {
		return attentiongroup;
	}
	public void setAttentiongroup(String attentiongroup) {
		this.attentiongroup = attentiongroup;
	}
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
	public String getBirthyear() {
		return birthyear;
	}
	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}
	public String getBirthmonth() {
		return birthmonth;
	}
	public void setBirthmonth(String birthmonth) {
		this.birthmonth = birthmonth;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public String getZodiac() {
		return zodiac;
	}
	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIdcardtype() {
		return idcardtype;
	}
	public void setIdcardtype(String idcardtype) {
		this.idcardtype = idcardtype;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getBirthprovince() {
		return birthprovince;
	}
	public void setBirthprovince(String birthprovince) {
		this.birthprovince = birthprovince;
	}
	public String getBirthcity() {
		return birthcity;
	}
	public void setBirthcity(String birthcity) {
		this.birthcity = birthcity;
	}
	public String getBirthdist() {
		return birthdist;
	}
	public void setBirthdist(String birthdist) {
		this.birthdist = birthdist;
	}
	public String getBirthcommunity() {
		return birthcommunity;
	}
	public void setBirthcommunity(String birthcommunity) {
		this.birthcommunity = birthcommunity;
	}
	public String getResideprovince() {
		return resideprovince;
	}
	public void setResideprovince(String resideprovince) {
		this.resideprovince = resideprovince;
	}
	public String getResidecity() {
		return residecity;
	}
	public void setResidecity(String residecity) {
		this.residecity = residecity;
	}
	public String getResidedist() {
		return residedist;
	}
	public void setResidedist(String residedist) {
		this.residedist = residedist;
	}
	public String getResidecommunity() {
		return residecommunity;
	}
	public void setResidecommunity(String residecommunity) {
		this.residecommunity = residecommunity;
	}
	public String getResidesuite() {
		return residesuite;
	}
	public void setResidesuite(String residesuite) {
		this.residesuite = residesuite;
	}
	public String getGraduateschool() {
		return graduateschool;
	}
	public void setGraduateschool(String graduateschool) {
		this.graduateschool = graduateschool;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRevenue() {
		return revenue;
	}
	public void setRevenue(String revenue) {
		this.revenue = revenue;
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
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	public String getIcq() {
		return icq;
	}
	public void setIcq(String icq) {
		this.icq = icq;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getYahoo() {
		return yahoo;
	}
	public void setYahoo(String yahoo) {
		this.yahoo = yahoo;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getTaobao() {
		return taobao;
	}
	public void setTaobao(String taobao) {
		this.taobao = taobao;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public String getField4() {
		return field4;
	}
	public void setField4(String field4) {
		this.field4 = field4;
	}
	public String getField5() {
		return field5;
	}
	public void setField5(String field5) {
		this.field5 = field5;
	}
	public String getField6() {
		return field6;
	}
	public void setField6(String field6) {
		this.field6 = field6;
	}
	public String getField7() {
		return field7;
	}
	public void setField7(String field7) {
		this.field7 = field7;
	}
	public String getField8() {
		return field8;
	}
	public void setField8(String field8) {
		this.field8 = field8;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getLastvisit() {
		return lastvisit;
	}
	public void setLastvisit(String lastvisit) {
		this.lastvisit = lastvisit;
	}
	public String getLastactivity() {
		return lastactivity;
	}
	public void setLastactivity(String lastactivity) {
		this.lastactivity = lastactivity;
	}
	public String getLastpost() {
		return lastpost;
	}
	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}
	public String getLastsendmail() {
		return lastsendmail;
	}
	public void setLastsendmail(String lastsendmail) {
		this.lastsendmail = lastsendmail;
	}
	public String getInvisible() {
		return invisible;
	}
	public void setInvisible(String invisible) {
		this.invisible = invisible;
	}
	public String getBuyercredit() {
		return buyercredit;
	}
	public void setBuyercredit(String buyercredit) {
		this.buyercredit = buyercredit;
	}
	public String getSellercredit() {
		return sellercredit;
	}
	public void setSellercredit(String sellercredit) {
		this.sellercredit = sellercredit;
	}
	public String getFavtimes() {
		return favtimes;
	}
	public void setFavtimes(String favtimes) {
		this.favtimes = favtimes;
	}
	public String getSharetimes() {
		return sharetimes;
	}
	public void setSharetimes(String sharetimes) {
		this.sharetimes = sharetimes;
	}
	public String getProfileprogress() {
		return profileprogress;
	}
	public void setProfileprogress(String profileprogress) {
		this.profileprogress = profileprogress;
	}
	public AdminGroup getAdmingroup() {
		return admingroup;
	}
	public void setAdmingroup(AdminGroup admingroup) {
		this.admingroup = admingroup;
	}
	public AdminGroup getGroup() {
		return group;
	}
	public void setGroup(AdminGroup group) {
		this.group = group;
	}
	public String getLastactivitydb() {
		return lastactivitydb;
	}
	public void setLastactivitydb(String lastactivitydb) {
		this.lastactivitydb = lastactivitydb;
	}
	public String getBuyerrank() {
		return buyerrank;
	}
	public void setBuyerrank(String buyerrank) {
		this.buyerrank = buyerrank;
	}
	public String getSellerrank() {
		return sellerrank;
	}
	public void setSellerrank(String sellerrank) {
		this.sellerrank = sellerrank;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getGroupiconid() {
		return groupiconid;
	}
	public void setGroupiconid(String groupiconid) {
		this.groupiconid = groupiconid;
	}

	public String isMyFriend() {
		return isMyFriend;
	}

	@JSONField(name = "is_my_friend")
	public void setIsMyFriend(String isMyFriend) {
		this.isMyFriend = isMyFriend;
	}

	public Extcredit[] getExtcredits() {
		return extcredits;
	}

	public void setExtcredits(Extcredit[] extcredits) {
		this.extcredits = extcredits;
	}
}
