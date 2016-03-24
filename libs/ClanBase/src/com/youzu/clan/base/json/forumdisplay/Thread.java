package com.youzu.clan.base.json.forumdisplay;

import com.youzu.android.framework.json.annotation.JSONField;
import com.youzu.clan.base.json.thread.BaseThread;

public class Thread  extends BaseThread {
    private String posttableid;
    private String sortid;
    private String price;
    private String displayorder;
    private String highlight;
    private String rate;
    private String special;
    private String moderated;
    private String closed;
    private String stickreply;
    private String status;
    private String isgroup;
    private String favtimes;
    private String sharetimes;
    private String stamp;
    private String pushedaid;
    private String cover;
    private String replycredit;
    private String relatebytag;
    private String maxposition;
    private String bgcolor;
    private String comments;
    private String hidden;
    private String threadtable;
    private String threadtableid;
    private String posttable;
    private String allreplies;
    private String is_archived;
    private String archiveid;
    private String subjectenc;
    private String short_subject;
    private String recommendlevel;
    private String heatlevel;
    private String relay;
    private String shareUrl;

    private String recommends;
    private String recommendAdd;
    private String recommend_sub;
    private String enableRecommend;
    private String click2login;
    private String addtext;
    private String subtext;
    private String recommendValue;
    private String recommended;



    //zhaoying add
    private String message;
    //add end

    public String getPosttableid() {
        return posttableid;
    }

    public void setPosttableid(String posttableid) {
        this.posttableid = posttableid;
    }

    public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplayorder() {
        return displayorder;
    }

    public void setDisplayorder(String displayorder) {
        this.displayorder = displayorder;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getModerated() {
        return moderated;
    }

    public void setModerated(String moderated) {
        this.moderated = moderated;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getStickreply() {
        return stickreply;
    }

    public void setStickreply(String stickreply) {
        this.stickreply = stickreply;
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

    @JSONField(name = "recommend_add")
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

    @JSONField(name = "enable_recommend")
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

    @JSONField(name = "recommend_value")
    public void setRecommendValue(String recommendValue) {
        this.recommendValue = recommendValue;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsgroup() {
        return isgroup;
    }

    public void setIsgroup(String isgroup) {
        this.isgroup = isgroup;
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

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }


    public String getPushedaid() {
        return pushedaid;
    }

    public void setPushedaid(String pushedaid) {
        this.pushedaid = pushedaid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getReplycredit() {
        return replycredit;
    }

    public void setReplycredit(String replycredit) {
        this.replycredit = replycredit;
    }

    public String getRelatebytag() {
        return relatebytag;
    }

    public void setRelatebytag(String relatebytag) {
        this.relatebytag = relatebytag;
    }

    public String getMaxposition() {
        return maxposition;
    }

    public void setMaxposition(String maxposition) {
        this.maxposition = maxposition;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public String getThreadtable() {
        return threadtable;
    }

    public void setThreadtable(String threadtable) {
        this.threadtable = threadtable;
    }

    public String getThreadtableid() {
        return threadtableid;
    }

    public void setThreadtableid(String threadtableid) {
        this.threadtableid = threadtableid;
    }

    public String getPosttable() {
        return posttable;
    }

    public void setPosttable(String posttable) {
        this.posttable = posttable;
    }

    public String getAllreplies() {
        return allreplies;
    }

    public void setAllreplies(String allreplies) {
        this.allreplies = allreplies;
    }

    public String getIs_archived() {
        return is_archived;
    }

    public void setIs_archived(String is_archived) {
        this.is_archived = is_archived;
    }

    public String getArchiveid() {
        return archiveid;
    }

    public void setArchiveid(String archiveid) {
        this.archiveid = archiveid;
    }

    public String getSubjectenc() {
        return subjectenc;
    }

    public void setSubjectenc(String subjectenc) {
        this.subjectenc = subjectenc;
    }

    public String getShort_subject() {
        return short_subject;
    }

    public void setShort_subject(String short_subject) {
        this.short_subject = short_subject;
    }

    public String getRecommendlevel() {
        return recommendlevel;
    }

    public void setRecommendlevel(String recommendlevel) {
        this.recommendlevel = recommendlevel;
    }

    public String getHeatlevel() {
        return heatlevel;
    }

    public void setHeatlevel(String heatlevel) {
        this.heatlevel = heatlevel;
    }

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    @JSONField(name = "share_url")
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
